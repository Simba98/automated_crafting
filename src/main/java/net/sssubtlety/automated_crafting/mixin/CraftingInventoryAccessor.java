package net.sssubtlety.automated_crafting.mixin;

import net.minecraft.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingInventory.class)
public interface CraftingInventoryAccessor {
    @Accessor("stacks")
    DefaultedList<ItemStack> getInventory();

    @Accessor("stacks")
    void setInventory(DefaultedList<ItemStack> inventory);

    @Accessor("container")
    Container getContainer();
}
