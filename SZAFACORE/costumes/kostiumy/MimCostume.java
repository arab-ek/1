package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MimCostume implements Costume {
  public String getId() {
    return "mima";
  }
  
  public double getHealthBonus(Player player) {
    return 4.0D;
  }
  
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.11D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\MimCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */