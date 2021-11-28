package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class InputInventory extends CraftingView {
    public InputInventory(DefaultedList<ItemStack> stacks) {
        super(stacks);
    }

    public int getComparatorOutput() {
        return occupiedSlots.size();
    }
}
