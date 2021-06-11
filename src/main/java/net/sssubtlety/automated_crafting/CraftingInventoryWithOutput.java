package net.sssubtlety.automated_crafting;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;

import java.util.Iterator;

public class CraftingInventoryWithOutput extends CraftingInventoryWithoutHandler implements Inventory {
    private final int invMaxStackAmount;

    public CraftingInventoryWithOutput(int width, int height, int outputs, int invMaxStackAmount, int apparentInvCount) {
        super(width, height * apparentInvCount + outputs);
//        this.setInventory(DefaultedList.ofSize(width * height * apparentInvCount + outputs, ItemStack.EMPTY));
        this.invMaxStackAmount = invMaxStackAmount;
    }

//    public CraftingInventoryWithOutput(int width, int height, int outputs, int apparentInvCount) {
//        this(width, height, outputs, 64, apparentInvCount);
//    }
//
//    public CraftingInventoryWithOutput(int width, int height, int apparentInvCount) {
//        this(width, height, 1, 64, apparentInvCount);
//    }

//    public CraftingInventoryWithOutput(int width, int height) {
//        this(width, height, 1);
//    }

    @Override
    public int size() {
        return this.getInventory().size();
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher matcher) {
        for (ItemStack stack : this.getInventory())
            matcher.addInput(stack);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return getStack(slot).isEmpty();
    }

    @Override
    public int getMaxCountPerStack() {
        return invMaxStackAmount;
    }

//    public DefaultedList<ItemStack> getInventory() {
//        return ((CraftingInventoryAccessor)this).getStacks();
//    }

//    public void setInventory(DefaultedList<ItemStack> newInventory) {
//        ((CraftingInventoryAccessor)this).setStacks(newInventory);
//    }
}
