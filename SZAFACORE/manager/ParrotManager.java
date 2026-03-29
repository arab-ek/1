/* Decompiler 346ms, total 743ms, lines 561 */
package dev.arab.SZAFACORE.manager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import dev.arab.Main;
import dev.arab.SZAFACORE.data.ParrotData;
import dev.arab.SZAFACORE.util.TimeConverter;
import dev.arab.SZAFACORE.util.UniqueIdUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Parrot.Variant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ParrotManager implements Listener {
    private final Main plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;
    final Map<UUID, ParrotData> activeParrots = new HashMap();
    boolean spawnEntity;
    private ConfigurationSection effectSection;

    public ParrotManager(Main plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "parrot_data.yml");
        this.loadData();
        this.startEffectTask();
    }

    private void loadData() {
        if (!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
            } catch (IOException var12) {
                var12.printStackTrace();
            }
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
        this.spawnEntity = this.plugin.getConfigManager().getConfig().getBoolean("settings.parrot_system.spawn_entity", false);
        this.effectSection = this.plugin.getConfigManager().getConfig().getConfigurationSection("settings.parrot_system.effects");
        ConfigurationSection section = this.dataConfig.getConfigurationSection("active_parrots");
        if (section != null) {
            Iterator var2 = section.getKeys(false).iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                UUID uuid = UUID.fromString(key);
                String parrotId = this.dataConfig.getString("active_parrots." + key + ".id");
                String uniqueId = this.dataConfig.getString("active_parrots." + key + ".uniqueId");
                long expiry = this.dataConfig.getLong("active_parrots." + key + ".expiry");
                String collector = this.dataConfig.getString("active_parrots." + key + ".collector");
                int rerolls = this.dataConfig.getInt("active_parrots." + key + ".rerolls");
                List<String> effects = this.dataConfig.getStringList("active_parrots." + key + ".effects");
                this.activeParrots.put(uuid, new ParrotData(parrotId, uniqueId, expiry, collector, rerolls, effects));
            }
        }

    }

    public void saveData() {
        this.dataConfig.set("active_parrots", (Object)null);
        Iterator var1 = this.activeParrots.entrySet().iterator();

        while(var1.hasNext()) {
            Entry<UUID, ParrotData> entry = (Entry)var1.next();
            String key = ((UUID)entry.getKey()).toString();
            ParrotData data = (ParrotData)entry.getValue();
            this.dataConfig.set("active_parrots." + key + ".id", data.getParrotId());
            this.dataConfig.set("active_parrots." + key + ".uniqueId", data.getUniqueId());
            this.dataConfig.set("active_parrots." + key + ".expiry", data.getExpiryTimestamp());
            this.dataConfig.set("active_parrots." + key + ".collector", data.getCollector());
            this.dataConfig.set("active_parrots." + key + ".rerolls", data.getRerolls());
            this.dataConfig.set("active_parrots." + key + ".effects", data.getEffects());
        }

        try {
            this.dataConfig.save(this.dataFile);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    private void startEffectTask() {
        (new BukkitRunnable() {
            int cleanupCounter = 0;

            public void run() {
                Iterator var1 = ParrotManager.this.activeParrots.entrySet().iterator();

                while(true) {
                    while(true) {
                        Entry entry;
                        Player player;
                        do {
                            do {
                                if (!var1.hasNext()) {
                                    if (++this.cleanupCounter >= 300) {
                                        this.cleanupCounter = 0;
                                        var1 = Bukkit.getWorlds().iterator();

                                        label46:
                                        while(var1.hasNext()) {
                                            World world = (World)var1.next();
                                            Iterator var6 = world.getEntitiesByClass(Parrot.class).iterator();

                                            while(true) {
                                                Parrot p;
                                                do {
                                                    if (!var6.hasNext()) {
                                                        continue label46;
                                                    }

                                                    p = (Parrot)var6.next();
                                                } while(!p.isTamed() && p.getOwner() == null);

                                                p.remove();
                                            }
                                        }
                                    }

                                    return;
                                }

                                entry = (Entry)var1.next();
                                player = Bukkit.getPlayer((UUID)entry.getKey());
                            } while(player == null);
                        } while(!player.isOnline());

                        ParrotData data = (ParrotData)entry.getValue();
                        if (data.isExpired()) {
                            ParrotManager.this.removeParrot(player, false);
                            player.sendMessage("§cTwoja papuszka wygasła!");
                        } else {
                            if (ParrotManager.this.spawnEntity && player.getShoulderEntityLeft() == null && ParrotManager.this.isStable(player)) {
                                ParrotManager.this.updateShoulder(player, data);
                            } else if (!ParrotManager.this.spawnEntity && player.getShoulderEntityLeft() != null) {
                                player.setShoulderEntityLeft((Entity)null);
                            }

                            ParrotManager.this.applyEffects(player, data.getEffects());
                        }
                    }
                }
            }
        }).runTaskTimer(this.plugin, 20L, 20L);
    }

    private List<String> generateRandomEffects() {
        List<String> all = new ArrayList(this.plugin.getConfigManager().getConfig().getConfigurationSection("settings.parrot_system.effects").getKeys(false));
        List<String> res = new ArrayList();
        int count = ThreadLocalRandom.current().nextInt(1, 3);
        Collections.shuffle(all);

        for(int i = 0; i < Math.min(count, all.size()); ++i) {
            res.add((String)all.get(i));
        }

        return res;
    }

    public void setParrot(Player player, ParrotData data) {
        this.activeParrots.put(player.getUniqueId(), data);
        this.updateShoulder(player, data);
        StatsManager.updateStats(player);
        this.saveData();
    }

    public void removeParrot(Player player) {
        this.removeParrot(player, true);
    }

    public void removeParrot(Player player, boolean giveItem) {
        ParrotData data = (ParrotData)this.activeParrots.remove(player.getUniqueId());
        if (data != null && giveItem) {
            String duration = data.getExpiryTimestamp() == -1L ? null : TimeConverter.formatMillis(data.getExpiryTimestamp() - System.currentTimeMillis());
            ItemStack item = this.createParrotItem(data.getParrotId(), data.getUniqueId(), duration, data.getCollector(), data.getRerolls(), data.getEffects());
            if (player.getInventory().addItem(new ItemStack[]{item}).size() > 0) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        player.setShoulderEntityLeft((Entity)null);
        StatsManager.updateStats(player);
        this.saveData();
    }

    void updateShoulder(Player player, ParrotData data) {
        boolean spawn = this.plugin.getConfigManager().getConfig().getBoolean("settings.parrot_system.spawn_entity", false);
        if (!spawn) {
            if (player.getShoulderEntityLeft() != null) {
                player.setShoulderEntityLeft((Entity)null);
            }

        } else {
            String variantStr = this.plugin.getConfigManager().getConfig().getString("gui_parrots.items." + data.getParrotId() + ".parrot_variant", "RED");
            Variant variant = Variant.valueOf(variantStr);
            Parrot parrot = (Parrot)player.getWorld().spawnEntity(player.getLocation(), EntityType.PARROT);
            parrot.setVariant(variant);
            parrot.setTamed(true);
            parrot.setOwner(player);
            parrot.setSilent(true);
            player.setShoulderEntityLeft(parrot);
            player.setShoulderEntityRight((Entity)null);
            parrot.remove();
        }
    }

    boolean isStable(Player player) {
        return player.isOnGround() && !player.isInWater() && !player.isInLava() && !player.isFlying();
    }

    void applyEffects(Player player, List<String> effectIds) {
        if (this.effectSection != null) {
            Iterator var3 = effectIds.iterator();

            while(var3.hasNext()) {
                String id = (String)var3.next();
                ConfigurationSection s = this.effectSection.getConfigurationSection(id);
                if (s != null) {
                    String type = s.getString("type");
                    int amp = s.getInt("amplifier", 0);
                    if (type != null) {
                        if (type.equalsIgnoreCase("NO_HUNGER")) {
                            player.setFoodLevel(20);
                            player.setSaturation(5.0F);
                        } else if (!type.equalsIgnoreCase("FALL_DAMAGE_RESISTANCE") && !type.equalsIgnoreCase("HEALTH_BOOST")) {
                            PotionEffectType pet = PotionEffectType.getByName(type);
                            if (pet != null) {
                                player.addPotionEffect(new PotionEffect(pet, 60, amp, false, false, true));
                            }
                        }
                    }
                }
            }

        }
    }

    public double getHealthBonus(Player player) {
        ParrotData data = (ParrotData)this.activeParrots.get(player.getUniqueId());
        if (data == null) {
            return 0.0D;
        } else {
            double extra = 0.0D;
            if (this.hasEffect(data, "HEALTH_BOOST")) {
                extra = 2.0D;
            }

            return extra;
        }
    }

    public double getAttackBonus(Player player) {
        return 0.0D;
    }

    private boolean hasEffect(ParrotData data, String targetType) {
        ConfigurationSection sec = this.plugin.getConfigManager().getConfig().getConfigurationSection("settings.parrot_system.effects");
        if (sec == null) {
            return false;
        } else {
            Iterator var4 = data.getEffects().iterator();

            String type;
            do {
                if (!var4.hasNext()) {
                    return false;
                }

                String id = (String)var4.next();
                type = sec.getString(id + ".type");
            } while(type == null || !type.equalsIgnoreCase(targetType));

            return true;
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final ParrotData data = (ParrotData)this.activeParrots.get(event.getPlayer().getUniqueId());
        if (data != null && !data.isExpired()) {
            (new BukkitRunnable() {
                public void run() {
                    ParrotManager.this.updateShoulder(event.getPlayer(), data);
                }
            }).runTaskLater(this.plugin, 20L);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        final ParrotData data = (ParrotData)this.activeParrots.get(event.getPlayer().getUniqueId());
        if (data != null && !data.isExpired()) {
            (new BukkitRunnable() {
                public void run() {
                    ParrotManager.this.updateShoulder(event.getPlayer(), data);
                }
            }).runTaskLater(this.plugin, 10L);
        }

    }

    @EventHandler
    public void onParrotSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.PARROT) {
        }

    }

    public boolean reroll(Player player) {
        ParrotData data = (ParrotData)this.activeParrots.get(player.getUniqueId());
        double baseCost = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_cost", 1000.0D);
        double multiplier = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_multiplier", 2.0D);
        if (baseCost > 0.0D) {
            double finalCost = baseCost * Math.pow(multiplier, (double)data.getRerolls());
            if (Main.getEconomy() == null) {
                player.sendMessage("§cSystem ekonomii jest niedostępny!");
                return false;
            }

            if (!Main.getEconomy().has(player, finalCost)) {
                player.sendMessage("§cNie masz wystarczająco pieniędzy! (§f" + finalCost + "§c)");
                return false;
            }

            Main.getEconomy().withdrawPlayer(player, finalCost);
        }

        data.setEffects(this.generateRandomEffects());
        data.incrementRerolls();
        this.saveData();
        player.sendMessage("§aPrzelosowano efekty papuszki!");
        return true;
    }

    public ParrotData getActiveParrot(Player player) {
        return (ParrotData)this.activeParrots.get(player.getUniqueId());
    }

    public void giveParrotItem(Player player, String parrotId, String duration) {
        ItemStack item = this.createParrotItem(parrotId, (String)null, duration, player.getName(), 0, this.generateRandomEffects());
        if (player.getInventory().addItem(new ItemStack[]{item}).size() > 0) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }

    }

    public ItemStack createParrotItem(String parrotId, String uniqueId, String duration, String collector, int rerolls, List<String> effects) {
        ItemStack item = this.plugin.getConfigManager().getItem("gui_parrots.items." + parrotId);
        if (item == null) {
            return null;
        } else {
            long durationMs = duration != null ? TimeConverter.parseTimeToMillis(duration) : -1L;
            long expiry = durationMs != -1L ? System.currentTimeMillis() + durationMs : -1L;
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String displayName = meta.getDisplayName();
                if (displayName != null) {
                    if (uniqueId != null) {
                        displayName = displayName.replaceAll("(?i)\\s*na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "");
                    } else if (duration != null && !duration.isEmpty()) {
                        if (displayName.contains("%duration%")) {
                            displayName = displayName.replace("%duration%", duration);
                        } else {
                            displayName = displayName.replaceAll("(?i)na czas\\s*[\\d\\.\\s%\\w]+(dni|d|h|m|s)?", "na czas " + duration);
                        }
                    }

                    meta.setDisplayName(displayName.trim());
                }

                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "parrot_id"), PersistentDataType.STRING, parrotId);
                if (uniqueId != null) {
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING, uniqueId);
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "is_activated"), PersistentDataType.BYTE, (byte)1);
                } else {
                    meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "is_activated"), PersistentDataType.BYTE, (byte)0);
                }

                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "parrot_expiry"), PersistentDataType.LONG, expiry);
                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "parrot_collector"), PersistentDataType.STRING, collector);
                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "parrot_rerolls"), PersistentDataType.INTEGER, rerolls);
                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "parrot_effects"), PersistentDataType.STRING, String.join(";", effects));
                List<String> lore = meta.getLore();
                if (lore != null) {
                    List<String> newLore = new ArrayList();
                    Iterator var16 = lore.iterator();

                    while(true) {
                        while(var16.hasNext()) {
                            String line = (String)var16.next();
                            if (line.contains("%effects%")) {
                                Iterator var25 = effects.iterator();

                                while(var25.hasNext()) {
                                    String effId = (String)var25.next();
                                    String effName = this.plugin.getConfigManager().getConfig().getString("settings.parrot_system.effects." + effId + ".name", effId);
                                    newLore.add("  §8» " + effName.replace("&", "§"));
                                }
                            } else if (!line.contains("%expiry%")) {
                                double bCost = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_cost", 1000.0D);
                                double mult = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_multiplier", 2.0D);
                                double currentCost = bCost * Math.pow(mult, (double)rerolls);
                                newLore.add(line.replace("%collector%", collector).replace("%rerolls%", String.valueOf(rerolls)).replace("%cost%", String.valueOf((int)currentCost)).replace("&", "§"));
                            }
                        }

                        newLore.removeIf((linex) -> {
                            return linex.contains("§7Ważność:") || linex.contains("§7ID:") || linex.contains("Wygaśnie:");
                        });
                        if ((Byte)meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(this.plugin, "is_activated"), PersistentDataType.BYTE, (byte)1) == 0) {
                            if (expiry == -1L) {
                                newLore.add("§7Ważność: §eNa zawsze");
                            } else {
                                newLore.add("§7Ważność: §c-");
                            }
                        } else {
                            String dateStr2 = expiry == -1L ? "Na zawsze" : (new SimpleDateFormat("dd.MM.yyyy")).format(new Date(expiry));
                            newLore.add("§7Ważność: §e" + dateStr2);
                            if (uniqueId != null) {
                                newLore.add("§7ID: §f" + uniqueId);
                            }
                        }

                        meta.setLore(newLore);
                        break;
                    }
                }

                item.setItemMeta(meta);
            }

            return item;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() != Material.AIR) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    String parrotId = (String)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "parrot_id"), PersistentDataType.STRING);
                    if (parrotId != null) {
                        event.setCancelled(true);
                        Player player = event.getPlayer();
                        NamespacedKey activatedKey = new NamespacedKey(this.plugin, "is_activated");
                        byte activated = (Byte)meta.getPersistentDataContainer().getOrDefault(activatedKey, PersistentDataType.BYTE, (byte)1);
                        long expiry = (Long)meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(this.plugin, "parrot_expiry"), PersistentDataType.LONG, -1L);
                        String uniqueId = (String)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING);
                        if (activated == 0) {
                            meta.getPersistentDataContainer().set(activatedKey, PersistentDataType.BYTE, (byte)1);
                            if (uniqueId == null) {
                                uniqueId = UniqueIdUtils.generateCustomId();
                                meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "unique_id"), PersistentDataType.STRING, uniqueId);
                            }

                            List<String> lore = meta.getLore();
                            if (lore != null) {
                                lore.removeIf((line) -> {
                                    return line.contains("§7Ważność:") || line.contains("§7ID:") || line.contains("Wygaśnie:");
                                });
                                String dateStr = expiry == -1L ? "Na zawsze" : (new SimpleDateFormat("dd.MM.yyyy")).format(new Date(expiry));
                                lore.add("§7Ważność: §e" + dateStr);
                                lore.add("§7ID: §f" + uniqueId);
                                meta.setLore(lore);
                            }

                            item.setItemMeta(meta);
                            player.sendMessage("§aAktywowano papuszkę!");
                        }

                        if (this.activeParrots.containsKey(player.getUniqueId())) {
                            player.sendTitle(" ", "§eMasz już założoną papugę!", 10, 40, 10);
                        } else {
                            String collector = (String)meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(this.plugin, "parrot_collector"), PersistentDataType.STRING, player.getName());
                            int rerolls = (Integer)meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(this.plugin, "parrot_rerolls"), PersistentDataType.INTEGER, 0);
                            String effectsStr = (String)meta.getPersistentDataContainer().get(new NamespacedKey(this.plugin, "parrot_effects"), PersistentDataType.STRING);
                            List<String> effects = effectsStr != null ? Arrays.asList(effectsStr.split(";")) : new ArrayList();
                            if (expiry != -1L && System.currentTimeMillis() > expiry) {
                                player.sendMessage("§cTa papuszka wygasła!");
                                player.getInventory().setItemInMainHand((ItemStack)null);
                            } else {
                                ParrotData data = new ParrotData(parrotId, uniqueId, expiry, collector, rerolls, (List)effects);
                                this.setParrot(player, data);
                                player.getInventory().setItemInMainHand((ItemStack)null);
                                player.sendTitle(" ", "§aPomyślnie założono papuszkę!", 10, 40, 10);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Parrot) {
            Parrot parrot = (Parrot)event.getEntity();
            if (parrot.isTamed() && parrot.getOwner() instanceof Player) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            ParrotData data = (ParrotData)this.activeParrots.get(player.getUniqueId());
            if (data != null && !data.isExpired()) {
                if (event.getCause() == DamageCause.FALL && this.hasEffect(data, "FALL_DAMAGE_RESISTANCE")) {
                    event.setDamage(event.getDamage() * 0.5D);
                }

            }
        }
    }
}