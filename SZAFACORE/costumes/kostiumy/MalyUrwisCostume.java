package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MalyUrwisCostume implements Costume {
  public String getId() {
    return "malego_urwisa";
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
  }
  
  public double getDefenseMultiplier(Player player, EntityDamageEvent event) {
    return (event.getCause() == EntityDamageEvent.DamageCause.FALL) ? 0.5D : 1.0D;
  }
  
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.1D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\MalyUrwisCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */