package dev.arab.SZAFACORE.util;

import java.util.Random;
import dev.arab.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class UniqueIdUtils {
  private static final NamespacedKey UNIQUE_ID_KEY = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "unique_id");
  
  private static final NamespacedKey ACTIVATED_KEY = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "is_activated");
  
  public static String generateCustomId() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder();
    int i;
    for (i = 0; i < 5; i++)
      sb.append(chars.charAt(rnd.nextInt(chars.length()))); 
    sb.append("-");
    for (i = 0; i < 4; i++)
      sb.append(chars.charAt(rnd.nextInt(chars.length()))); 
    return sb.toString();
  }
  
  public static void setUniqueId(ItemStack item) {
    setUniqueId(item, generateCustomId());
  }
  
  public static void setUniqueId(ItemStack item, String id) {
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
      meta.getPersistentDataContainer().set(UNIQUE_ID_KEY, PersistentDataType.STRING, id);
      item.setItemMeta(meta);
    } 
  }
  
  public static String getUniqueId(ItemStack item) {
    return (item == null || !item.hasItemMeta()) ? null : (String)item.getItemMeta().getPersistentDataContainer().get(UNIQUE_ID_KEY, PersistentDataType.STRING);
  }
  
  public static void setActivated(ItemStack item, boolean activated) {
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
      meta.getPersistentDataContainer().set(ACTIVATED_KEY, PersistentDataType.BYTE, Byte.valueOf((byte)(activated ? 1 : 0)));
      item.setItemMeta(meta);
    } 
  }
  
  public static boolean isActivated(ItemStack item) {
    if (item == null || !item.hasItemMeta())
      return false; 
    Byte val = (Byte)item.getItemMeta().getPersistentDataContainer().get(ACTIVATED_KEY, PersistentDataType.BYTE);
    return (val != null && val.byteValue() == 1);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACOR\\util\UniqueIdUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */