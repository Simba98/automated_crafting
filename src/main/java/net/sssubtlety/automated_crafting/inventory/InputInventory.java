package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.sssubtlety.automated_crafting.Config;

public class InputInventory extends RecipeInventory {
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return true;
    }

    public int getComparatorOutput() {
        return occupiedSlots.size();
    }
}
