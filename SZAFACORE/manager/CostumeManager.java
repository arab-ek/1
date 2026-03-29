package dev.arab.SZAFACORE.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import dev.arab.Main;
import dev.arab.EVENTOWKICORE.eventowki.RozdzkaIluzjonisty;
import dev.arab.EVENTOWKICORE.utils.ScoreboardUtils;
import dev.arab.SZAFACORE.costumes.Costume;
import dev.arab.SZAFACORE.costumes.CostumeRegistry;
import dev.arab.SZAFACORE.costumes.kostiumy.GrinchCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.MalyUrwisCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.MikolajCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.MimCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.NurekCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.PiratCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.PrzeciwzakazeniowyCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.WalentynkowyCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.WampirCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.ZbojczyKrolikCostume;
import dev.arab.SZAFACORE.costumes.kostiumy.PrezydentCostume;
import dev.arab.SZAFACORE.data.CostumeData;
import dev.arab.SZAFACORE.util.TimeConverter;
import dev.arab.SZAFACORE.util.UniqueIdUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public class CostumeManager implements Listener {
    private final Main plugin;
    final Map<UUID, CostumeData> activeCostumes = new HashMap<>();
    private final Map<UUID, Set<String>> unlockedCostumes = new HashMap<>();
    private final Map<UUID, Long> lastShiftTime = new HashMap<>();
    private final Map<UUID, Integer> shiftCount = new HashMap<>();
    final Map<UUID, Map<String, Long>> skillCooldown = new HashMap<>();
    private final Map<UUID, CostumeData> pendingRespawnCostume = new HashMap<>();
    final Map<UUID, Long> mimeActiveUntil = new HashMap<>();
    final Map<UUID, Long> grinchActiveUntil = new HashMap<>();
    private final Map<UUID, Map<UUID, Long>> infectedBy = new HashMap<>();
    final Map<UUID, BossBar> activeBossBars = new HashMap<>();
    final Map<UUID, Map<String, BossBar>> cooldownBossBars = new HashMap<>();
    private final Map<String, ItemStack[]> armorCache = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;
    final CostumeRegistry costumeRegistry;
    private final Map<String, String> displayNameToCostumeId = new HashMap<>();
    private boolean dirty = false;
    final Map<Block, Long> snowBlocks = new HashMap<>();
    private final Map<UUID, String> itemBasedCache = new HashMap<>();
    private final Map<UUID, Long> lastItemBasedCheck = new HashMap<>();
    private final NamespacedKey costumeItemKey = new NamespacedKey(Main.getPlugin(Main.class), "costume_id");

    public CostumeManager(Main plugin) {
        this.plugin = plugin;
        this.costumeRegistry = new CostumeRegistry(plugin);
        this.costumeRegistry.register(new WalentynkowyCostume());
        this.costumeRegistry.register(new PrzeciwzakazeniowyCostume());
        this.costumeRegistry.register(new NurekCostume());
        this.costumeRegistry.register(new MimCostume());
        this.costumeRegistry.register(new WampirCostume());
        this.costumeRegistry.register(new MikolajCostume());
        this.costumeRegistry.register(new GrinchCostume());
        this.costumeRegistry.register(new ZbojczyKrolikCostume(plugin));
        this.costumeRegistry.register(new MalyUrwisCostume());
        this.costumeRegistry.register(new PiratCostume());
        this.costumeRegistry.register(new PrezydentCostume(plugin));

        this.loadData();
        this.populateDisplayNameMap();
        this.startCostumeTask();
        this.startCleanupTask();
    }

    private void populateDisplayNameMap() {
        this.displayNameToCostumeId.clear();
        Iterator<String> var1 = this.costumeRegistry.getAllCostumes().keySet().iterator();

        while(var1.hasNext()) {
            String id = var1.next();
            ItemStack visual = this.getCostumeVisualItem(id);
            if (visual != null && visual.hasItemMeta() && visual.getItemMeta().hasDisplayName()) {
                String name = ChatColor.stripColor(visual.getItemMeta().getDisplayName());
                this.displayNameToCostumeId.put(name.toLowerCase(), id);
            }
        }
    }

    public CostumeRegistry getRegistry() {
        return this.costumeRegistry;
    }

    private void startCleanupTask() {
        (new BukkitRunnable() {
            public void run() {
                long now = System.currentTimeMillis();
                Iterator<Entry<Block, Long>> it = CostumeManager.this.snowBlocks.entrySet().iterator();

                while(it.hasNext()) {
                    Entry<Block, Long> entry = it.next();
                    if (now > entry.getValue()) {
                        Block b = entry.getKey();
                        if (b.getType() == Material.SNOW) {
                            b.setType(Material.AIR);
                        }
                        it.remove();
                    }
                }
            }
        }).runTaskTimer(this.plugin, 0L, 100L);
    }

    public void reload() {
        this.armorCache.clear();
    }

    private void loadData() {
        File folder = this.plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.dataFile = new File(folder, "costume_data.yml");
        if (this.dataFile.exists() && this.dataFile.isDirectory()) {
            this.dataFile.delete();
        }

        if (!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
            } catch (IOException var11) {
                this.plugin.getLogger().severe("Błąd podczas tworzenia pliku costume_data.yml: " + var11.getMessage());
            }
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
        Iterator<String> var2 = this.dataConfig.getKeys(false).iterator();

        while(var2.hasNext()) {
            String key = var2.next();

            try {
                UUID uuid = UUID.fromString(key);
                String activeId = this.dataConfig.getString(key + ".active");
                String uniqueId = this.dataConfig.getString(key + ".uniqueId");
                long expiry = this.dataConfig.getLong(key + ".expiry", -1L);
                if (activeId != null) {
                    this.activeCostumes.put(uuid, new CostumeData(activeId, uniqueId, expiry));
                }

                List<String> unlocked = this.dataConfig.getStringList(key + ".unlocked");
                if (unlocked != null) {
                    this.unlockedCostumes.put(uuid, new HashSet<>(unlocked));
                }
            } catch (Exception var10) {
            }
        }
    }

    public void saveData() {
        if (this.dirty && this.dataFile != null) {
            FileConfiguration copy = new YamlConfiguration();
            Iterator<Entry<UUID, CostumeData>> var2 = this.activeCostumes.entrySet().iterator();

            Entry<UUID, CostumeData> entry;
            while(var2.hasNext()) {
                entry = var2.next();
                String uuid = entry.getKey().toString();
                copy.set(uuid + ".active", entry.getValue().getCostumeId());
                copy.set(uuid + ".uniqueId", entry.getValue().getUniqueId());
                copy.set(uuid + ".expiry", entry.getValue().getExpiryTime());
            }

            Iterator<Entry<UUID, Set<String>>> var3 = this.unlockedCostumes.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<UUID, Set<String>> unlockedEntry = var3.next();
                copy.set(unlockedEntry.getKey().toString() + ".unlocked", new ArrayList<>(unlockedEntry.getValue()));
            }

            this.dirty = false;
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                try {
                    copy.save(this.dataFile);
                } catch (IOException var3x) {
                    this.plugin.getLogger().severe("Błąd podczas zapisywania costume_data.yml! " + var3x.getMessage());
                }
            });
        }
    }

    public boolean isUnlocked(Player player, String costumeId) {
        Set<String> unlocked = this.unlockedCostumes.get(player.getUniqueId());
        return unlocked != null && unlocked.contains(costumeId);
    }

    public void unlockCostume(Player player, String costumeId) {
        this.unlockedCostumes.computeIfAbsent(player.getUniqueId(), (k) -> new HashSet<>()).add(costumeId);
        this.dirty = true;
        this.saveData();
    }

    public ItemStack getCostumeVisualItem(String costumeId) {
        return this.plugin.getConfigManager().getItem("gui_costumes.items." + costumeId);
    }

    public String getActiveCostume(Player player) {
        CostumeData data = this.activeCostumes.get(player.getUniqueId());
        if (data != null) {
            return data.getCostumeId();
        } else {
            long now = System.currentTimeMillis();
            UUID uuid = player.getUniqueId();
            if (now - this.lastItemBasedCheck.getOrDefault(uuid, 0L) < 1000L) {
                return this.itemBasedCache.get(uuid);
            } else {
                String id = this.getItemBasedCostumeId(player);
                this.itemBasedCache.put(uuid, id);
                this.lastItemBasedCheck.put(uuid, now);
                return id;
            }
        }
    }

    public String getItemBasedCostumeId(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item != null && item.getType() != Material.AIR && item.getType().name().startsWith("LEATHER_") && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                String id = this.displayNameToCostumeId.get(name.toLowerCase());
                if (id != null) {
                    return id;
                }
            }
        }
        return null;
    }

    public CostumeData getActiveCostumeData(Player player) {
        CostumeData data = this.activeCostumes.get(player.getUniqueId());
        if (data != null) {
            return data;
        } else {
            String itemBasedId = this.getItemBasedCostumeId(player);
            return itemBasedId != null ? new CostumeData(itemBasedId, "ITEM_BASED", -1L) : null;
        }
    }

    public boolean isGrinch(Player player) {
        return this.costumeIdEquals(player, "grincha");
    }

    public boolean isGrinchSkillActive(Player player) {
        return this.grinchActiveUntil.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis();
    }

    public void equipCostume(Player player, String costumeId, String uniqueId, long durationMs) {
        String current = this.getActiveCostume(player);
        this.removeCostume(player, current == null || !costumeId.equals(current));
        long expiry = -1L;
        if (durationMs > 0L) {
            expiry = System.currentTimeMillis() + durationMs;
        } else if (durationMs == 0L) {
            int defaultDays = this.plugin.getConfigManager().getConfig().getInt("settings.costume_system.default_duration_days", 7);
            expiry = System.currentTimeMillis() + (long)defaultDays * 24L * 60L * 60L * 1000L;
        }

        this.activeCostumes.put(player.getUniqueId(), new CostumeData(costumeId, uniqueId, expiry));
        this.dirty = true;
        this.saveData();
        StatsManager.updateStats(player);
        this.applyGhostArmor(player, costumeId);
        Costume costume = this.costumeRegistry.getCostume(costumeId);
        if (costume != null) {
            costume.onEquip(player);
        }
    }

    public void removeCostume(Player player) {
        this.removeCostume(player, true);
    }

    public void removeCostume(Player player, boolean giveItem) {
        CostumeData oldData = this.activeCostumes.remove(player.getUniqueId());
        if (oldData != null) {
            StatsManager.updateStats(player);
            this.removeGhostArmor(player);
            Costume costume = this.costumeRegistry.getCostume(oldData.getCostumeId());
            if (costume != null) {
                costume.onUnequip(player);
            }

            player.updateInventory();
            UUID uuid = player.getUniqueId();
            long now = System.currentTimeMillis();
            if (this.mimeActiveUntil.getOrDefault(uuid, 0L) > now) {
                this.skillCooldown.computeIfAbsent(uuid, (k) -> new HashMap<>()).put("mima", now);
            }

            if (this.grinchActiveUntil.getOrDefault(uuid, 0L) > now) {
                this.skillCooldown.computeIfAbsent(uuid, (k) -> new HashMap<>()).put("grincha", now);
            }

            if (this.infectedBy.containsKey(uuid) && !this.infectedBy.get(uuid).isEmpty()) {
                this.skillCooldown.computeIfAbsent(uuid, (k) -> new HashMap<>()).put("przeciwzakazeniowy", now);
            }

            this.mimeActiveUntil.remove(uuid);
            this.grinchActiveUntil.remove(uuid);
            this.infectedBy.remove(uuid);
            BossBar activeBar = this.activeBossBars.remove(uuid);
            if (activeBar != null) {
                activeBar.removePlayer(player);
            }

            if (giveItem) {
                long remaining = oldData.getExpiryTime() == -1L ? -1L : oldData.getExpiryTime() - System.currentTimeMillis();
                this.giveCostumeItem(player, oldData.getCostumeId(), oldData.getUniqueId(), Math.max(-1L, remaining));
            }
        }

        this.dirty = true;
        this.saveData();
    }

    public double getHealthBonus(Player player) {
        return 0.0D;
    }

    public double getAttackBonus(Player player) {
        return 0.0D;
    }

    private void startCostumeTask() {
        (new BukkitRunnable() {
            public void run() {
                long now = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    try {
                        // 🔥 KLUCZOWE — ciągłe pilnowanie statów
                        StatsManager.updateStats(player);

                        String costumeId = CostumeManager.this.getActiveCostume(player);
                        if (costumeId == null) continue;

                        CostumeData data = CostumeManager.this.activeCostumes.get(player.getUniqueId());
                        if (data != null && data.isExpired()) {
                            CostumeManager.this.removeCostume(player, false);
                            player.sendMessage("§cTwój kostium wygasł!");
                            continue;
                        }

                        CostumeManager.this.updateVisualHiding(player);

                        Costume costume = CostumeManager.this.costumeRegistry.getCostume(costumeId);
                        if (costume != null) {
                            costume.onTick(player);
                        }

                        if (costumeId.equals("mikolaja") && player.isOnGround()) {
                            Block b = player.getLocation().getBlock();
                            if (b.getType() == Material.AIR || b.getType() == Material.SNOW) {
                                b.setType(Material.SNOW);
                                CostumeManager.this.snowBlocks.put(b, now + 5000L);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).runTaskTimer(this.plugin, 20L, 20L);
    }

    public void updateVisualHiding(Player player) {
        String costumeId = this.getActiveCostume(player);
        if (costumeId != null && !RozdzkaIluzjonisty.isPlayerHidden(player.getUniqueId())) {
            ItemStack head, chest, leggings, boots;

            if (player.getGameMode() == GameMode.CREATIVE) {
                head = player.getInventory().getHelmet();
                chest = player.getInventory().getChestplate();
                leggings = player.getInventory().getLeggings();
                boots = player.getInventory().getBoots();
            } else {
                ItemStack[] visualArmor = this.getCostumeArmor(costumeId);
                if (visualArmor == null) return;

                head = visualArmor[0];
                chest = visualArmor[1];
                ItemStack realChest = player.getInventory().getChestplate();
                if (realChest != null && realChest.getType() == Material.ELYTRA) {
                    chest = realChest;
                }
                leggings = visualArmor[2];
                boots = visualArmor[3];
            }

            double distSq = 4096.0D;
            for (Player other : player.getWorld().getPlayers()) {
                if (other.getLocation().distanceSquared(player.getLocation()) <= distSq) {
                    other.sendEquipmentChange(player, EquipmentSlot.HEAD, head);
                    other.sendEquipmentChange(player, EquipmentSlot.CHEST, chest);
                    other.sendEquipmentChange(player, EquipmentSlot.LEGS, leggings);
                    other.sendEquipmentChange(player, EquipmentSlot.FEET, boots);
                }
            }
        }
    }

    private void updateVisualHidingFor(Player wearer, Player observer) {
        String costumeId = this.getActiveCostume(wearer);
        if (costumeId != null && !RozdzkaIluzjonisty.isPlayerHidden(wearer.getUniqueId())) {
            ItemStack head, chest, leggings, boots;

            if (wearer.getGameMode() == GameMode.CREATIVE) {
                head = wearer.getInventory().getHelmet();
                chest = wearer.getInventory().getChestplate();
                leggings = wearer.getInventory().getLeggings();
                boots = wearer.getInventory().getBoots();
            } else {
                ItemStack[] visualArmor = this.getCostumeArmor(costumeId);
                if (visualArmor == null) return;

                head = visualArmor[0];
                chest = visualArmor[1];
                ItemStack realChest = wearer.getInventory().getChestplate();
                if (realChest != null && realChest.getType() == Material.ELYTRA) {
                    chest = realChest;
                }
                leggings = visualArmor[2];
                boots = visualArmor[3];
            }

            observer.sendEquipmentChange(wearer, EquipmentSlot.HEAD, head);
            observer.sendEquipmentChange(wearer, EquipmentSlot.CHEST, chest);
            observer.sendEquipmentChange(wearer, EquipmentSlot.LEGS, leggings);
            observer.sendEquipmentChange(wearer, EquipmentSlot.FEET, boots);
        }
    }

    public ItemStack[] getCostumeArmor(String costumeId) {
        ItemStack[] armor = this.armorCache.get(costumeId);
        if (armor != null) {
            return armor;
        } else {
            ItemStack chest = this.plugin.getConfigManager().getItem("gui_costumes.items." + costumeId);
            if (chest == null) {
                return null;
            } else {
                ItemMeta cMeta = chest.getItemMeta();
                if (cMeta != null) {
                    cMeta.setLore(null);
                    cMeta.addItemFlags(ItemFlag.values());
                    chest.setItemMeta(cMeta);
                }

                ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                ItemStack head = new ItemStack(Material.AIR);
                String displayName = chest.getItemMeta() != null ? chest.getItemMeta().getDisplayName() : null;
                if (chest.getItemMeta() instanceof LeatherArmorMeta) {
                    Color color = ((LeatherArmorMeta)chest.getItemMeta()).getColor();
                    LeatherArmorMeta lMeta = (LeatherArmorMeta)leggings.getItemMeta();
                    if (lMeta != null) {
                        lMeta.setColor(color);
                        lMeta.addItemFlags(ItemFlag.HIDE_DYE);
                        lMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        if (displayName != null) {
                            lMeta.setDisplayName(displayName);
                        }

                        leggings.setItemMeta(lMeta);
                    }

                    LeatherArmorMeta bMeta = (LeatherArmorMeta)boots.getItemMeta();
                    if (bMeta != null) {
                        bMeta.setColor(color);
                        bMeta.addItemFlags(ItemFlag.HIDE_DYE);
                        bMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        if (displayName != null) {
                            bMeta.setDisplayName(displayName);
                        }

                        boots.setItemMeta(bMeta);
                    }
                }

                String texture = this.plugin.getConfigManager().getConfig().getString("gui_costumes.items." + costumeId + ".value");
                if (texture == null || texture.isEmpty()) {
                    texture = this.plugin.getConfigManager().getConfig().getString("gui_costumes.items." + costumeId + ".texture");
                    if (texture != null && !texture.isEmpty() && texture.startsWith("http")) {
                        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + texture + "\"}}}";
                        texture = Base64.getEncoder().encodeToString(json.getBytes());
                    }
                }

                if (texture != null && !texture.isEmpty()) {
                    head = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta)head.getItemMeta();
                    if (meta != null) {
                        if (displayName != null) {
                            meta.setDisplayName(displayName);
                        }

                        try {
                            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                            profile.setProperty(new ProfileProperty("textures", texture));
                            meta.setPlayerProfile(profile);
                            head.setItemMeta(meta);
                        } catch (Throwable var12) {
                        }
                    }
                }

                armor = new ItemStack[]{head, chest, leggings, boots};
                this.armorCache.put(costumeId, armor);
                return armor;
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            String costumeId = this.getActiveCostume(player);
            if (costumeId != null) {
                Costume costume = this.costumeRegistry.getCostume(costumeId);
                if (costume != null) {
                    boolean cancel = costume.onDamage(player, event);
                    if (cancel) {
                        event.setCancelled(true);
                        return;
                    }
                }

                if (this.mimeActiveUntil.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
                    if (event instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent edee = (EntityDamageByEntityEvent)event;
                        if (edee.getDamager() instanceof Player) {
                            Player attacker = (Player)edee.getDamager();
                            attacker.damage(event.getDamage());
                        }
                    }

                    event.setCancelled(true);
                } else {
                    if (costume != null) {
                        double multiplier = costume.getDefenseMultiplier(player, event);
                        if (multiplier != 1.0D) {
                            event.setDamage(event.getDamage() * multiplier);
                        }
                    }

                }
            }
        }
    }

    public boolean isCostumeItem(ItemStack item) {
        return item != null && item.hasItemMeta() ? item.getItemMeta().getPersistentDataContainer().has(this.costumeItemKey, PersistentDataType.STRING) : false;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player)event.getDamager();
            String costumeId = this.getActiveCostume(player);
            if (costumeId != null) {
                Costume costume = this.costumeRegistry.getCostume(costumeId);
                if (costume != null) {
                    costume.onAttack(player, event);
                    double multiplier = costume.getAttackMultiplier(player, event);
                    Map<UUID, Long> myInfected = this.infectedBy.get(player.getUniqueId());
                    if (myInfected != null) {
                        Entity var9 = event.getEntity();
                        if (var9 instanceof Player) {
                            Player victim = (Player)var9;
                            Long expiry = myInfected.get(victim.getUniqueId());
                            if (expiry != null && expiry > System.currentTimeMillis()) {
                                multiplier *= 1.5D;
                            }
                        }
                    }

                    if (multiplier != 1.0D) {
                        event.setDamage(event.getDamage() * multiplier);
                    }
                }

            }
        }
    }

    private void handleStealing(Player attacker, Player victim, String costumeId) {
        String path = "settings.costume_system.stealing." + costumeId;
        double percentage = this.plugin.getConfigManager().getDouble(path + ".percentage");
        Economy econ = Main.getEconomy();
        if (econ != null) {
            double victimBalance = econ.getBalance(victim);
            double amount = victimBalance * (percentage / 100.0D);
            amount = (double)Math.round(amount * 100.0D) / 100.0D;
            if (amount > 0.0D) {
                econ.withdrawPlayer(victim, amount);
                econ.depositPlayer(attacker, amount);
                String title = this.plugin.getConfigManager().getColoredString(path + ".title");
                String subtitle = this.plugin.getConfigManager().getColoredString(path + ".subtitle").replace("%amount%", String.valueOf(amount)).replace("%player%", victim.getName());
                attacker.sendTitle(title, subtitle, 10, 40, 10);
            }

        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            String costumeId = this.getActiveCostume(killer);
            if (costumeId != null) {
                Costume costume = this.costumeRegistry.getCostume(costumeId);
                if (costume != null) {
                    costume.onKill(killer, player);
                }

                if (costumeId.equals("malego_urwisa") || costumeId.equals("grincha") || costumeId.equals("pirata")) {
                    this.handleStealing(killer, player, costumeId);
                }
            }
        }

        CostumeData costumeData = this.activeCostumes.get(player.getUniqueId());
        if (costumeData != null) {
            this.pendingRespawnCostume.put(player.getUniqueId(), costumeData);
            this.removeCostume(player, false);
        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        CostumeData data = this.pendingRespawnCostume.remove(player.getUniqueId());

        if (data != null) {
            this.equipCostume(player, data.getCostumeId(), data.getUniqueId(),
                    data.getExpiryTime() == -1L ? -1L : Math.max(0L, data.getExpiryTime() - System.currentTimeMillis()));

            // 🔥 dodaj to
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                StatsManager.updateStats(player);
            }, 20L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CostumeData data = this.activeCostumes.get(player.getUniqueId());

        if (data != null) {
            if (data.isExpired()) {
                this.activeCostumes.remove(player.getUniqueId());
                this.saveData();
                return;
            }

            // 🔥 pierwsze ustawienie
            StatsManager.updateStats(player);

            Costume costume = this.costumeRegistry.getCostume(data.getCostumeId());
            if (costume != null) {
                costume.onEquip(player);
            }

            // 🔥 KLUCZOWE — opóźnione nadpisanie (naprawia bug 10 serc)
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (player.isOnline()) {
                    StatsManager.updateStats(player);
                }
            }, 20L); // 1 sekunda

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                this.updateVisualHiding(player);
            }, 20L);
        }

        // pokazanie kostiumów innych graczy
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            if (player.isOnline()) {
                for (Entry<UUID, CostumeData> entry : this.activeCostumes.entrySet()) {
                    Player wearer = Bukkit.getPlayer(entry.getKey());
                    if (wearer != null && wearer.isOnline() && wearer.getWorld().equals(player.getWorld())) {
                        this.updateVisualHidingFor(wearer, player);
                    }
                }
            }
        }, 20L);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (this.getActiveCostume(player) != null) {
            this.updateVisualHiding(player);
        }

        Iterator<Entry<UUID, CostumeData>> var3 = this.activeCostumes.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<UUID, CostumeData> entry = var3.next();
            Player wearer = Bukkit.getPlayer(entry.getKey());
            if (wearer != null && wearer.isOnline() && wearer.getWorld().equals(player.getWorld())) {
                this.updateVisualHidingFor(wearer, player);
            }
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            if (this.getActiveCostume(player) != null) {
                this.updateVisualHiding(player);
            }
        }, 1L);
    }

    @EventHandler
    public void onJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        String costumeId = this.getActiveCostume(player);
        if (costumeId != null) {
            Costume costume = this.costumeRegistry.getCostume(costumeId);
            if (costume != null) {
                costume.onJump(player, event);
            }
        }

    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            Player player = event.getPlayer();
            String costumeId = this.getActiveCostume(player);
            if (costumeId != null) {
                Costume costume = this.costumeRegistry.getCostume(costumeId);
                if (costume != null) {
                    costume.onSneak(player, event);
                }

                if (costumeId.equals("mima") || costumeId.equals("grincha") || costumeId.equals("przeciwzakazeniowy")) {
                    long now = System.currentTimeMillis();
                    if (now - this.lastShiftTime.getOrDefault(player.getUniqueId(), 0L) > 500L) {
                        this.shiftCount.put(player.getUniqueId(), 1);
                    } else {
                        this.shiftCount.put(player.getUniqueId(), this.shiftCount.getOrDefault(player.getUniqueId(), 0) + 1);
                    }

                    this.lastShiftTime.put(player.getUniqueId(), now);
                    if (this.shiftCount.get(player.getUniqueId()) >= 3) {
                        this.shiftCount.put(player.getUniqueId(), 0);
                        this.handleShiftSkill(player, costumeId);
                    }
                }
            }

        }
    }

    private void handleShiftSkill(final Player player, String costumeId) {
        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();
        Map<String, Long> userCooldowns = this.skillCooldown.get(uuid);
        if (userCooldowns == null) {
            userCooldowns = new HashMap<>();
            this.skillCooldown.put(uuid, userCooldowns);
        }

        long lastUse = userCooldowns.getOrDefault(costumeId, 0L);
        if (now - lastUse < 120000L) {
            this.showCooldownBar(player, costumeId, 120000L - (now - lastUse));
        } else {
            final BossBar bossBar;
            if (costumeId.equals("mima")) {
                if (this.mimeActiveUntil.getOrDefault(player.getUniqueId(), 0L) > now) {
                    player.sendMessage("§cTa umiejętność jest już aktywna!");
                } else {
                    this.mimeActiveUntil.put(player.getUniqueId(), now + 10000L);
                    player.sendTitle(" ", "§5Mim Aktywny! §7Masz §f10s §7na odbijanie ciosów", 5, 40, 5);
                    bossBar = Bukkit.createBossBar("§5Mim Aktywny! §7Odbijasz ciosy jeszcze przez §f10s", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
                    bossBar.addPlayer(player);
                    this.activeBossBars.put(player.getUniqueId(), bossBar);
                    (new BukkitRunnable() {
                        int timeLeft = 10;

                        public void run() {
                            if (player.isOnline() && this.timeLeft > 0 && CostumeManager.this.costumeIdEquals(player, "mima")) {
                                bossBar.setTitle("§5Mim Aktywny! §7Odbijasz ciosy jeszcze przez §f" + this.timeLeft + "s");
                                bossBar.setProgress((double)this.timeLeft / 10.0D);
                                --this.timeLeft;
                            } else {
                                CostumeManager.this.mimeActiveUntil.remove(player.getUniqueId());
                                CostumeManager.this.activeBossBars.remove(player.getUniqueId());
                                bossBar.removePlayer(player);
                                this.cancel();
                                if (player.isOnline()) {
                                    CostumeManager.this.skillCooldown.computeIfAbsent(player.getUniqueId(), (k) -> new HashMap<>()).put("mima", System.currentTimeMillis());
                                    if (CostumeManager.this.costumeIdEquals(player, "mima")) {
                                        CostumeManager.this.showCooldownBar(player, "mima", 120000L);
                                    }
                                }

                            }
                        }
                    }).runTaskTimer(this.plugin, 0L, 20L);
                }
            } else if (costumeId.equals("grincha")) {
                if (this.grinchActiveUntil.getOrDefault(player.getUniqueId(), 0L) > now) {
                    player.sendMessage("§cTa umiejętność jest już aktywna!");
                } else {
                    this.grinchActiveUntil.put(player.getUniqueId(), now + 10000L);
                    player.sendTitle("§a§lGRINCH!", "§7Jesteś odporny na §ceventówki §7przez §f10s§7!", 5, 40, 5);
                    bossBar = Bukkit.createBossBar("§2Grinch aktywny! §7Odrzucasz eventówki §7jeszcze przez §a10s!", BarColor.GREEN, BarStyle.SOLID, new BarFlag[0]);
                    bossBar.addPlayer(player);
                    this.activeBossBars.put(player.getUniqueId(), bossBar);
                    (new BukkitRunnable() {
                        int timeLeft = 10;

                        public void run() {
                            if (player.isOnline() && this.timeLeft > 0 && CostumeManager.this.costumeIdEquals(player, "grincha")) {
                                bossBar.setTitle("§2Grinch aktywny! §7Odrzucasz eventówki §7jeszcze przez §a" + this.timeLeft + "s!");
                                bossBar.setProgress((double)this.timeLeft / 10.0D);
                                --this.timeLeft;
                            } else {
                                CostumeManager.this.grinchActiveUntil.remove(player.getUniqueId());
                                CostumeManager.this.activeBossBars.remove(player.getUniqueId());
                                bossBar.removePlayer(player);
                                this.cancel();
                                if (player.isOnline()) {
                                    CostumeManager.this.skillCooldown.computeIfAbsent(player.getUniqueId(), (k) -> new HashMap<>()).put("grincha", System.currentTimeMillis());
                                    if (CostumeManager.this.costumeIdEquals(player, "grincha")) {
                                        CostumeManager.this.showCooldownBar(player, "grincha", 120000L);
                                    }
                                }

                            }
                        }
                    }).runTaskTimer(this.plugin, 0L, 20L);
                }
            } else if (costumeId.equals("przeciwzakazeniowy")) {
                RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 10.0D, (entity) -> entity instanceof Player && !entity.equals(player));
                if (result != null) {
                    Entity var11 = result.getHitEntity();
                    if (var11 instanceof Player) {
                        final Player victim = (Player)var11;
                        Map<UUID, Long> myInfected = this.infectedBy.computeIfAbsent(player.getUniqueId(), (k) -> new HashMap<>());
                        myInfected.put(victim.getUniqueId(), now + 10000L);
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
                        this.skillCooldown.computeIfAbsent(player.getUniqueId(), (k) -> new HashMap<>()).put("przeciwzakazeniowy", now);
                        this.showCooldownBar(player, "przeciwzakazeniowy", 120000L);
                        victim.sendTitle("§4§lZostałeś zainfekowany!", "§7Zostałeś zainfekowany przez gracza §f" + player.getName() + "!", 5, 40, 5);
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
                        ScoreboardUtils.applyRgbTeam(victim, ChatColor.GREEN);
                        player.sendTitle("§7Zainfekowałeś §f" + victim.getName() + "!", " ", 5, 40, 5);
                        (new BukkitRunnable() {
                            public void run() {
                                if (victim.isOnline()) {
                                    ScoreboardUtils.removeFromRgbTeams(victim);
                                }

                            }
                        }).runTaskLater(this.plugin, 200L);
                        return;
                    }
                }

                player.sendMessage("§cMusisz patrzeć na gracza, §4aby go zainfekować!");
            }
        }
    }

    void showCooldownBar(final Player player, final String costumeId, final long remainingMs) {
        Map<String, BossBar> userBars = this.cooldownBossBars.computeIfAbsent(player.getUniqueId(), (k) -> new HashMap<>());
        if (!userBars.containsKey(costumeId)) {
            String costumeNameTemp = costumeId;
            if (costumeId.equals("grincha")) {
                costumeNameTemp = "§agrincha";
            } else if (costumeId.equals("mima")) {
                costumeNameTemp = "§5mima";
            } else if (costumeId.equals("przeciwzakazeniowy")) {
                costumeNameTemp = "§aprzeciwzakarzeniowy";
            }
            final String costumeName = costumeNameTemp;

            final BossBar bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
            long seconds = remainingMs / 1000L % 60L;
            long minutes = remainingMs / 60000L;
            String var10000 = minutes > 0L ? minutes + "m " : "";
            String timeStr = var10000 + seconds + "s";
            bossBar.setTitle("§7Kostium " + costumeName + " §7wykorzystany! Odnawianie umiejetnosci... §8(§f" + timeStr + "§8)");
            bossBar.setProgress(Math.max(0.0D, Math.min(1.0D, (double)remainingMs / 120000.0D)));
            bossBar.addPlayer(player);
            userBars.put(costumeId, bossBar);
            (new BukkitRunnable() {
                long remaining = remainingMs;
                final long total = 120000L;

                public void run() {
                    if (player.isOnline() && this.remaining > 0L && CostumeManager.this.costumeIdEquals(player, costumeId)) {
                        this.remaining -= 1000L;
                        long secs = this.remaining / 1000L % 60L;
                        long mins = this.remaining / 60000L;
                        String var10000 = mins > 0L ? mins + "m " : "";
                        String tStr = var10000 + secs + "s";
                        bossBar.setTitle("§7Kostium " + costumeName + " §7wykorzystany! Odnawianie umiejetnosci... §8(§f" + tStr + "§8)");
                        bossBar.setProgress(Math.max(0.0D, Math.min(1.0D, (double)this.remaining / 120000.0D)));
                    } else {
                        bossBar.removePlayer(player);
                        Map<String, BossBar> currentBars = CostumeManager.this.cooldownBossBars.get(player.getUniqueId());
                        if (currentBars != null) {
                            currentBars.remove(costumeId);
                        }

                        this.cancel();
                    }
                }
            }).runTaskTimer(this.plugin, 20L, 20L);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (this.getActiveCostume(player) != null) {
            // Opóźnione odświeżanie, aby pakiet natywny (np. rzut śnieżką) nie nadpisał naszego!
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                this.updateVisualHiding(player);
            }, 2L);
        }

        if (event.getHand() == EquipmentSlot.HAND) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() != Material.AIR) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    String costumeId = meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "costume_id"), PersistentDataType.STRING);
                    if (costumeId != null) {
                        if (!event.getAction().name().contains("RIGHT_CLICK")) {
                            return;
                        }

                        if (this.getActiveCostume(player) != null) {
                            this.removeCostume(player, true);
                        }

                        NamespacedKey durKey = new NamespacedKey(this.plugin, "costume_duration");
                        long durationMs = meta.getPersistentDataContainer().getOrDefault(durKey, PersistentDataType.LONG, 0L);
                        NamespacedKey activatedKey = new NamespacedKey(this.plugin, "is_activated");
                        byte activated = meta.getPersistentDataContainer().getOrDefault(activatedKey, PersistentDataType.BYTE, (byte)1);
                        String uniqueId = UniqueIdUtils.getUniqueId(item);
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
                                if (durationMs == -1L) {
                                    timeStr = "Na zawsze";
                                } else {
                                    long expiry = System.currentTimeMillis() + durationMs;
                                    timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(expiry));
                                }

                                lore.add("§7Ważność: §e" + timeStr);
                                lore.add("§7ID: §f" + uniqueId);
                                meta.setLore(lore);
                            }

                            item.setItemMeta(meta);
                            player.sendMessage("§aAktywowano kostium!");
                        }

                        if (this.isUnlocked(player, costumeId)) {
                            this.equipCostume(player, costumeId, uniqueId, durationMs);
                            player.sendTitle(" ", "§aPomyślnie założono kostium!", 10, 40, 10);
                        } else {
                            this.unlockCostume(player, costumeId);
                            player.sendTitle(" ", "§aPomyślnie założono kostium!", 10, 40, 10);
                            this.equipCostume(player, costumeId, uniqueId, durationMs);
                        }

                        if (item.getAmount() > 1) {
                            item.setAmount(item.getAmount() - 1);
                        } else {
                            player.getInventory().setItemInMainHand(null);
                        }

                        event.setCancelled(true);
                        return;
                    }
                }

                if (this.getActiveCostume(player) != null) {
                    Costume activeCostume = this.costumeRegistry.getCostume(this.getActiveCostume(player));
                    if (activeCostume != null) {
                        activeCostume.onInteract(player, event);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (this.getActiveCostume(player) != null) {
            // Opóźnione odświeżenie po zjedzeniu (żeby pakiet jedzenia nas nie nadpisał)
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                this.updateVisualHiding(player);
            }, 2L);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            if (this.getActiveCostume(player) != null && (event.getSlotType() == SlotType.ARMOR || event.getClick().isShiftClick())) {
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    this.updateVisualHiding(player);
                    StatsManager.updateStats(player);
                });
            }

        }
    }

    public void giveCostumeItem(Player player, String costumeId, String uniqueId, long durationMs) {
        ItemStack item = this.plugin.getConfigManager().getItem("gui_costumes.items." + costumeId);
        if (item == null) {
            this.plugin.getLogger().warning("Could not find costume item in config: " + costumeId);
        } else {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String displayName = meta.getDisplayName();
                if (displayName != null) {
                    if (uniqueId != null) {
                        displayName = displayName.replaceAll("(?i)\\s*na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "");
                    } else if (durationMs > 0L) {
                        String durStr = TimeConverter.formatMillis(durationMs);
                        if (displayName.contains("%duration%")) {
                            displayName = displayName.replace("%duration%", durStr);
                        } else {
                            displayName = displayName.replaceAll("(?i)na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "na czas " + durStr);
                        }
                    } else if (durationMs == -1L) {
                        if (displayName.contains("%duration%")) {
                            displayName = displayName.replace("%duration%", "Na zawsze");
                        } else {
                            displayName = displayName.replaceAll("(?i)na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "na zawsze");
                        }
                    }

                    meta.setDisplayName(displayName.trim());
                }

                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "costume_id"), PersistentDataType.STRING, costumeId);
                if (uniqueId != null) {
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING, uniqueId);
                }

                if (durationMs != 0L) {
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "costume_duration"), PersistentDataType.LONG, durationMs);
                    boolean isAlreadyActivated = uniqueId != null;
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "is_activated"), PersistentDataType.BYTE, (byte)(isAlreadyActivated ? 1 : 0));
                    List<String> lore = meta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }

                    lore.removeIf(line -> line.contains("§7Ważność:") || line.contains("§7ID:") || line.contains("Wygaśnie:"));
                    if (isAlreadyActivated) {
                        String timeStr;
                        if (durationMs == -1L) {
                            timeStr = "Na zawsze";
                        } else {
                            long expiry = System.currentTimeMillis() + durationMs;
                            timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(expiry));
                        }

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

        }
    }

    private void applyGhostArmor(Player player, String costumeId) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            this.updateVisualHiding(player);
        }, 1L);
    }

    private void removeGhostArmor(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        for (Player other : player.getWorld().getPlayers()) {
            other.sendEquipmentChange(player, EquipmentSlot.HEAD, helmet);
            other.sendEquipmentChange(player, EquipmentSlot.CHEST, chest);
            other.sendEquipmentChange(player, EquipmentSlot.LEGS, legs);
            other.sendEquipmentChange(player, EquipmentSlot.FEET, boots);
        }

    }

    boolean costumeIdEquals(Player player, String id) {
        String costume = this.getActiveCostume(player);
        return costume != null && costume.equals(id);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        boolean destroyable = this.plugin.getConfigManager().getConfig().getBoolean("settings.costume_system.mikolaj.snow_destroyable", false);
        if (!destroyable && this.snowBlocks.containsKey(event.getBlock())) {
            event.setCancelled(true);
        }

    }
}