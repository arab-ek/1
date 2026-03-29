package dev.arab.SZAFACORE.commands;

import java.util.ArrayList;
import java.util.List;
import dev.arab.Main;
import dev.arab.SZAFACORE.util.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminSzafaCommand implements CommandExecutor, TabCompleter {
  private final Main plugin;
  
  public AdminSzafaCommand(Main plugin) {
    this.plugin = plugin;
  }
  
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!sender.hasPermission("paczkiszafa.admin")) {
      sender.sendMessage(this.plugin.getConfigManager().getMessage("no_permission").replace("&", "§"));
      return true;
    } 
    if (args.length > 0 && args[0].equalsIgnoreCase("daj")) {
      if (args.length < 4) {
        sender.sendMessage("§cUzycie: /aszafa daj <nick> <pet|costume> <id> [czas]");
        sender.sendMessage("§7Przyklad: /aszafa daj Notch pet aniolek 7d");
        sender.sendMessage("§7Przyklad: /aszafa daj Notch costume walentynkowy");
        return true;
      } 
      String targetName = args[1];
      String type = args[2].toLowerCase();
      String id = args[3].toLowerCase();
      String duration = (args.length >= 5) ? args[4] : null;
      Player target = Bukkit.getPlayer(targetName);
      if (target == null) {
        sender.sendMessage("§cGracz nie jest online!");
        return true;
      } 
      if (type.equals("pet")) {
        if (!this.plugin.getConfigManager().getConfig().contains("gui_pets.items." + id)) {
          sender.sendMessage("§cTaki pet nie istnieje!");
          return true;
        } 
        if (id.equalsIgnoreCase("back_button")) {
          sender.sendMessage("§cNie mozna dac 'back_button' jako peta!");
          return true;
        } 
        this.plugin.getPetManager().givePetItem(target, id, null, duration);
        sender.sendMessage("§aDano peta " + id + " graczowi " + target.getName() + ((duration != null) ? (" na " + duration) : ""));
      } else if (type.equals("costume")) {
        if (!this.plugin.getConfigManager().getConfig().contains("gui_costumes.items." + id)) {
          sender.sendMessage("§cTaki kostium nie istnieje!");
          return true;
        } 
        if (id.equalsIgnoreCase("back_button")) {
          sender.sendMessage("§cNie mozna dac 'back_button' jako kostiumu!");
          return true;
        } 
        String durationStr = (args.length >= 5) ? args[4] : null;
        long durationMs = (durationStr != null) ? TimeConverter.parseTimeToMillis(durationStr) : 0L;
        this.plugin.getCostumeManager().giveCostumeItem(target, id, null, durationMs);
        sender.sendMessage("§aNadano przedmiot kostiumu " + id + " graczowi " + target.getName() + ((durationStr != null) ? (" na czas " + durationStr) : "") + "!");
      } else if (type.equals("parrot")) {
        if (!this.plugin.getConfigManager().getConfig().contains("gui_parrots.items." + id)) {
          sender.sendMessage("§cTaka papuszka nie istnieje!");
          return true;
        } 
        this.plugin.getParrotManager().giveParrotItem(target, id, duration);
        sender.sendMessage("§aNadano przedmiot papuszki " + id + " graczowi " + target.getName() + ((duration != null) ? (" na czas " + duration) : "") + "!");
      } else {
        sender.sendMessage("§cTyp musi byc 'pet', 'costume' lub 'parrot'!");
      } 
      return true;
    } 
    sender.sendMessage("§cPoprawne uzycie: /aszafa <daj>");
    return true;
  }
  
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (args.length == 1)
      return List.of("daj"); 
    if (args.length == 2 && args[0].equalsIgnoreCase("daj"))
      return null; 
    if (args.length == 3 && args[0].equalsIgnoreCase("daj"))
      return List.of("pet", "costume", "parrot"); 
    if (args.length == 4 && args[0].equalsIgnoreCase("daj")) {
      String type = args[2].toLowerCase();
      String path = type.equals("costume") ? "gui_costumes.items" : (type.equals("pet") ? "gui_pets.items" : "gui_parrots.items");
      ConfigurationSection sec = this.plugin.getConfigManager().getConfig().getConfigurationSection(path);
      if (sec != null) {
        List<String> keys = new ArrayList<>(sec.getKeys(false));
        keys.removeIf(key -> key.equalsIgnoreCase("back_button"));
        return keys;
      } 
    } 
    return (args.length == 5 && args[0].equalsIgnoreCase("daj")) ? List.of("1d", "7d", "30d", "1h", "24h") : List.of();
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\commands\AdminSzafaCommand.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */