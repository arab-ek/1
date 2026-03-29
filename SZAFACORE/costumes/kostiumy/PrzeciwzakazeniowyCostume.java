package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class PrzeciwzakazeniowyCostume implements Costume {
  public String getId() {
    return "przeciwzakazeniowy";
  }
  
  public double getHealthBonus(Player player) {
    return 3.0D;
  }
  
  public double getDefenseMultiplier(Player player, EntityDamageEvent event) {
    return 0.9D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\PrzeciwzakazeniowyCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */