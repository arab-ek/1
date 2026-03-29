package dev.arab.KSIEGI.ksiazki;

import dev.arab.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedBook extends Book {
  public SpeedBook(Main plugin) {
    super(plugin, "szybkosc", "Szybkości I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("BOOTS");
  }
  
  public void onTick(Player player, ItemStack item) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0, false, false));
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\SpeedBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */