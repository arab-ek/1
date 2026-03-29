package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PaczusPet implements Pet {
  public String getId() {
    return "paczus";
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 1, true, false));
    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, true, false));
    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 0, true, false));
  }
  
  public void onFoodChange(Player player, FoodLevelChangeEvent event) {
    event.setCancelled(true);
    player.setFoodLevel(20);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\PaczusPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */