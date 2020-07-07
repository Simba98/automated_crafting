package net.sssubtlety.automated_crafting;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;

import java.util.Iterator;

public class CraftingInventoryWithOutput extends CraftingInventory {
    private final int invMaxStackAmount;
    private static final CraftingScreenHandler dummyHandler = new CraftingScreenHandler(0, new PlayerInventory(null));

    public CraftingInventoryWithOutput(ScreenHandler handler, int width, int height, int outputs, int invMaxStackAmount, int apparentInvCount) {
        super(handler, width, height);
        ((CraftingInventoryAccessor)this).setInventory(DefaultedList.ofSize(width * height * apparentInvCount + outputs, ItemStack.EMPTY));
        this.invMaxStackAmount = invMaxStackAmount;
    }

    public CraftingInventoryWithOutput(ScreenHandler handler, int width, int height, int outputs) {
        this(handler, width, height, outputs, 64, 1);
    }

    public CraftingInventoryWithOutput(int width, int height, int outputs, int invMaxStackAmount, int apparentInvCount) {
        this(dummyHandler, width, height, outputs, invMaxStackAmount, apparentInvCount);
    }

    public CraftingInventoryWithOutput(int width, int height, int outputs, int apparentInvCount) {
        this(dummyHandler, width, height, outputs, 64, apparentInvCount);
    }

    public CraftingInventoryWithOutput(int width, int height, int apparentInvCount) {
        this(dummyHandler, width, height, 1, 64, apparentInvCount);
    }

    public CraftingInventoryWithOutput(int width, int height) {
        this(dummyHandler, width, height, 1);
    }

    public void onContentChanged() {
        ((CraftingInventoryAccessor)this).getHandler().onContentChanged(this);
    }

    @Override
    public int size() {
        return this.getWidth() * this.getHeight();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {
//        System.out.println("provideRecipeInputs called");
        Iterator invItr = ((CraftingInventoryAccessor)this).getInventory().iterator();

        if(!invItr.hasNext()) { return; }

        for (int remaining = this.size(); remaining >= 0; remaining--) {
            recipeFinder.addNormalItem((ItemStack)invItr.next());
        }

        System.out.println("provideRecipeInputs' recipeFinder.size(): " + recipeFinder.idToAmountMap.size());
    }

    @Override
    public int getMaxCountPerStack() {
        return invMaxStackAmount;
    }
}
