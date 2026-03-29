/* Decompiler 82ms, total 296ms, lines 61 */
package dev.arab.SZAFACORE.costumes.kostiumy;

import dev.arab.Main;
import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZbojczyKrolikCostume implements Costume {
  private final Main plugin;

  public ZbojczyKrolikCostume(Main plugin) {
    this.plugin = plugin;
  }

  public String getId() {
    return "zbojczego_krolika";
  }

  public void onEquip(Player player) {
    player.setAllowFlight(true);
  }

  public void onUnequip(Player player) {
    if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
      player.setAllowFlight(false);
      player.setFlying(false);
    }

  }

  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 40, 1));
  }

  public boolean onDamage(Player player, EntityDamageEvent event) {
    return event.getCause() == DamageCause.FALL;
  }

  public void onJump(Player player, PlayerToggleFlightEvent event) {
    if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
      event.setCancelled(true);
      player.setAllowFlight(false);
      player.setFlying(false);
      player.setVelocity(player.getLocation().getDirection().multiply(1.2D).setY(1.0D));
      player.sendMessage("§bPodwójny skok!");
      Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
        if (this.plugin.getCostumeManager().getActiveCostume(player) != null && this.plugin.getCostumeManager().getActiveCostume(player).equals(this.getId())) {
          player.setAllowFlight(true);
        }

      }, 300L);
    }
  }
}