package dev.arab.SZAFACORE.pets.pety;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SowaPet implements Pet {
  private static final Map<UUID, UUID> lastTarget = new HashMap<>();
  
  private static final Map<UUID, Long> lastHitTime = new HashMap<>();
  
  private static final long DISPLAY_DURATION_MS = 3000L;
  
  public static void recordHit(UUID attackerUUID, UUID targetUUID) {
    lastTarget.put(attackerUUID, targetUUID);
    lastHitTime.put(attackerUUID, Long.valueOf(System.currentTimeMillis()));
  }
  
  public static String getTargetInfo(UUID attackerUUID) {
    Long hitTime = lastHitTime.get(attackerUUID);
    if (hitTime == null)
      return null; 
    if (System.currentTimeMillis() - hitTime.longValue() > 3000L) {
      lastTarget.remove(attackerUUID);
      lastHitTime.remove(attackerUUID);
      return null;
    } 
    UUID targetUUID = lastTarget.get(attackerUUID);
    if (targetUUID == null)
      return null; 
    Player target = Bukkit.getPlayer(targetUUID);
    if (target == null || !target.isOnline()) {
      lastTarget.remove(attackerUUID);
      lastHitTime.remove(attackerUUID);
      return null;
    } 
    double hp = target.getHealth();
    double hearts = hp / 2.0D;
    String heartsStr = (hearts == Math.floor(hearts)) ? String.valueOf((int)hearts) : String.format("%.1f", new Object[] { Double.valueOf(hearts) });
    return "§7" + target.getName() + " §c" + heartsStr + "❤";
  }
  
  public String getId() {
    return "sowa";
  }
  
  public double getAttackBonus(Player player) {
    return 3.0D;
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, false));
  }
  
  public void onFoodChange(Player player, FoodLevelChangeEvent event) {
    event.setCancelled(true);
    player.setFoodLevel(20);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\SowaPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */