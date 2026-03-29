package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class GolemikPet implements Pet {
  public String getId() {
    return "golemik";
  }
  
  public double getHealthBonus(Player player) {
    return 1.0D;
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    event.setDamage(event.getDamage() * 0.9D);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\GolemikPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */