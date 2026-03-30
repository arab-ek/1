package dev.arab.SZAFACORE.manager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import dev.arab.Main;
import dev.arab.SZAFACORE.data.PetData;
import dev.arab.SZAFACORE.pets.Pet;
import dev.arab.SZAFACORE.pets.PetRegistry;
import dev.arab.SZAFACORE.pets.pety.*;
import dev.arab.SZAFACORE.util.TimeConverter;
import dev.arab.SZAFACORE.util.UniqueIdUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class PetManager implements Listener {
    private final Main plugin;
    final Map<UUID, PetData> activePets = new HashMap<>();
    private final Map<UUID, PetData> pendingRespawnPet = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;
    PetRegistry petRegistry;

    public PetManager(Main plugin) {
        this.plugin = plugin;
        this.petRegistry = new PetRegistry(plugin);
        this.registerPets();
        this.loadData();
        this.startPetTask();
        this.startExpiryCheckTask();
    }

    private void registerPets() {
        this.petRegistry.register(new AniolekPet());
        this.petRegistry.register(new PancernikPet());
        this.petRegistry.register(new PedziwiatrPet());
        this.petRegistry.register(new SkalniaczekPet());
        this.petRegistry.register(new EustachyPet());
        this.petRegistry.register(new DuszekPet());
        this.petRegistry.register(new NietoperekPet());
        this.petRegistry.register(new ReniferekPet());
        this.petRegistry.register(new PisklakPet());
        this.petRegistry.register(new SowaPet());
        this.petRegistry.register(new DrakulaPet());
        this.petRegistry.register(new ElfikPet());
        this.petRegistry.register(new BalwanekPet());
        this.petRegistry.register(new ZajaczekPet());
        this.petRegistry.register(new PajakPet());
        this.petRegistry.register(new CukiereczekPet());
        this.petRegistry.register(new RozdymekPet());
        this.petRegistry.register(new TorcikPet());
        this.petRegistry.register(new PierniczekPet());
        this.petRegistry.register(new PaczusPet());
        this.petRegistry.register(new PudzianekPet());
        this.petRegistry.register(new BobasPet());
        this.petRegistry.register(new GlodomorekPet());
        this.petRegistry.register(new DiscordzikPet());
        this.petRegistry.register(new GolemikPet());
        this.petRegistry.register(new CyberObroncaPet());
        this.petRegistry.register(new TelekrolikPet());
        this.petRegistry.register(new EndermanPet());
        this.petRegistry.register(new BiznesmenikPet());
        this.petRegistry.register(new LeniuszekPet());
    }

    private void loadData() {
        // 1. Tworzymy główny folder "data"
        File dataFolder = new File(this.plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        // 2. Łączymy plik pet_data.yml z nowym folderem
        this.dataFile = new File(dataFolder, "pet_data.yml");
        if (!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 3. Wczytujemy konfigurację z dysku
        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);

        // 4. Wczytujemy aktywne pety graczy do pamięci
        for (String key : this.dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String petId = this.dataConfig.getString(key + ".petId");
                String uniqueId = this.dataConfig.getString(key + ".uniqueId");
                long expiry = this.dataConfig.getLong(key + ".expiry");

                this.activePets.put(uuid, new PetData(petId, uniqueId, expiry));
            } catch (Exception e) {
                this.plugin.getLogger().warning("Blad podczas wczytywania peta dla UUID: " + key);
            }
        }
    }

    public void saveData() {
        // Czyścimy starą konfigurację przed zapisem nowych danych
        for (String key : this.dataConfig.getKeys(false)) {
            this.dataConfig.set(key, null);
        }

        // Mapujemy aktywne pety do konfiguracji
        for (Map.Entry<UUID, PetData> entry : this.activePets.entrySet()) {
            String key = entry.getKey().toString();
            PetData data = entry.getValue();

            this.dataConfig.set(key + ".petId", data.getPetId());
            this.dataConfig.set(key + ".uniqueId", data.getUniqueId());
            this.dataConfig.set(key + ".expiry", data.getExpiryTimestamp());
        }

        try {
            // ZAPIS SYNCHRONICZNY: Kluczowy, aby dane nie znikały przy restarcie/wyłączeniu
            this.dataConfig.save(this.dataFile);
        } catch (IOException e) {
            this.plugin.getLogger().severe("Blad podczas zapisu pet_data.yml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void activatePet(Player player, String petId) {
        this.activatePet(player, petId, (String)null, (String)null);
    }

    public void activatePet(Player player, String petId, String uniqueId, String customDuration) {
        long expiryTime;
        if (customDuration != null && !customDuration.isEmpty()) {
            long durationMillis = TimeConverter.parseTimeToMillis(customDuration);
            if (durationMillis == -1L) {
                player.sendMessage("§cNieprawidłowy format czasu! Użyj np: 7d, 24h, 30m");
                return;
            }

            expiryTime = System.currentTimeMillis() + durationMillis;
        } else {
            int durationDays = this.plugin.getConfigManager().getConfig().getInt("settings.pet_system.default_duration_days", 7);
            expiryTime = System.currentTimeMillis() + (long)durationDays * 24L * 60L * 60L * 1000L;
        }

        PetData petData = new PetData(petId, uniqueId, expiryTime);
        this.activePets.put(player.getUniqueId(), petData);
        Pet pet = this.petRegistry.getPet(petId);
        if (pet != null) {
            pet.onEquip(player);
        }

        StatsManager.updateStats(player);
        this.saveData();
        int daysRemaining = petData.getDaysRemaining();
        player.sendTitle(" ", "§aPomyślnie założono peta!", 10, 40, 10);
    }

    public void extendPet(Player player, int days) {
        PetData petData = (PetData)this.activePets.get(player.getUniqueId());
        if (petData != null) {
            petData.extendDays(days);
            this.saveData();
            player.sendTitle(" ", "§aPrzedłużono peta o " + days + " dni!", 10, 40, 10);
        }
    }

    public void removePet(Player player) {
        this.removePet(player, true, false);
    }

    public void removePet(Player player, boolean giveItem) {
        this.removePet(player, giveItem, false);
    }

    public void removePet(Player player, boolean giveItem, boolean silent) {
        PetData petData = (PetData)this.activePets.remove(player.getUniqueId());
        if (petData != null) {
            Pet pet = this.petRegistry.getPet(petData.getPetId());
            if (pet != null) {
                pet.onUnequip(player);
            }

            StatsManager.updateStats(player);
            if (giveItem) {
                long remainingMillis = petData.getExpiryTimestamp() - System.currentTimeMillis();
                String durationStr = TimeConverter.formatMillis(remainingMillis);
                this.givePetItem(player, petData.getPetId(), petData.getUniqueId(), durationStr);
            }

            this.saveData();
            if (!silent) {
                player.sendTitle(" ", "§cPomyślnie zdjęto peta!", 10, 40, 10);
            }

        }
    }

    public PetData getActivePet(Player player) {
        return (PetData)this.activePets.get(player.getUniqueId());
    }

    public String getEquippedPet(Player player) {
        PetData data = (PetData)this.activePets.get(player.getUniqueId());
        return data != null ? data.getPetId() : null;
    }

    public void givePetItem(Player player, String petId) {
        this.givePetItem(player, petId, (String)null, (String)null);
    }

    public void givePetItem(Player player, String petId, String uniqueId, String duration) {
        ItemStack item = this.plugin.getConfigManager().getItem("gui_pets.items." + petId);
        if (item == null) {
            this.plugin.getLogger().warning("Could not find pet item in config: " + petId);
        } else {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "pet_id"), PersistentDataType.STRING, petId);
                if (uniqueId != null) {
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING, uniqueId);
                }

                boolean isAlreadyActivated = uniqueId != null;
                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "is_activated"), PersistentDataType.BYTE, (byte)(isAlreadyActivated ? 1 : 0));
                if (duration != null && !duration.isEmpty()) {
                    long durationMs = TimeConverter.parseTimeToMillis(duration);
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "pet_duration_ms"), PersistentDataType.LONG, durationMs);
                    String displayName = meta.getDisplayName();
                    if (displayName != null) {
                        if (isAlreadyActivated) {
                            displayName = displayName.replaceAll("(?i)\\s*na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "");
                        } else if (displayName.contains("%duration%")) {
                            displayName = displayName.replace("%duration%", duration);
                        } else {
                            displayName = displayName.replaceAll("(?i)na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "na czas " + duration);
                        }

                        meta.setDisplayName(displayName.trim());
                    }

                    List<String> lore = meta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }

                    lore.removeIf((line) -> line.contains("§7Ważność:") || line.contains("§7ID:") || line.contains("Wygaśnie:"));
                    if (isAlreadyActivated) {
                        long expiry = System.currentTimeMillis() + durationMs;
                        String timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(expiry));
                        lore.add("§7Ważność: §e" + timeStr);
                        if (uniqueId != null) {
                            lore.add("§7ID: §f" + uniqueId);
                        }
                    } else if (durationMs == -1L) {
                        lore.add("§7Ważność: §eNa zawsze");
                    } else {
                        lore.add("§7Ważność: §c-");
                    }

                    meta.setLore(lore);
                }

                item.setItemMeta(meta);
            }

            if (player.getInventory().addItem(new ItemStack[]{item}).size() > 0) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }

            if (duration == null || duration.isEmpty()) {
                player.sendMessage("§aOtrzymałeś peta: " + petId);
            }

        }
    }

    private void startPetTask() {
        (new BukkitRunnable() {
            public void run() {
                Iterator var1 = Bukkit.getOnlinePlayers().iterator();

                while(var1.hasNext()) {
                    Player player = (Player)var1.next();
                    PetData petData = (PetData)PetManager.this.activePets.get(player.getUniqueId());
                    if (petData != null) {
                        Pet pet = PetManager.this.petRegistry.getPet(petData.getPetId());
                        if (pet != null) {
                            pet.onTick(player);
                        }
                    }
                }

            }
        }).runTaskTimer(this.plugin, 0L, 20L);
    }

    private void startExpiryCheckTask() {
        (new BukkitRunnable() {
            public void run() {
                Iterator var1 = Bukkit.getOnlinePlayers().iterator();

                while(var1.hasNext()) {
                    Player player = (Player)var1.next();
                    PetData data = (PetData)PetManager.this.activePets.get(player.getUniqueId());
                    if (data != null && data.isExpired()) {
                        PetManager.this.removePet(player, false);
                        player.sendMessage("§cTwój pet wygasł!");
                    }
                }

            }
        }).runTaskTimer(this.plugin, 0L, 72000L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.PLAYER_HEAD) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String petId = (String)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "pet_id"), PersistentDataType.STRING);
                if (petId != null) {
                    if (!event.getAction().name().contains("RIGHT_CLICK")) {
                        return;
                    }

                    event.setCancelled(true);
                    NamespacedKey activatedKey = new NamespacedKey(this.plugin, "is_activated");
                    byte activated = (Byte)meta.getPersistentDataContainer().getOrDefault(activatedKey, PersistentDataType.BYTE, (byte)1);
                    Long customDurationMs = (Long)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "pet_duration_ms"), PersistentDataType.LONG);
                    String uniqueId = (String)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING);
                    if (activated == 0) {
                        meta.getPersistentDataContainer().set(activatedKey, PersistentDataType.BYTE, (byte)1);
                        if (uniqueId == null) {
                            uniqueId = UniqueIdUtils.generateCustomId();
                            meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING, uniqueId);
                        }

                        List<String> lore = meta.getLore();
                        if (lore != null) {
                            lore.removeIf((line) -> line.contains("§7Ważność:") || line.contains("§7ID:") || line.contains("Wygaśnie:"));
                            String timeStr;
                            if (customDurationMs == null) {
                                long duration = 2592000000L;
                                long expiry = System.currentTimeMillis() + duration;
                                timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(expiry));
                            } else if (customDurationMs == -1L) {
                                timeStr = "Na zawsze";
                            } else {
                                long expiry = System.currentTimeMillis() + customDurationMs;
                                timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(expiry));
                            }

                            lore.add("§7Ważność: §e" + timeStr);
                            lore.add("§7ID: §f" + uniqueId);
                            meta.setLore(lore);
                        }

                        item.setItemMeta(meta);
                        player.sendMessage("§aAktywowano peta!");
                    }

                    if (this.activePets.containsKey(player.getUniqueId())) {
                        this.removePet(player, true, true);
                    }

                    item.setAmount(item.getAmount() - 1);
                    if (customDurationMs != null) {
                        this.activatePetWithMs(player, petId, uniqueId, customDurationMs);
                    } else {
                        this.activatePet(player, petId, uniqueId, (String)null);
                    }
                }

            }
        }
    }

    private void activatePetWithMs(Player player, String petId, String uniqueId, long durationMs) {
        long expiryTime = System.currentTimeMillis() + durationMs;
        PetData petData = new PetData(petId, uniqueId, expiryTime);
        this.activePets.put(player.getUniqueId(), petData);
        Pet pet = this.petRegistry.getPet(petId);
        if (pet != null) {
            pet.onEquip(player);
        }

        StatsManager.updateStats(player);
        this.saveData();
        player.sendTitle(" ", "§aPomyślnie założono peta!", 10, 40, 10);
    }

    public double getHealthBonus(Player player) {
        PetData petData = (PetData)this.activePets.get(player.getUniqueId());
        if (petData == null) {
            return 0.0D;
        } else {
            Pet pet = this.petRegistry.getPet(petData.getPetId());
            return pet != null ? pet.getHealthBonus(player) : 0.0D;
        }
    }

    public double getAttackBonus(Player player) {
        PetData petData = (PetData)this.activePets.get(player.getUniqueId());
        if (petData == null) {
            return 0.0D;
        } else {
            Pet pet = this.petRegistry.getPet(petData.getPetId());
            return pet != null ? pet.getAttackBonus(player) : 0.0D;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PetData petData = (PetData)this.activePets.get(player.getUniqueId());
        if (petData != null) {
            this.pendingRespawnPet.put(player.getUniqueId(), petData);
            this.removePet(player, false, true);
        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PetData data = (PetData)this.pendingRespawnPet.remove(player.getUniqueId());
        if (data != null) {
            long remainingMs = data.getExpiryTimestamp() - System.currentTimeMillis();
            if (this.activePets.containsKey(player.getUniqueId())) {
                return;
            }

            if (remainingMs > 0L || data.getExpiryTimestamp() == -1L) {
                this.activatePetWithMs(player, data.getPetId(), data.getUniqueId(), data.getExpiryTimestamp() == -1L ? -1L : Math.max(1L, remainingMs));
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PetData data = (PetData)this.activePets.get(player.getUniqueId());
        if (data != null) {
            if (data.isExpired()) {
                this.removePet(player, false, true);
                return;
            }

            StatsManager.updateStats(player);
            Pet pet = this.petRegistry.getPet(data.getPetId());
            if (pet != null) {
                pet.onEquip(player);
            }
        }

    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            PetData petData = (PetData)this.activePets.get(player.getUniqueId());
            if (petData != null) {
                Pet pet = this.petRegistry.getPet(petData.getPetId());
                if (pet != null) {
                    pet.onFoodChange(player, event);
                }

            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            PetData petData = (PetData)this.activePets.get(player.getUniqueId());
            if (petData != null) {
                Pet pet = this.petRegistry.getPet(petData.getPetId());
                if (pet != null) {
                    pet.onDamage(player, event);
                }

            }
        }
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof Player) {
                Player attacker = (Player)event.getDamager();
                Player target = (Player)event.getEntity();
                PetData petData = (PetData)this.activePets.get(attacker.getUniqueId());
                if (petData != null) {
                    if ("sowa".equals(petData.getPetId())) {
                        SowaPet.recordHit(attacker.getUniqueId(), target.getUniqueId());
                    }

                }
            }
        }
    }
}