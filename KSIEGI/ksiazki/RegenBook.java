package dev.arab.KSIEGI.ksiazki;

import java.util.Random;
import dev.arab.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegenBook extends Book {
  private final Random random = new Random();
  
  public RegenBook(Main plugin) {
    super(plugin, "regeneracja", "Regeneracji I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("CHESTPLATE");
  }
  
  public void onDamageVictim(EntityDamageByEntityEvent event, Player victim, ItemStack item) {
    if (this.random.nextInt(100) < 50)
      victim.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1)); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\RegenBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */