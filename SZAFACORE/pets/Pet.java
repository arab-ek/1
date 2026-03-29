package dev.arab.SZAFACORE.pets;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public interface Pet {
  String getId();
  
  default double getHealthBonus(Player player) {
    return 0.0D;
  }
  
  default double getAttackBonus(Player player) {
    return 0.0D;
  }
  
  default void onTick(Player player) {}
  
  default void onEquip(Player player) {}
  
  default void onUnequip(Player player) {}
  
  default void onDamage(Player player, EntityDamageEvent event) {}
  
  default void onFoodChange(Player player, FoodLevelChangeEvent event) {}
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\Pet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */