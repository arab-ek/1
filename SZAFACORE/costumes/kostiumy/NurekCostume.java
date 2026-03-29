package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NurekCostume implements Costume {
  public String getId() {
    return "nurka";
  }
  
  public double getHealthBonus(Player player) {
    return 4.0D;
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
    if (player.getLocation().getBlock().getType() == Material.WATER)
      player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 40, 0)); 
  }
  
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return (player.getLocation().getBlock().getType() == Material.WATER) ? 1.3D : 1.0D;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\kostiumy\NurekCostume.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */