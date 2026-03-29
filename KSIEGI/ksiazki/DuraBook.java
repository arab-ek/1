package dev.arab.KSIEGI.ksiazki;

import java.util.Random;
import dev.arab.Main;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DuraBook extends Book {
  private final Random random = new Random();
  
  public DuraBook(Main plugin) {
    super(plugin, "wytrzymalosc", "Wytrzymałości I");
  }
  
  public boolean canApply(ItemStack item) {
    String type = item.getType().toString();
    return (type.contains("HELMET") || type.contains("CHESTPLATE") || type.contains("LEGGINGS") || type.contains("BOOTS") || type.contains("SWORD"));
  }
  
  public void onItemDamage(PlayerItemDamageEvent event, ItemStack item) {
    if (this.random.nextInt(100) < 50)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\DuraBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */