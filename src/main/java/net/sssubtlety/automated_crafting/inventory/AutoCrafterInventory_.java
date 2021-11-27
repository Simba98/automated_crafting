package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.CraftingInventoryWithoutHandler;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;

/*
    ArrayInventory crafting without a screen handler.
    This means it's not bound to a single player.
    It also means that a screen handler must be constructed whenever a player views the inventory.
*/
public class AutoCrafterInventory_ extends CraftingInventoryWithoutHandler implements Inventory {
    public AutoCrafterInventory_(int width, int height) {
        super(width, height);
    }

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
        return 1;
    }
}
