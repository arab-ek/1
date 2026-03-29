package dev.arab.SZAFACORE.costumes.kostiumy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import dev.arab.SZAFACORE.costumes.Costume;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class WampirCostume implements Costume, Listener {
  private static boolean listenerRegistered = false;
  private static final Set<UUID> openShulkers = new HashSet<>();

  public WampirCostume() {
    // Rejestrujemy eventy tylko raz przy pierwszym załadowaniu kostiumu
    if (!listenerRegistered) {
      listenerRegistered = true;
      Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(WampirCostume.class));
    }
  }

  @Override
  public String getId() {
    return "wampira";
  }

  @Override
  public double getHealthBonus(Player player) {
    return 4.0D;
  }

  @Override
  public double getAttackMultiplier(Player player, EntityDamageByEntityEvent event) {
    return 1.1D;
  }

  @Override
  public void onInteract(Player player, PlayerInteractEvent event) {
    if (!event.getAction().name().contains("RIGHT")) return;

    ItemStack item = event.getItem();
    if (item != null && item.getType().name().contains("SHULKER_BOX") && item.getItemMeta() instanceof BlockStateMeta bsm) {
      if (bsm.getBlockState() instanceof ShulkerBox shulker) {
        // Tworzymy wirtualne inventory, żeby móc do niego włożyć i wyciągnąć itemy
        Inventory inv = Bukkit.createInventory(null, 27, item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "Shulker Box");
        inv.setContents(shulker.getInventory().getContents());

        player.openInventory(inv);
        openShulkers.add(player.getUniqueId());
        event.setCancelled(true);
      }
    }
  }

  // Zapisywanie itemów po zamknięciu Shulker Boxa
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    if (event.getPlayer() instanceof Player player) {
      if (openShulkers.remove(player.getUniqueId())) {
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand != null && hand.getType().name().contains("SHULKER_BOX") && hand.getItemMeta() instanceof BlockStateMeta bsm) {
          if (bsm.getBlockState() instanceof ShulkerBox shulker) {
            shulker.getInventory().setContents(event.getInventory().getContents());
            bsm.setBlockState(shulker);
            hand.setItemMeta(bsm);
          }
        } else {
          // Sprawdzamy drugą rękę (offhand) jeśli nie było w głównej
          ItemStack offHand = player.getInventory().getItemInOffHand();
          if (offHand != null && offHand.getType().name().contains("SHULKER_BOX") && offHand.getItemMeta() instanceof BlockStateMeta bsmOff) {
            if (bsmOff.getBlockState() instanceof ShulkerBox shulker) {
              shulker.getInventory().setContents(event.getInventory().getContents());
              bsmOff.setBlockState(shulker);
              offHand.setItemMeta(bsmOff);
            }
          }
        }
      }
    }
  }

  // Zabezpieczenie przed glitchem (wkładanie shulkera do shulkera / przekładanie)
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player player) {
      if (openShulkers.contains(player.getUniqueId())) {
        // Zablokuj kliknięcie na shulker box
        if (event.getCurrentItem() != null && event.getCurrentItem().getType().name().contains("SHULKER_BOX")) {
          event.setCancelled(true);
        }
        // Zablokuj podmianę z paska szybkiego wyboru (hotbar)
        if (event.getClick().name().contains("NUMBER_KEY")) {
          ItemStack hotbarItem = player.getInventory().getItem(event.getHotbarButton());
          if (hotbarItem != null && hotbarItem.getType().name().contains("SHULKER_BOX")) {
            event.setCancelled(true);
          }
        }
      }
    }
  }
}