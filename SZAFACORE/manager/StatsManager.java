package dev.arab.SZAFACORE.manager;

import java.util.ArrayList;
import java.util.UUID;
import dev.arab.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;

public class StatsManager {
  private static final UUID HEALTH_UID = UUID.fromString("8e1627c2-9a04-4861-8dc1-2c06f8517c5b");
  private static final UUID DAMAGE_UID = UUID.fromString("3d94f8a1-b5e2-4c76-92a3-f01de83cb45a");
  private static final UUID ARMOR_UID  = UUID.fromString("c72d5b4e-81f3-4a09-b69c-5d2f1e4a3b7c");

  public static void updateStats(Player player) {
    Main plugin = Main.getPlugin(Main.class);
    double extraHealth = 0.0D;
    double extraDamage = 0.0D;

    if (plugin.getCostumeManager() != null) {
      extraHealth += plugin.getCostumeManager().getHealthBonus(player);
      extraDamage += plugin.getCostumeManager().getAttackBonus(player);
    }

    if (plugin.getPetManager() != null) {
      extraHealth += plugin.getPetManager().getHealthBonus(player);
      extraDamage += plugin.getPetManager().getAttackBonus(player);
    }

    if (plugin.getParrotManager() != null) {
      extraHealth += plugin.getParrotManager().getHealthBonus(player);
    }

    // --- OBRAŻENIA ---
    AttributeInstance attackDamageAttr = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
    if (attackDamageAttr != null) {
      for (AttributeModifier mod : new ArrayList<>(attackDamageAttr.getModifiers())) {
        if (mod.getUniqueId().equals(DAMAGE_UID)) {
          attackDamageAttr.removeModifier(mod);
        }
      }
      if (extraDamage > 0.0D) {
        attackDamageAttr.addModifier(new AttributeModifier(DAMAGE_UID, "szafa_damage_bonus", extraDamage, Operation.ADD_NUMBER));
      }
    }

    // --- ZBROJA ---
    AttributeInstance armorAttr = player.getAttribute(Attribute.GENERIC_ARMOR);
    if (armorAttr != null) {
      for (AttributeModifier mod : new ArrayList<>(armorAttr.getModifiers())) {
        if (mod.getUniqueId().equals(ARMOR_UID)) {
          armorAttr.removeModifier(mod);
        }
      }
    }
  }
}