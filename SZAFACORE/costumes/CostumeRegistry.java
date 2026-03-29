package dev.arab.SZAFACORE.costumes;

import java.util.HashMap;
import java.util.Map;
import dev.arab.Main;

public class CostumeRegistry {
  private final Map<String, Costume> costumes = new HashMap<>();
  
  public CostumeRegistry(Main plugin) {}
  
  public void register(Costume costume) {
    this.costumes.put(costume.getId(), costume);
  }
  
  public Costume getCostume(String id) {
    return this.costumes.get(id);
  }
  
  public Map<String, Costume> getAllCostumes() {
    return this.costumes;
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\costumes\CostumeRegistry.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */