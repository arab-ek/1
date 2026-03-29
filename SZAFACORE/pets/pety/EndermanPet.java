package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;

public class EndermanPet implements Pet {
  public String getId() {
    return "enderman";
  }
  
  public double getAttackBonus(Player player) {
    return 3.0D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\EndermanPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */