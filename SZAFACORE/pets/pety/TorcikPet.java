package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TorcikPet implements Pet {
  public String getId() {
    return "torcik";
  }
  
  public double getHealthBonus(Player player) {
    return 2.0D;
  }
  
  public double getAttackBonus(Player player) {
    return 2.0D;
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, false));
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\TorcikPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */