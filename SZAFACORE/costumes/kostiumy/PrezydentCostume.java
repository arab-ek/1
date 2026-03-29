package dev.arab.SZAFACORE.costumes.kostiumy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import dev.arab.Main;
import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;

public class PrezydentCostume implements Costume {
    private final Main plugin;
    private static final Map<UUID, UUID> markedTargets = new HashMap<>();
    private static final Map<UUID, Long> markExpiries = new HashMap<>();

    public PrezydentCostume(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "prezydenta";
    }

    @Override
    public double getHealthBonus(Player player) {
        return 3.0D; // 1.5 serca
    }

    @Override
    public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
        double multiplier = 1.05D; // +5% bazowych obrażeń

        if (event.getEntity() instanceof Player victim) {
            Long expiry = markExpiries.get(player.getUniqueId());
            // Jeśli cel jest oznaczony i czas jeszcze nie minął: +50% obrażeń!
            if (expiry != null && expiry > System.currentTimeMillis() && victim.getUniqueId().equals(markedTargets.get(player.getUniqueId()))) {
                multiplier *= 1.5D;
            }
        }

        return multiplier;
    }

    @Override
    public void onTick(Player player) {
        // Prezydent na bieżąco usuwa z siebie negatywne efekty
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (isNegativeEffect(effect.getType())) {
                player.removePotionEffect(effect.getType());
            }
        }
    }

    private boolean isNegativeEffect(PotionEffectType type) {
        return type.equals(PotionEffectType.POISON) ||
                type.equals(PotionEffectType.WITHER) ||
                type.equals(PotionEffectType.SLOW) ||
                type.equals(PotionEffectType.WEAKNESS) ||
                type.equals(PotionEffectType.BLINDNESS) ||
                type.equals(PotionEffectType.CONFUSION) ||
                type.equals(PotionEffectType.HUNGER);
    }

    @Override
    public void onInteract(Player player, PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK")) {
            // Promień na 5 bloków, by zaznaczyć gracza
            RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 5.0D, entity -> entity instanceof Player && !entity.equals(player));

            if (result != null && result.getHitEntity() instanceof Player target) {
                markedTargets.put(player.getUniqueId(), target.getUniqueId());
                markExpiries.put(player.getUniqueId(), System.currentTimeMillis() + 10000L);

                applyGlow(target);
                player.sendTitle("", "§fOznaczono gracza §a" + target.getName() + " §fna 10 sekund!", 10, 70, 20);
            }
        }
    }

    private void applyGlow(Player target) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("red_glow");
        if (team == null) {
            team = board.registerNewTeam("red_glow");
            team.setColor(ChatColor.RED);
        }

        team.addEntry(target.getName());
        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0)); // 10 sekund glow

        // Automatyczne usunięcie poświaty po 10 sekundach (200 tickach)
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Team t = board.getTeam("red_glow");
            if (t != null) {
                t.removeEntry(target.getName());
            }
        }, 200L);
    }
}