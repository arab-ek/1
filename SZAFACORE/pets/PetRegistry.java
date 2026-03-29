package dev.arab.SZAFACORE.pets;

import java.util.HashMap;
import java.util.Map;
import dev.arab.Main;

public class PetRegistry {
  private final Main plugin;
  
  private final Map<String, Pet> pets = new HashMap<>();
  
  public PetRegistry(Main plugin) {
    this.plugin = plugin;
  }
  public void register(Pet pet) {
    this.pets.put(pet.getId().toLowerCase(), pet);
  }
  public Pet getPet(String id) {
    return (id == null) ? null : this.pets.get(id.toLowerCase());
  }
}
