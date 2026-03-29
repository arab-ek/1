package dev.arab.KSIEGI.ksiazki;

import java.util.Random;
import dev.arab.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonBook extends Book {
  private final Random random = new Random();
  
  public PoisonBook(Main plugin) {
    super(plugin, "otrucie", "Otrucie przeciwnika I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("SWORD");
  }
  
  public void onDamageDamager(EntityDamageByEntityEvent event, Player damager, ItemStack item) {
    Entity entity = event.getEntity();
    if (entity instanceof LivingEntity) {
      LivingEntity victim = (LivingEntity)entity;
      if (this.random.nextInt(100) < 20)
        victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0)); 
    } 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\PoisonBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */