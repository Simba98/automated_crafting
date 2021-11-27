package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.sssubtlety.automated_crafting.Config;

public class TemplateInventory extends RecipeInventory {
    @Override
    public boolean isValid(int slot, ItemStack stack) {
//        return super.isValid(slot, stack) && Config.isSimpleMode();

        if (stack.isEmpty()) setEmpty(slot);
        else {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            setStack(slot, copy);
        }

        return false;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.removeStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        super.removeStack(slot);
        return ItemStack.EMPTY;
    }

//    @Override
//    public void setStack(int slot, ItemStack stack) {
//        if (stack.isEmpty()) return;
//        super.setStack(slot, stack);
//        occupiedSlots.add(slot);
//    }
}
