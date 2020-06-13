package net.sssubtlety.automated_crafting;

import net.sssubtlety.automated_crafting.mixin.ContainerAccessor;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public class InventoryContainer extends Container {

    protected InventoryContainer(int size) {
        super(null, 1);
//        for (int i = 0; i < size; i++) {
//            this.addSlot(new Slot(null, i, i, 0));
//        }
        this.setInventory(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    @Override
    public boolean canUse(PlayerEntity player) { return false; }

//    public DefaultedList<ItemStack> getInventory() {
//        return ((ContainerAccessor)this).getTrackedStacks();
//    }

    public void setInventory(DefaultedList<ItemStack> stacks) {
        ((ContainerAccessor)this).setTrackedStacks(stacks);
    }
}
