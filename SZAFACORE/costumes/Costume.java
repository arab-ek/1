package dev.arab.SZAFACORE.costumes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public interface Costume {
  String getId();
  
  default double getHealthBonus(Player player) {
    return 0.0D;
  }
  
  default double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.0D;
  }
  
  default double getDefenseMultiplier(Player player, EntityDamageEvent event) {
    return 1.0D;
  }
  
  default void onEquip(Player player) {}
  
  default void onUnequip(Player player) {}
  
  default void onTick(Player player) {}
  
  default boolean onDamage(Player player, EntityDamageEvent event) {
    return false;
  }
  
  default void onAttack(Player player, EntityDamageByEntityEvent event) {}
  
  default void onKill(Player killer, Player victim) {}
  
  default void onJump(Player player, PlayerToggleFlightEvent event) {}
  
  default void onSneak(Player player, PlayerToggleSneakEvent event) {}
  
  default void onInteract(Player player, PlayerInteractEvent event) {}
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\Costume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */