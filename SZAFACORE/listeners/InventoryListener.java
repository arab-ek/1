package dev.arab.SZAFACORE.listeners;

import dev.arab.SZAFACORE.inventory.CostumesInventoryHolder;
import dev.arab.SZAFACORE.inventory.PetsInventoryHolder;
import dev.arab.SZAFACORE.inventory.SzafkaInventoryHolder;
import dev.arab.Main;
import dev.arab.SZAFACORE.data.CostumeData;
import dev.arab.SZAFACORE.inventory.SzafaInventory;
import dev.arab.SZAFACORE.manager.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryListener implements Listener {
  private final Main plugin;
  
  public InventoryListener(Main plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    StatsManager.updateStats(event.getPlayer());
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getClickedInventory() == null)
      return; 
    InventoryHolder holder = event.getView().getTopInventory().getHolder();
    if (holder instanceof SzafkaInventoryHolder) {
      event.setCancelled(true);
      handleMainGui(event);
    } else if (holder instanceof CostumesInventoryHolder) {
      event.setCancelled(true);
      handleCostumesGui(event);
    } else if (holder instanceof PetsInventoryHolder) {
      event.setCancelled(true);
      handlePetsGui(event);
    } 
  }
  
  private void handlePetsGui(InventoryClickEvent event) {
    if (event.getCurrentItem() == null)
      return; 
    Player player = (Player)event.getWhoClicked();
    int slot = event.getSlot();
    int backSlot = this.plugin.getConfigManager().getConfig().getInt("gui_pets.items.back_button.slot", 49);
    if (slot == backSlot) {
      (new SzafaInventory(this.plugin)).openInventory(player);
      return;
    } 
  }
  
  private void handleCostumesGui(InventoryClickEvent event) {
    if (event.getCurrentItem() == null)
      return; 
    Player player = (Player)event.getWhoClicked();
    int slot = event.getSlot();
    int backSlot = this.plugin.getConfigManager().getConfig().getInt("gui_costumes.items.back_button.slot", 49);
    if (slot == backSlot) {
      (new SzafaInventory(this.plugin)).openInventory(player);
      return;
    } 
  }
  
  private void handleMainGui(InventoryClickEvent event) {
    if (event.getCurrentItem() == null)
      return; 
    Player player = (Player)event.getWhoClicked();
    int slot = event.getSlot();
    int closeSlot = this.plugin.getConfigManager().getSlot("gui.items.close_button");
    if (slot == closeSlot) {
      player.closeInventory();
      return;
    } 
    if (slot == 19) {
      this.plugin.getCostumeManager().removeCostume(player);
      player.sendTitle(" ", "§cPomyślnie zdjęto kostium!", 10, 40, 10);
      (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
      return;
    } 
    if (slot == 21) {
      boolean useMoney = this.plugin.getConfigManager().getConfig().getBoolean("settings.costume_system.extension.use_money", false);
      double extCost = this.plugin.getConfigManager().getConfig().getDouble("settings.costume_system.extension.cost", 5000.0D);
      int extDays = this.plugin.getConfigManager().getConfig().getInt("settings.costume_system.extension.days_added", 30);
      if (useMoney) {
        if (Main.getEconomy() == null) {
          player.sendMessage("§cSystem ekonomii jest niedostępny!");
          return;
        } 
        if (Main.getEconomy().has((OfflinePlayer)player, extCost)) {
          Main.getEconomy().withdrawPlayer((OfflinePlayer)player, extCost);
          CostumeData data = this.plugin.getCostumeManager().getActiveCostumeData(player);
          if (data != null) {
            data.extendDays(extDays);
            this.plugin.getCostumeManager().saveData();
            player.sendTitle(" ", "§aPrzedłużono kostium o " + extDays + " dni!", 10, 40, 10);
            (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
          } 
        } else {
          player.sendMessage("§cNie masz wystarczająco pieniędzy!");
        } 
      } else {
        String extItemName = this.plugin.getConfigManager().getConfig().getString("settings.costume_system.extension.item", "EMERALD");
        Material extMaterial = Material.getMaterial(extItemName);
        if (extMaterial != null && player.getInventory().containsAtLeast(new ItemStack(extMaterial), (int)extCost)) {
          player.getInventory().removeItem(new ItemStack[] { new ItemStack(extMaterial, (int)extCost) });
          CostumeData data = this.plugin.getCostumeManager().getActiveCostumeData(player);
          if (data != null) {
            data.extendDays(extDays);
            this.plugin.getCostumeManager().saveData();
            player.sendTitle(" ", "§aPrzedłużono kostium o " + extDays + " dni!", 10, 40, 10);
            (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
          } 
        } else {
          player.sendMessage("§cNie masz wystarczająco pieniędzy!");
        } 
      } 
      return;
    } 
    int costumesSlot = this.plugin.getConfigManager().getSlot("gui.items.armor_stand");
    if (slot == costumesSlot) {
      (new SzafaInventory(this.plugin)).openCostumesInventory(player);
      return;
    } 
    int petsSlot = this.plugin.getConfigManager().getSlot("gui.items.egg");
    if (slot == petsSlot) {
      (new SzafaInventory(this.plugin)).openPetsInventory(player);
      return;
    } 
    if (slot == 30) {
      this.plugin.getParrotManager().removeParrot(player);
      player.sendTitle(" ", "§cPomyślnie zdjęto papuszkę!", 10, 40, 10);
      (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
      return;
    } 
    if (slot == 32) {
      if (this.plugin.getParrotManager().reroll(player))
        (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory()); 
      return;
    } 
    if (slot == 23) {
      ItemStack clicked = event.getCurrentItem();
      if (clicked != null && clicked.getType() == Material.RED_DYE) {
        this.plugin.getPetManager().removePet(player);
        (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
        return;
      } 
    } 
    if (slot == 25) {
      ItemStack clicked = event.getCurrentItem();
      if (clicked != null && clicked.getType() == Material.LIME_DYE) {
        boolean useMoney = this.plugin.getConfigManager().getConfig().getBoolean("settings.pet_system.extension.use_money", false);
        double extCost = this.plugin.getConfigManager().getConfig().getDouble("settings.pet_system.extension.cost", 5000.0D);
        int extDays = this.plugin.getConfigManager().getConfig().getInt("settings.pet_system.extension.days_added", 30);
        if (useMoney) {
          if (Main.getEconomy() == null) {
            player.sendMessage("§cSystem ekonomii jest niedostępny!");
            return;
          } 
          if (Main.getEconomy().has((OfflinePlayer)player, extCost)) {
            Main.getEconomy().withdrawPlayer((OfflinePlayer)player, extCost);
            this.plugin.getPetManager().extendPet(player, extDays);
            (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
          } else {
            player.sendMessage("§cNie masz wystarczająco pieniędzy!");
          } 
        } else {
          String extItemName = this.plugin.getConfigManager().getConfig().getString("settings.pet_system.extension.item", "EMERALD");
          Material extMaterial = Material.getMaterial(extItemName);
          if (extMaterial != null && player.getInventory().containsAtLeast(new ItemStack(extMaterial), (int)extCost)) {
            player.getInventory().removeItem(new ItemStack[] { new ItemStack(extMaterial, (int)extCost) });
            this.plugin.getPetManager().extendPet(player, extDays);
            (new SzafaInventory(this.plugin)).refreshMainGui(player, event.getInventory());
          } else {
            player.sendMessage("§cNie masz wystarczająco " + extItemName + "!");
          } 
        } 
        return;
      } 
    } 
  }
  
  @EventHandler
  public void onPersonalInventoryClick(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player))
      return; 
    Player player = (Player)event.getWhoClicked();
    InventoryType.SlotType slotType = event.getSlotType();
    boolean isShift = event.getClick().isShiftClick();
    if (slotType == InventoryType.SlotType.ARMOR) {
      ItemStack itemToPlace = (event.getClick() == ClickType.NUMBER_KEY) ? player.getInventory().getItem(event.getHotbarButton()) : event.getCursor();
      if (itemToPlace != null && itemToPlace.getType() != Material.AIR && this.plugin.getCostumeManager().isCostumeItem(itemToPlace)) {
        event.setCancelled(true);
        player.sendMessage("§cKostiumy można zakładać tylko prawym przyciskiem myszy w ręce!");
        return;
      } 
      ItemStack clickedItem = event.getCurrentItem();
      if (clickedItem != null && clickedItem.getType() != Material.AIR && this.plugin.getCostumeManager().isCostumeItem(clickedItem)) {
        event.setCancelled(true);
        player.sendMessage("§cKostiumy można zakładać tylko prawym przyciskiem myszy w ręce!");
        return;
      } 
    } 
    if (isShift) {
      ItemStack item = event.getCurrentItem();
      if (item != null && item.getType() != Material.AIR && this.plugin.getCostumeManager().isCostumeItem(item)) {
        event.setCancelled(true);
        player.sendMessage("§cKostiumy można zakładać tylko prawym przyciskiem myszy w ręce!");
        return;
      } 
    } 
    if (slotType == InventoryType.SlotType.ARMOR || isShift)
      Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
            this.plugin.getCostumeManager().updateVisualHiding(player);
            StatsManager.updateStats(player);
          }); 
  }
}