package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.stream.Stream;

public interface TrimmableInventory extends Inventory {
    Inventory getTrimmed();
    Stream<ItemStack> getTrimmedStream();
}
