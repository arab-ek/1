package dev.arab.KSIEGI;

import dev.arab.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KsiegiCommand implements CommandExecutor {
  private final Main plugin;
  
  private final BookManager bookManager;
  
  public KsiegiCommand(Main plugin, BookManager bookManager) {
    this.plugin = plugin;
    this.bookManager = bookManager;
  }
  
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    Player player;
    if (sender instanceof Player) {
      player = (Player)sender;
    } else {
      sender.sendMessage("Ta komenda jest tylko dla graczy!");
      return true;
    } 
    if (!player.hasPermission("ksiegi.use")) {
      player.sendMessage("§cNie masz uprawnień!");
      return true;
    } 
    (new KsiegiInventory(this.plugin, this.bookManager)).open(player);
    return true;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\KsiegiCommand.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */