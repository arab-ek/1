package dev.arab.KSIEGI;

import dev.arab.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KsiegiInventory {
  private final Main plugin;
  
  private final BookManager bookManager;
  
  public KsiegiInventory(Main plugin, BookManager bookManager) {
    this.plugin = plugin;
    this.bookManager = bookManager;
  }
  
  public void open(Player player) {
    String title = this.bookManager.getConfig().getString("gui.title", "&8Zaklęte Książki").replace("&", "§");
    int size = this.bookManager.getConfig().getInt("gui.size", 27);
    Inventory inv = Bukkit.createInventory(null, size, title);
    int slot = 0;
    if (this.bookManager.getConfig().getConfigurationSection("books") != null)
      for (String key : this.bookManager.getConfig().getConfigurationSection("books").getKeys(false)) {
        ItemStack book = this.bookManager.createBook(key);
        if (book != null) {
          inv.setItem(slot++, book);
          if (slot == 16)
            slot = 20; 
        } 
      }  
    player.openInventory(inv);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\KsiegiInventory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */