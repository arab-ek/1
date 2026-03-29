package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GrinchCostume implements Costume {
  public String getId() {
    return "grincha";
  }
  
  public double getHealthBonus(Player player) {
    return 3.0D;
  }
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.1D;
  }
}