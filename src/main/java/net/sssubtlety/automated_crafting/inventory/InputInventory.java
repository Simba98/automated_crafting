package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.sssubtlety.automated_crafting.Config;

public class InputInventory extends RecipeInventory {
    public int getComparatorOutput() {
        return occupiedSlots.size();
    }
}
