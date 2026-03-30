package dev.arab.SZAFACORE.pets.pety;

import dev.arab.SZAFACORE.pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class LeniuszekPet implements Pet {

    // Ta metoda jest wymagana przez interfejs Pet (mówi o błędzie "must implement abstract method 'getId()'")
    @Override
    public String getId() {
        return "leniuszek";
    }

    // --- Poniższe metody są opcjonalne (mają modyfikator 'default' w interfejsie),
    // ale zostawiamy je na wypadek, gdybyś chciał dodać efekty w przyszłości ---

    @Override
    public double getHealthBonus(Player player) {
        return 0.0D;
    }

    @Override
    public double getAttackBonus(Player player) {
        return 0.0D;
    }

    @Override
    public void onEquip(Player player) {
    }

    @Override
    public void onUnequip(Player player) {
    }

    @Override
    public void onTick(Player player) {
    }

    @Override
    public void onFoodChange(Player player, FoodLevelChangeEvent event) {
    }

    @Override
    public void onDamage(Player player, EntityDamageEvent event) {
    }
}