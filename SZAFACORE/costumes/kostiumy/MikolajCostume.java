package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class MikolajCostume implements Costume {
  public String getId() {
    return "mikolaja";
  }
  
  public double getHealthBonus(Player player) {
    return 6.0D;
  }
  
  public double getDefenseMultiplier(Player player, EntityDamageEvent event) {
    return 0.95D;
  }
  
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.08D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\MikolajCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */