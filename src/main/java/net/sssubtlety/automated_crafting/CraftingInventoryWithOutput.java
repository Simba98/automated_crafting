package net.sssubtlety.automated_crafting;

import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.minecraft.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.DefaultedList;

import java.util.Iterator;

public class CraftingInventoryWithOutput extends CraftingInventory {
    private final int invMaxStackAmount;

    public CraftingInventoryWithOutput(Container container, int width, int height, int outputs, int invMaxStackAmount, boolean templated) {
        super(container, width, height);
        ((CraftingInventoryAccessor)this).setInventory(DefaultedList.ofSize((width * height * (templated ? 2 : 1)) + outputs, ItemStack.EMPTY));
        this.invMaxStackAmount = invMaxStackAmount;
    }

    public CraftingInventoryWithOutput(Container container, int width, int height, int outputs) {
        this(container, width, height, outputs, 64, false);
    }

    public CraftingInventoryWithOutput(int width, int height, int outputs, int invMaxStackAmount) {
        this(new InventoryContainer(0), width, height, outputs, invMaxStackAmount, false);
    }

    public CraftingInventoryWithOutput(int width, int height) {
        this(new InventoryContainer(0), width, height, 1);
    }

    public CraftingInventoryWithOutput(int width, int height, int invMaxStackAmount, boolean templated) {
        this(new InventoryContainer(0), width, height, 1, invMaxStackAmount, templated);
    }

    public void onContentChanged() {
        ((CraftingInventoryAccessor)this).getContainer().onContentChanged(this);
    }

    @Override
    public int getInvSize() {
        return this.getWidth() * this.getHeight();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {
        System.out.println("provideRecipeInputs called");
        Iterator invItr = ((CraftingInventoryAccessor)this).getInventory().iterator();

        if(!invItr.hasNext()) { return; }

        for (int remaining = this.getInvSize(); remaining >= 0; remaining--) {
            recipeFinder.addNormalItem((ItemStack)invItr.next());
        }

        System.out.println("provideRecipeInputs' recipeFinder.size(): " + recipeFinder.idToAmountMap.size());
    }

    @Override
    public int getInvMaxStackAmount() {
        return invMaxStackAmount;
    }
}
