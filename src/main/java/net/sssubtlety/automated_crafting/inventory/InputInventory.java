package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.Config;

import static net.sssubtlety.automated_crafting.AutoCrafterBlockEntity.templatePredicate;

public class InputInventory extends CraftingView {
    protected final Inventory templateInventory;

    public InputInventory(DefaultedList<ItemStack> stacks, Inventory templateInventory) {
        super(stacks);
        this.templateInventory = templateInventory;
    }

    public int getComparatorOutput() {
        return occupiedSlots.size();
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return super.isValid(slot, stack) && matchesTemplate(slot, stack);
    }

    protected boolean matchesTemplate(int slot, ItemStack inputStack) {
        boolean matches = templatePredicate(inputStack, this.templateInventory.getStack(slot));
        return !Config.isSimpleMode() || matches;
    }
}
