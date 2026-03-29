package dev.arab.SZAFACORE.commands;

import dev.arab.Main;
import dev.arab.SZAFACORE.inventory.SzafaInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SzafaCommand implements CommandExecutor {
  private final Main plugin;
  
  public SzafaCommand(Main plugin) {
    this.plugin = plugin;
  }
  
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("Komenda tylko dla graczy!");
      return true;
    } 
    Player player = (Player)sender;
    SzafaInventory inventory = new SzafaInventory(this.plugin);
    inventory.openInventory(player);
    return true;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\commands\SzafaCommand.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */