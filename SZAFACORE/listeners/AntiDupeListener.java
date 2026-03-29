/* Decompiler 129ms, total 332ms, lines 127 */
package dev.arab.SZAFACORE.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import dev.arab.Main;
import dev.arab.SZAFACORE.util.UniqueIdUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class AntiDupeListener implements Listener {
  private final Main plugin;
  private final Map<UUID, Long> lastCheck = new HashMap();

  public AntiDupeListener(Main plugin) {
    this.plugin = plugin;
    Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
        Player player = (Player)var1.next();
        this.checkInventory(player);
      }

    }, 1200L, 1200L);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    this.checkInventory(event.getPlayer());
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    if (event.getPlayer() instanceof Player) {
      this.checkInventory((Player)event.getPlayer());
    }

  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      this.checkInventory((Player)event.getWhoClicked());
    }

  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      this.checkInventory((Player)event.getWhoClicked());
    }

  }

  @EventHandler
  public void onPickup(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player) {
      this.checkInventory((Player)event.getEntity());
    }

  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    this.checkInventory(event.getPlayer());
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    this.checkInventory(event.getPlayer());
  }

  @EventHandler
  public void onItemHeld(PlayerItemHeldEvent event) {
    this.checkInventory(event.getPlayer());
  }

  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent event) {
    this.checkInventory(event.getPlayer());
  }

  public synchronized void checkInventory(Player player) {
    long now = System.currentTimeMillis();
    UUID uuid = player.getUniqueId();
    if (now - (Long)this.lastCheck.getOrDefault(uuid, 0L) >= 5000L) {
      this.lastCheck.put(uuid, now);
      Set<String> foundIds = new HashSet();
      ItemStack[] contents = player.getInventory().getContents();
      boolean removed = false;

      for(int i = 0; i < contents.length; ++i) {
        ItemStack item = contents[i];
        if (item != null && !item.getType().isAir()) {
          String uid = UniqueIdUtils.getUniqueId(item);
          if (uid != null && !uid.isEmpty() && !foundIds.add(uid)) {
            player.getInventory().setItem(i, (ItemStack)null);
            removed = true;
          }
        }
      }

      if (removed) {
        player.updateInventory();
      }

    }
  }
}