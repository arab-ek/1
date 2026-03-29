package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZajaczekPet implements Pet {
  public String getId() {
    return "zajaczek";
  }
  
  public double getAttackBonus(Player player) {
    return 4.0D;
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1, true, false));
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, true, false));
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
      event.setDamage(event.getDamage() * 0.5D); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\ZajaczekPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */