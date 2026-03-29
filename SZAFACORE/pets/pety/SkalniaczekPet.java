package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkalniaczekPet implements Pet {
  public String getId() {
    return "skalniaczek";
  }
  
  public double getHealthBonus(Player player) {
    return 2.0D;
  }
  
  public void onTick(Player player) {
    if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) || player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 1, true, false)); 
    if (!player.hasPotionEffect(PotionEffectType.SLOW) || player.getPotionEffect(PotionEffectType.SLOW).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 1, true, false)); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\SkalniaczekPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */