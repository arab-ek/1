package dev.arab.KSIEGI.ksiazki;

import java.util.Random;
import dev.arab.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class VampBook extends Book {
  private final Random random = new Random();
  
  public VampBook(Main plugin) {
    super(plugin, "wampiryzm", "Wampiryzm I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("SWORD");
  }
  
  public void onDamageDamager(EntityDamageByEntityEvent event, Player damager, ItemStack item) {
    if (this.random.nextInt(100) < 15) {
      double heal = event.getFinalDamage() * 0.2D;
      damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + heal));
    } 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\VampBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */