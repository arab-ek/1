package dev.arab.SZAFACORE.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import dev.arab.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ConfigManager {
  private final Main plugin;
  
  private FileConfiguration config;
  
  private final Map<String, ItemStack> itemCache = new HashMap<>();
  
  public ConfigManager(Main plugin) {
    this.plugin = plugin;
    loadSzafaConfig();
  }
  
  public void loadSzafaConfig() {
    File configFile = new File(this.plugin.getDataFolder(), "config-szafa.yml");
    if (!configFile.exists())
      this.plugin.saveResource("config-szafa.yml", false); 
    this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(configFile);
    this.itemCache.clear();
  }
  
  public FileConfiguration getConfig() {
    return this.config;
  }
  
  public String getGuiTitle() {
    return this.config.getString("gui.title", "&8Szafka");
  }
  
  public int getGuiRows() {
    return this.config.getInt("gui.rows", 6);
  }
  
  public ItemStack getItem(String path) {
    if (this.itemCache.containsKey(path))
      return ((ItemStack)this.itemCache.get(path)).clone(); 
    ConfigurationSection section = this.config.getConfigurationSection(path);
    if (section == null)
      return null; 
    String materialName = section.getString("material");
    if (materialName == null)
      return null; 
    Material material = Material.getMaterial(materialName);
    if (material == null)
      return new ItemStack(Material.STONE); 
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
      String name = section.getString("name");
      if (name != null)
        meta.setDisplayName(name.replace("&", "§")); 
      List<String> lore = section.getStringList("lore");
      if (lore != null)
        meta.setLore((List)lore.stream().map(line -> line.replace("&", "§")).collect(Collectors.toList())); 
      if (section.contains("custom-model-data"))
        meta.setCustomModelData(Integer.valueOf(section.getInt("custom-model-data"))); 
      if (material == Material.PLAYER_HEAD && meta instanceof SkullMeta) {
        SkullMeta skullMeta = (SkullMeta)meta;
        String owner = section.getString("owner");
        String texturePath = section.getString("texture");
        String valuePath = section.getString("value");
        String finalTexture = null;
        if (valuePath != null && !valuePath.isEmpty()) {
          finalTexture = valuePath;
        } else if (texturePath != null && !texturePath.isEmpty()) {
          if (texturePath.startsWith("http")) {
            String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + texturePath + "\"}}}";
            finalTexture = Base64.getEncoder().encodeToString(json.getBytes());
          } else {
            finalTexture = texturePath;
          } 
        } 
        if (finalTexture != null) {
          try {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", finalTexture));
            skullMeta.setPlayerProfile(profile);
          } catch (Throwable t) {
            if (owner != null && !owner.isEmpty())
              skullMeta.setOwner(owner); 
          } 
        } else if (owner != null && !owner.isEmpty()) {
          skullMeta.setOwner(owner);
        } 
        item.setItemMeta((ItemMeta)skullMeta);
      } else if (meta instanceof LeatherArmorMeta && section.contains("color")) {
        LeatherArmorMeta leatherMeta = (LeatherArmorMeta)meta;
        String colorStr = section.getString("color");
        if (colorStr != null && colorStr.startsWith("#"))
          try {
            int r = Integer.valueOf(colorStr.substring(1, 3), 16).intValue();
            int g = Integer.valueOf(colorStr.substring(3, 5), 16).intValue();
            int b = Integer.valueOf(colorStr.substring(5, 7), 16).intValue();
            leatherMeta.setColor(Color.fromRGB(r, g, b));
          } catch (Exception exception) {} 
        leatherMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE });
        item.setItemMeta((ItemMeta)leatherMeta);
      } else {
        item.setItemMeta(meta);
      } 
    } 
    this.itemCache.put(path, item.clone());
    return item;
  }
  
  public int getSlot(String path) {
    return this.config.getInt(path + ".slot", -1);
  }
  
  public List<Integer> getSlots(String path) {
    return this.config.getIntegerList(path + ".slots");
  }
  
  public String getMessage(String path) {
    return this.config.getString("messages." + path, "");
  }
  
  public double getDouble(String path) {
    return this.config.getDouble(path, 0.0D);
  }
  
  public String getColoredString(String path) {
    return this.config.getString(path, "").replace("&", "§");
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\manager\ConfigManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */