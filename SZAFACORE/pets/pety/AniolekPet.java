package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AniolekPet implements Pet {
  public String getId() {
    return "aniolek";
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, true, false));
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    event.setDamage(event.getDamage() * 0.9D);
  }
  
  public void onFoodChange(Player player, FoodLevelChangeEvent event) {
    event.setCancelled(true);
    player.setFoodLevel(20);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\AniolekPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */