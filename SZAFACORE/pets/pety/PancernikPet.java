package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PancernikPet implements Pet {
  public String getId() {
    return "pancernik";
  }
  
  public void onTick(Player player) {
    if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) || player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 1, true, false)); 
    if (!player.hasPotionEffect(PotionEffectType.SLOW) || player.getPotionEffect(PotionEffectType.SLOW).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, true, false)); 
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
      event.setDamage(event.getDamage() * 0.5D); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\PancernikPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */