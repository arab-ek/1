package dev.arab.KSIEGI.ksiazki;

import dev.arab.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HasteBook extends Book {
  public HasteBook(Main plugin) {
    super(plugin, "pospiech", "Pośpiech I");
  }
  
  public boolean canApply(ItemStack item) {
    return item.getType().toString().contains("PICKAXE");
  }
  
  public void onTick(Player player, ItemStack item) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 40, 1, false, false));
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\ksiazki\HasteBook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */