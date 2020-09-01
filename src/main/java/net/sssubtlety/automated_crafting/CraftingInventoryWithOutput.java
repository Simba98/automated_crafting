package net.sssubtlety.automated_crafting;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;

import java.util.Iterator;

public class CraftingInventoryWithOutput extends CraftingInventoryWithoutHandler implements Inventory {
    private final int invMaxStackAmount;

    public CraftingInventoryWithOutput(int width, int height, int outputs, int invMaxStackAmount, int apparentInvCount) {
        super(width, height);
        this.setInventory(DefaultedList.ofSize(width * height * apparentInvCount + outputs, ItemStack.EMPTY));
        this.invMaxStackAmount = invMaxStackAmount;
    }

    public CraftingInventoryWithOutput(int width, int height, int outputs, int apparentInvCount) {
        this(width, height, outputs, 64, apparentInvCount);
    }

    public CraftingInventoryWithOutput(int width, int height, int apparentInvCount) {
        this(width, height, 1, 64, apparentInvCount);
    }

    public CraftingInventoryWithOutput(int width, int height) {
        this(width, height, 1);
    }

    @Override
    public int size() {
        return this.getInventory().size();//this.getWidth() * this.getHeight();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {
        Iterator<?> invItr = this.getInventory().iterator();

        if(!invItr.hasNext()) { return; }

        for (int remaining = this.size(); remaining >= 0; remaining--) {
            recipeFinder.addNormalItem((ItemStack)invItr.next());
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return getStack(slot).isEmpty() && stack.getCount() <= getMaxCountPerStack();
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
