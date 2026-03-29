package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class WalentynkowyCostume implements Costume {
  public String getId() {
    return "walentynkowy";
  }
  
  public double getHealthBonus(Player player) {
    return 5.0D;
  }
  
  public boolean onDamage(Player player, EntityDamageEvent event) {
    return (event.getCause() == EntityDamageEvent.DamageCause.FALL);
  }
  
  public double getDefenseMultiplier(Player player, EntityDamageEvent event) {
    return 0.95D;
  }
  
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.12D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\WalentynkowyCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */