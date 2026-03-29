package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;

public class PudzianekPet implements Pet {
  public String getId() {
    return "pudzianek";
  }
  
  public double getHealthBonus(Player player) {
    return 2.0D;
  }
  
  public double getAttackBonus(Player player) {
    return 2.0D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\PudzianekPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */