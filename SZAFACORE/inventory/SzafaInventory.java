package dev.arab.SZAFACORE.inventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dev.arab.Main;
import dev.arab.SZAFACORE.data.CostumeData;
import dev.arab.SZAFACORE.data.ParrotData;
import dev.arab.SZAFACORE.data.PetData;
import dev.arab.SZAFACORE.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SzafaInventory {
  private final Main plugin;
  
  private final ConfigManager configManager;
  
  public SzafaInventory(Main plugin) {
    this.plugin = plugin;
    this.configManager = plugin.getConfigManager();
  }
  
  public void openInventory(Player player) {
    String title = this.plugin.getConfigManager().getGuiTitle().replace("&", "§");
    Inventory inv = Bukkit.createInventory(new SzafkaInventoryHolder(), 54, title);
    refreshMainGui(player, inv);
    player.openInventory(inv);
  }
  
  public void refreshMainGui(Player player, Inventory inv) {
    inv.setItem(19, null);
    inv.setItem(21, null);
    inv.setItem(23, null);
    inv.setItem(25, null);
    inv.setItem(30, null);
    inv.setItem(32, null);
    inv.setItem(20, null);
    inv.setItem(24, null);
    inv.setItem(this.configManager.getSlot("gui.items.armor_stand"), this.configManager.getItem("gui.items.armor_stand"));
    inv.setItem(this.configManager.getSlot("gui.items.egg"), this.configManager.getItem("gui.items.egg"));
    inv.setItem(this.configManager.getSlot("gui.items.close_button"), this.configManager.getItem("gui.items.close_button"));
    PetData pData = this.plugin.getPetManager().getActivePet(player);
    if (pData != null) {
      String activePetId = pData.getPetId();
      long expiry = pData.getExpiryTimestamp();
      long currentTime = System.currentTimeMillis();
      long daysRemaining = (expiry == -1L) ? 999L : ((expiry - currentTime) / 86400000L);
      ItemStack petHead = this.plugin.getConfigManager().getItem("gui_pets.items." + activePetId);
      if (petHead != null) {
        ItemMeta meta = petHead.getItemMeta();
        if (meta != null) {
          if (meta instanceof org.bukkit.inventory.meta.LeatherArmorMeta)
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE }); 
          petHead.setItemMeta(meta);
        } 
        inv.setItem(15, petHead);
      } 
      ItemStack activeGlass = this.configManager.getItem("gui.items.pet_active_glass");
      if (activeGlass != null) {
        ItemMeta meta = activeGlass.getItemMeta();
        if (meta != null) {
          List<String> lore = meta.getLore();
          if (lore != null) {
            List<String> newLore = new ArrayList<>();
            for (String line : lore)
              newLore.add(line.replace("%time%", pData.getDurationString()).replace("&", "§")); 
            meta.setLore(newLore);
          } 
          if (meta instanceof org.bukkit.inventory.meta.LeatherArmorMeta)
            meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE }); 
          activeGlass.setItemMeta(meta);
        } 
        inv.setItem(24, activeGlass);
      } 
      ItemStack removePetBtn = this.configManager.getItem("gui.items.remove_pet_button");
      if (removePetBtn != null)
        inv.setItem(23, removePetBtn); 
      ItemStack extendPetBtn = this.configManager.getItem("gui.items.extend_pet_button");
      if (extendPetBtn != null) {
        ItemMeta meta = extendPetBtn.getItemMeta();
        if (meta != null) {
          List<String> lore = meta.getLore();
          if (lore != null) {
            String extItemName = this.plugin.getConfigManager().getConfig().getString("settings.pet_system.extension.item", "EMERALD");
            int extCost = this.plugin.getConfigManager().getConfig().getInt("settings.pet_system.extension.cost", 1);
            List<String> newLore = new ArrayList<>();
            for (String line : lore)
              newLore.add(line.replace("{cost}", String.valueOf(extCost)).replace("{item}", extItemName).replace("&", "§")); 
            meta.setLore(newLore);
          } 
          extendPetBtn.setItemMeta(meta);
        } 
        inv.setItem(25, extendPetBtn);
      } 
    } else {
      inv.setItem(15, this.configManager.getItem("gui.items.pet_slot"));
      inv.setItem(24, this.configManager.getItem("gui.items.pet_inactive_glass"));
    } 
    String activeCostume = this.plugin.getCostumeManager().getActiveCostume(player);
    if (activeCostume != null) {
      ItemStack costumeItem = this.plugin.getCostumeManager().getCostumeVisualItem(activeCostume);
      if (costumeItem != null) {
        ItemMeta meta = costumeItem.getItemMeta();
        if (meta != null && meta instanceof org.bukkit.inventory.meta.LeatherArmorMeta) {
          meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE });
          costumeItem.setItemMeta(meta);
        } 
        inv.setItem(11, costumeItem);
        CostumeData data = this.plugin.getCostumeManager().getActiveCostumeData(player);
        ItemStack activeGlass = this.plugin.getConfigManager().getItem("gui.items.costume_active_glass");
        if (activeGlass != null && data != null) {
          ItemMeta glassMeta = activeGlass.getItemMeta();
          if (glassMeta != null) {
            List<String> lore = glassMeta.getLore();
            if (lore != null) {
              List<String> newLore = new ArrayList<>();
              for (String line : lore)
                newLore.add(line.replace("%time%", data.getDurationString()).replace("&", "§")); 
              glassMeta.setLore(newLore);
            } 
            if (glassMeta instanceof org.bukkit.inventory.meta.LeatherArmorMeta)
              glassMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DYE }); 
            activeGlass.setItemMeta(glassMeta);
          } 
          inv.setItem(20, activeGlass);
        } 
        ItemStack removeCostumeBtn = this.configManager.getItem("gui.items.remove_costume_button");
        if (removeCostumeBtn != null)
          inv.setItem(19, removeCostumeBtn); 
        ItemStack extendCostumeBtn = this.configManager.getItem("gui.items.extend_costume_button");
        if (extendCostumeBtn != null) {
          ItemMeta glassMeta = extendCostumeBtn.getItemMeta();
          if (glassMeta != null) {
            List<String> lore = glassMeta.getLore();
            if (lore != null) {
              String extItemName = this.plugin.getConfigManager().getConfig().getString("settings.costume_system.extension.item", "EMERALD");
              int extCost = this.plugin.getConfigManager().getConfig().getInt("settings.costume_system.extension.cost", 1);
              int extDays = this.plugin.getConfigManager().getConfig().getInt("settings.costume_system.extension.days_added", 1);
              List<String> newLore = new ArrayList<>();
              for (String line : lore)
                newLore.add(line.replace("%cost%", String.valueOf(extCost)).replace("%item%", extItemName).replace("%days%", String.valueOf(extDays)).replace("&", "§")); 
              glassMeta.setLore(newLore);
            } 
            extendCostumeBtn.setItemMeta(glassMeta);
          } 
          inv.setItem(21, extendCostumeBtn);
        } 
      } 
    } else {
      inv.setItem(11, this.plugin.getConfigManager().getItem("gui.items.costume_slot"));
      inv.setItem(20, this.plugin.getConfigManager().getItem("gui.items.costume_inactive_glass"));
    } 
    ParrotData parrotData = this.plugin.getParrotManager().getActiveParrot(player);
    if (parrotData != null) {
      inv.setItem(31, getParrotItem(parrotData));
      inv.setItem(30, this.configManager.getItem("gui.items.parrot_remove_button"));
      ItemStack rerollItem = this.configManager.getItem("gui.items.parrot_reroll_button");
      if (rerollItem != null) {
        ItemMeta meta = rerollItem.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore != null) {
          double baseCost = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_cost", 1000.0D);
          double multiplier = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_multiplier", 2.0D);
          double currentCost = baseCost * Math.pow(multiplier, parrotData.getRerolls());
          List<String> newLore = new ArrayList<>();
          for (String line : lore)
            newLore.add(line.replace("%cost%", String.valueOf((int)currentCost)).replace("&", "§")); 
          meta.setLore(newLore);
          rerollItem.setItemMeta(meta);
        } 
        inv.setItem(32, rerollItem);
      } 
    } else {
      inv.setItem(31, this.configManager.getItem("gui.items.parrot_info"));
    } 
  }
  
  public void openCostumesInventory(Player player) {
    String title = this.plugin.getConfigManager().getConfig().getString("gui_costumes.title", "&8Kostiumy").replace("&", "§");
    Inventory inv = Bukkit.createInventory(new CostumesInventoryHolder(), 54, title);
    refreshCostumesInventory(player, inv);
    player.openInventory(inv);
  }
  
  public void refreshCostumesInventory(Player player, Inventory inv) {
    ConfigurationSection sec = this.plugin.getConfigManager().getConfig().getConfigurationSection("gui_costumes.items");
    if (sec != null)
      for (String key : sec.getKeys(false)) {
        ItemStack item = this.configManager.getItem("gui_costumes.items." + key);
        int slot = this.plugin.getConfigManager().getConfig().getInt("gui_costumes.items." + key + ".slot", -1);
        if (slot != -1)
          inv.setItem(slot, item); 
      }  
  }
  
  public void openPetsInventory(Player player) {
    String title = this.plugin.getConfigManager().getConfig().getString("gui_pets.title", "&8Pety").replace("&", "§");
    Inventory inv = Bukkit.createInventory(new PetsInventoryHolder(false), 54, title);
    refreshPetsInventory(player, inv);
    player.openInventory(inv);
  }
  
  public void refreshPetsInventory(Player player, Inventory inv) {
    ConfigurationSection sec = this.plugin.getConfigManager().getConfig().getConfigurationSection("gui_pets.items");
    if (sec != null)
      for (String key : sec.getKeys(false)) {
        ItemStack item = this.configManager.getItem("gui_pets.items." + key);
        int slot = this.plugin.getConfigManager().getConfig().getInt("gui_pets.items." + key + ".slot", -1);
        if (slot != -1)
          inv.setItem(slot, item); 
      }  
  }
  
  private ItemStack getParrotItem(ParrotData data) {
    String path = "gui_parrots.items." + data.getParrotId();
    ItemStack item = this.configManager.getItem(path);
    if (item == null)
      return null; 
    ItemMeta meta = item.getItemMeta();
    List<String> lore = meta.getLore();
    if (lore != null) {
      List<String> newLore = new ArrayList<>();
      for (String line : lore) {
        if (line.contains("%effects%")) {
          for (String effId : data.getEffects()) {
            String effName = this.plugin.getConfigManager().getConfig().getString("settings.parrot_system.effects." + effId + ".name", effId);
            newLore.add("  §8» " + effName.replace("&", "§"));
          } 
          continue;
        } 
        double bCost = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_cost", 1000.0D);
        double mult = this.plugin.getConfigManager().getConfig().getDouble("settings.parrot_system.reroll_multiplier", 2.0D);
        double currentCost = bCost * Math.pow(mult, data.getRerolls());
        newLore.add(line.replace("%collector%", data.getCollector()).replace("%rerolls%", String.valueOf(data.getRerolls())).replace("%cost%", String.valueOf((int)currentCost)).replace("%expiry%", (new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss")).format(new Date(data.getExpiryTimestamp()))).replace("&", "§"));
      } 
      meta.setLore(newLore);
      item.setItemMeta(meta);
    } 
    return item;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\inventory\SzafaInventory.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */