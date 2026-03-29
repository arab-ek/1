package dev.arab.KSIEGI.ksiazki;

import java.util.Random;
import dev.arab.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class UnikBook extends Book {
  private final Random random = new Random();
  
  public UnikBook(Main plugin) {
    super(plugin, "unik", "Unik I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("LEGGINGS");
  }
  
  public void onDamageVictim(EntityDamageByEntityEvent event, Player victim, ItemStack item) {
    if (this.random.nextInt(100) < 10) {
      event.setCancelled(true);
      victim.sendMessage("§aUnik!");
    } 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\UnikBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */