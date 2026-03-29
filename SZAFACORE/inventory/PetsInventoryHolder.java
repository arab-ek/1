package dev.arab.SZAFACORE.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PetsInventoryHolder implements InventoryHolder {
  private final boolean managementMode;
  
  private Inventory inventory;
  
  public PetsInventoryHolder(boolean managementMode) {
    this.managementMode = managementMode;
  }
  
  public boolean isManagementMode() {
    return this.managementMode;
  }
  
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
  
  public Inventory getInventory() {
    return this.inventory;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\inventory\PetsInventoryHolder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */