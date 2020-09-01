package net.sssubtlety.automated_crafting;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.sssubtlety.automated_crafting.mixin.DefaultedListAccessor;

public class CraftingInventoryWithoutHandler extends CraftingInventory {
    private static final CraftingScreenHandler dummyHandler = new CraftingScreenHandler(0, new PlayerInventory(null));

    public CraftingInventoryWithoutHandler(int width, int height) {
        super(dummyHandler, width, height);
    }

    public CraftingInventoryWithoutHandler(int width, int height, DefaultedList<ItemStack> contents) throws IllegalArgumentException {
        this(width, height);
        if (contents.size() == this.size())
            ((CraftingInventoryAccessor)this).setInventory(contents);
        else if (contents.size() > this.size())
            ((CraftingInventoryAccessor)this).setInventory(DefaultedListAccessor.createDefaultedList(contents
                    .subList(0, this.size()), ItemStack.EMPTY));
        else
            //  this.size() > contents.size()
            throw new IllegalArgumentException("Trying to create CraftingInventoryWithoutHandler from list with size < width * height. ");
//        int shorterLength = contents.size();
//
//            for (int i = 0; i < shorterLength; i++)
//                this.setStack(i, contents.get(i));
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ((CraftingInventoryAccessor)this).getStacks().set(slot, stack);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(((CraftingInventoryAccessor)this).getStacks(), slot, amount);
    }
}
