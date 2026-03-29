package dev.arab.KSIEGI.ksiazki;

import dev.arab.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CritBook extends Book {
  public CritBook(Main plugin) {
    super(plugin, "krytyczne", "Obrażenia krytyczne I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("SWORD");
  }
  
  public void onDamageDamager(EntityDamageByEntityEvent event, Player damager, ItemStack item) {
    event.setDamage(event.getDamage() * 1.2D);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\CritBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */