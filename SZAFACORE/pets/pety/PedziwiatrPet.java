package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PedziwiatrPet implements Pet {
  public String getId() {
    return "pedziwiatr";
  }
  
  public void onTick(Player player) {
    if (!player.hasPotionEffect(PotionEffectType.SPEED) || player.getPotionEffect(PotionEffectType.SPEED).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, false)); 
    if (!player.hasPotionEffect(PotionEffectType.JUMP) || player.getPotionEffect(PotionEffectType.JUMP).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1, true, false)); 
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    event.setDamage(event.getDamage() * 1.1D);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\PedziwiatrPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */