package net.sssubtlety.automated_crafting;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;

public class CraftingInventoryWithoutHandler extends CraftingInventory {
    private static final CraftingScreenHandler dummyHandler = new CraftingScreenHandler(0, new PlayerInventory(null));

    public CraftingInventoryWithoutHandler(int width, int height) {
        super(dummyHandler, width, height);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ((CraftingInventoryAccessor)this).getInventory().set(slot, stack);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(((CraftingInventoryAccessor)this).getInventory(), slot, amount);
    }
}
