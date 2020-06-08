package net.sssubtlety.automated_crafting;

import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.minecraft.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.DefaultedList;

import java.util.Iterator;

public class CraftingInventoryWithOutput extends CraftingInventory {
    public CraftingInventoryWithOutput(Container container, int width, int height) {
        super(container, width, height);
    }

    public CraftingInventoryWithOutput(Container container, int width, int height, int outputs) {
        super(container, width, height);
        ((CraftingInventoryAccessor)this).setInventory(DefaultedList.ofSize(width * height + outputs, ItemStack.EMPTY));
    }

    public CraftingInventoryWithOutput(int width, int height, int outputs) {
        this(new InventoryContainer(0), width, height, outputs);
    }

    public CraftingInventoryWithOutput(int width, int height) {
        this(new InventoryContainer(0), width, height, 1);
    }

    @Override
    public int getInvSize() {
        return this.getWidth() * this.getHeight();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {
        System.out.println("provideRecipeInputs called");
        Iterator var2 = ((CraftingInventoryAccessor)this).getInventory().iterator();

        if(!var2.hasNext()) { return; }
        while(true) {
            ItemStack itemStack = (ItemStack)var2.next();
            if(!var2.hasNext()) { break; }
            recipeFinder.addNormalItem(itemStack);
        }
        System.out.println("provideRecipeInputs' recipeFinder.size(): " + recipeFinder.idToAmountMap.size());
    }
}
