package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PajakPet implements Pet {
  public String getId() {
    return "pajak";
  }
  
  public void onTick(Player player) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1, true, false));
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, true, false));
  }
  
  public void onDamage(Player player, EntityDamageEvent event) {
    if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\pets\pety\PajakPet.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */