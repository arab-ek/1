package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;

public class CyberObroncaPet implements Pet {
  public String getId() {
    return "cyber_obronca";
  }
  
  public double getAttackBonus(Player player) {
    return 2.0D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\CyberObroncaPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */