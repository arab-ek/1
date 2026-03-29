package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PiratCostume implements Costume {
  public String getId() {
    return "pirata";
  }
  
  public double getDefenseMultiplier(Player player, EntityDamageEvent event) {
    return 1.05D;
  }
  
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.13D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\PiratCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */