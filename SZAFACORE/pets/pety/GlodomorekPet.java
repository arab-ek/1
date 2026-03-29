package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class GlodomorekPet implements Pet {
  public String getId() {
    return "glodomorek";
  }
  
  public double getHealthBonus(Player player) {
    return 1.0D;
  }
  
  public void onFoodChange(Player player, FoodLevelChangeEvent event) {
    event.setCancelled(true);
    player.setFoodLevel(20);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\GlodomorekPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */