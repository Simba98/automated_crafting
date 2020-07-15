package net.sssubtlety.automated_crafting;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;

import java.util.Iterator;

public class CraftingInventoryWithOutput extends CraftingInventoryWithoutHandler {
    private final int invMaxStackAmount;

    public CraftingInventoryWithOutput(int width, int height, int outputs, int invMaxStackAmount, int apparentInvCount) {
        super(width, height);
        ((CraftingInventoryAccessor)this).setInventory(DefaultedList.ofSize(width * height * apparentInvCount + outputs, ItemStack.EMPTY));
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
        return this.getWidth() * this.getHeight();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {
        Iterator<?> invItr = ((CraftingInventoryAccessor)this).getInventory().iterator();

        if(!invItr.hasNext()) { return; }

        for (int remaining = this.size(); remaining >= 0; remaining--) {
            recipeFinder.addNormalItem((ItemStack)invItr.next());
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return invMaxStackAmount;
    }
}
