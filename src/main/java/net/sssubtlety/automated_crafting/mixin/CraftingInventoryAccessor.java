package net.sssubtlety.automated_crafting.mixin;


import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingInventory.class)
public interface CraftingInventoryAccessor {
    @Accessor
    DefaultedList<ItemStack> getStacks();

    @Accessor
    @Mutable
    void setStacks(DefaultedList<ItemStack> stacks);
}
