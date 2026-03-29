package dev.arab.KSIEGI;

import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import dev.arab.Main;
import dev.arab.EVENTOWKICORE.utils.EquipmentCacheManager;
import dev.arab.EVENTOWKICORE.utils.EquipmentCacheManager.PlayerEquipment;
import dev.arab.KSIEGI.ksiazki.Book;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BookListener implements Listener {
  private final Main plugin;
  private final BookManager bookManager;
  private static final Set<Material> SUPPORTED_MATERIALS = EnumSet.noneOf(Material.class);

  public BookListener(Main plugin, BookManager bookManager) {
    this.plugin = plugin;
    this.bookManager = bookManager;
    this.startPassiveTask();
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getClickedInventory() instanceof AnvilInventory inv && event.getRawSlot() == 2) {
      ItemStack target = inv.getItem(1);
      if (target != null && target.getType() == Material.ENCHANTED_BOOK) {
        String bookId = this.bookManager.getBookId(target);
        if (bookId != null) {
          Book book = this.bookManager.getBookById(bookId);
          if (book != null) {
            ItemStack result = event.getCurrentItem();
            if (result != null && result.getType() != Material.AIR) {
              Player player = (Player)event.getWhoClicked();
              if (player.getLevel() >= inv.getRepairCost() || player.getGameMode() == GameMode.CREATIVE) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                  player.setLevel(player.getLevel() - inv.getRepairCost());
                }

                ItemStack finalResult = result.clone();
                event.setCancelled(true);
                player.setItemOnCursor(finalResult);

                final AnvilInventory finalInv = inv;
                final ItemStack finalTarget = target;
                final Player finalPlayer = player;

                this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
                  finalInv.setItem(0, new ItemStack(Material.AIR));
                  if (finalTarget.getAmount() > 1) {
                    finalTarget.setAmount(finalTarget.getAmount() - 1);
                    finalInv.setItem(1, finalTarget);
                  } else {
                    finalInv.setItem(1, new ItemStack(Material.AIR));
                  }
                  finalPlayer.updateInventory();
                });
                return;
              }

              event.setCancelled(true);
            }
          }
        }
      }
    }

    if (!event.getView().getTitle().contains("Zaklęte Książki")) {
      ItemStack cursor = event.getCursor();
      ItemStack target = event.getCurrentItem();
      if (cursor != null && cursor.getType() == Material.ENCHANTED_BOOK && target != null && target.getType() != Material.AIR) {
        String bookId = this.bookManager.getBookId(cursor);
        if (bookId != null) {
          Book book = this.bookManager.getBookById(bookId);
          if (book != null) {
            if (book.canApply(target)) {
              if (book.hasBook(target)) {
                return;
              }

              event.setCancelled(true);
              book.apply(target);
              cursor.setAmount(cursor.getAmount() - 1);
              event.getWhoClicked().sendMessage("§aPomyślnie nałożono zaklęcie!");
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onPrepareAnvil(PrepareAnvilEvent event) {
    AnvilInventory inv = event.getInventory();
    ItemStack target = inv.getItem(0);
    ItemStack addition = inv.getItem(1);
    if (addition != null && addition.getType() == Material.ENCHANTED_BOOK && target != null && target.getType() != Material.AIR) {
      String bookId = this.bookManager.getBookId(addition);
      if (bookId != null) {
        Book book = this.bookManager.getBookById(bookId);
        if (book != null) {
          if (book.canApply(target)) {
            if (book.hasBook(target)) {
              event.setResult(null);
              return;
            }

            ItemStack result = target.clone();
            book.apply(result);
            inv.setRepairCost(3);
            event.setResult(result);
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
              inv.setRepairCost(3);
            });
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onDamage(EntityDamageByEntityEvent event) {
    Entity victimEntity = event.getEntity();
    if (victimEntity instanceof Player) {
      Player victim = (Player)victimEntity;
      for (ItemStack armor : victim.getInventory().getArmorContents()) {
        if (armor != null && armor.getType() != Material.AIR) {
          ItemMeta meta = armor.getItemMeta();
          if (meta != null && this.bookManager.hasCustomBook(armor, meta)) {
            for (Book book : this.bookManager.getBooks()) {
              if (book.hasBook(meta) && book.canApply(armor)) {
                book.onDamageVictim(event, victim, armor);
              }
            }
          }
        }
      }
    }

    Entity damagerEntity = event.getDamager();
    if (damagerEntity instanceof Player) {
      Player damager = (Player)damagerEntity;
      ItemStack weapon = damager.getInventory().getItemInMainHand();
      if (weapon != null && weapon.getType() != Material.AIR) {
        ItemMeta meta = weapon.getItemMeta();
        if (meta == null) return;

        if (this.bookManager.hasCustomBook(weapon, meta)) {
          boolean modified = false;
          for (Book book : this.bookManager.getBooks()) {
            if (book.hasBook(meta) && book.canApply(weapon)) {
              book.onDamageDamager(event, damager, weapon);
              modified = true;
            }
          }

          if (modified) {
            weapon.setItemMeta(meta);
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onItemDamage(PlayerItemDamageEvent event) {
    ItemStack item = event.getItem();
    if (item != null && item.getType() != Material.AIR) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
        if (this.bookManager.hasCustomBook(item, meta)) {
          for (Book book : this.bookManager.getBooks()) {
            if (book.hasBook(meta) && book.canApply(item)) {
              book.onItemDamage(event, item);
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onInventoryClickMigration(InventoryClickEvent event) {
    ItemStack item = event.getCurrentItem();
    if (item != null && item.getType() != Material.AIR) {
      this.bookManager.migrateItem(item);
    }

    ItemStack cursor = event.getCursor();
    if (cursor != null && cursor.getType() != Material.AIR) {
      this.bookManager.migrateItem(cursor);
    }
  }

  private void startPassiveTask() {
    this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
      EquipmentCacheManager cache = this.plugin.getEquipmentCacheManager();
      for (Player player : this.plugin.getServer().getOnlinePlayers()) {
        PlayerEquipment equipment = cache.getEquipment(player.getUniqueId());
        if (equipment != null) {
          for (Entry<Integer, List<Book>> entry : equipment.slotBooks.entrySet()) {
            ItemStack item = player.getInventory().getItem(entry.getKey());
            if (item != null && item.getType() != Material.AIR) {
              for (Book book : entry.getValue()) {
                book.onTick(player, item);
              }
            }
          }
        }
      }
    }, 40L, 40L);
  }

  private boolean isArmor(Material type) {
    String name = type.name();
    return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS") || name.equals("ELYTRA");
  }

  static {
    for (Material mat : Material.values()) {
      String name = mat.name();
      if (name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS") || name.endsWith("_SWORD") || name.endsWith("_PICKAXE") || name.equals("ELYTRA")) {
        SUPPORTED_MATERIALS.add(mat);
      }
    }
  }
}