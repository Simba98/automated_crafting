package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class TemplateInventory extends CraftingView {
    public TemplateInventory(DefaultedList<ItemStack> stacks) {
        super(stacks);
    }

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
        setEmpty(slot);
        return ItemStack.EMPTY;
    }

//    @Override
//    public void setStack(int slot, ItemStack stack) {
//        if (stack.isEmpty()) return;
//        super.setStack(slot, stack);
//        occupiedSlots.add(slot);
//    }
}
