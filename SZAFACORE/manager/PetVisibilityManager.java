package dev.arab.SZAFACORE.manager;

import java.util.UUID;
import dev.arab.Main;

public class PetVisibilityManager {
  private static Main plugin;
  
  public static void init(Main instance) {
    plugin = instance;
  }
  
  public static boolean isVisible(UUID uuid) {
    return plugin.getConfig().getBoolean("visibility." + uuid.toString(), true);
  }
  
  public static boolean toggle(UUID uuid) {
    boolean newState = !isVisible(uuid);
    plugin.getConfig().set("visibility." + uuid.toString(), Boolean.valueOf(newState));
    plugin.saveConfig();
    return newState;
  }
}