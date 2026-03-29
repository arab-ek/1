package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class TelekrolikPet implements Pet {
  public String getId() {
    return "telekrolik";
  }
  
  public double getAttackBonus(Player player) {
    return 3.0D;
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    event.setDamage(event.getDamage() * 0.9D);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\TelekrolikPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */