package dev.arab.KSIEGI.ksiazki;

import java.util.ArrayList;
import java.util.List;
import dev.arab.EVENTOWKICORE.utils.ChatUtils;
import dev.arab.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public abstract class Book {
  protected final Main plugin;
  
  private final String id;
  
  private final String displayName;
  
  private final NamespacedKey key;
  
  public Book(Main plugin, String id, String displayName) {
    this.plugin = plugin;
    this.id = id;
    this.displayName = displayName;
    this.key = new NamespacedKey((Plugin)plugin, "ksiega_" + id);
  }
  
  public abstract boolean canApply(ItemStack paramItemStack);
  
  public void apply(ItemStack item) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return; 
    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
    lore.add("§e" + this.displayName);
    meta.setLore(lore);
    meta.getPersistentDataContainer().set(this.key, PersistentDataType.BYTE, Byte.valueOf((byte)1));
    meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "has_custom_book"), PersistentDataType.BYTE, Byte.valueOf((byte)1));
    item.setItemMeta(meta);
  }
  
  public boolean hasBook(ItemStack item) {
    if (item == null || item.getType() == Material.AIR)
      return false; 
    ItemMeta meta = item.getItemMeta();
    return hasBook(meta);
  }
  
  public boolean hasBook(ItemMeta meta) {
    if (meta == null)
      return false; 
    PersistentDataContainer pdc = meta.getPersistentDataContainer();
    if (pdc.has(this.key, PersistentDataType.BYTE))
      return true; 
    if (meta.hasLore())
      for (String line : meta.getLore()) {
        if (ChatUtils.stripColor(line).contains(this.displayName)) {
          pdc.set(this.key, PersistentDataType.BYTE, Byte.valueOf((byte)1));
          pdc.set(new NamespacedKey((Plugin)this.plugin, "has_custom_book"), PersistentDataType.BYTE, Byte.valueOf((byte)1));
          return true;
        } 
      }  
    return false;
  }
  
  public void onDamageVictim(EntityDamageByEntityEvent event, Player victim, ItemStack item) {}
  
  public void onDamageDamager(EntityDamageByEntityEvent event, Player damager, ItemStack item) {}
  
  public void onItemDamage(PlayerItemDamageEvent event, ItemStack item) {}
  
  public void onTick(Player player, ItemStack item) {}
  
  public String getId() {
    return this.id;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\Book.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */