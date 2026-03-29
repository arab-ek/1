package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CukiereczekPet implements Pet {
  public String getId() {
    return "cukiereczek";
  }
  
  public double getAttackBonus(Player player) {
    return 2.0D;
  }
  
  public void onTick(Player player) {
    if (!player.hasPotionEffect(PotionEffectType.SPEED) || player.getPotionEffect(PotionEffectType.SPEED).getDuration() < 60)
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, false)); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\CukiereczekPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */