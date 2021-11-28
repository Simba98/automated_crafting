package net.sssubtlety.automated_crafting.gui;

import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.sssubtlety.automated_crafting.AutoCrafterBlockEntity;

import java.util.function.BiPredicate;

public class WTemplatedSlot extends WItemSlot {
    public static boolean templateFilter(ItemStack stack, int i, Inventory templateInventory) {
        return AutoCrafterBlockEntity.templatePredicate(stack, templateInventory.getStack(i));
    }

    protected final Inventory templateInventory;
    protected final int templateIndexOffset;

    public WTemplatedSlot(Inventory inventory, int startIndex, Inventory templateInventory, int templateIndexOffset, int slotsWide, int slotsHigh, boolean big) {
        super(inventory, startIndex, slotsWide, slotsHigh, big);
        this.templateInventory = templateInventory;
        this.templateIndexOffset = templateIndexOffset;
    }

    public static WTemplatedSlot of(Inventory inventory, int startIndex, Inventory templateInventory, int templateIndexOffset, int slotsHigh, int slotsWide) {
        return new WTemplatedSlot(inventory, startIndex, templateInventory, templateIndexOffset, slotsWide, slotsHigh, false);
    }

    public static WTemplatedSlot of(Inventory inventory, int index, Inventory templateInventory, int templateIndex) {
        return WTemplatedSlot.of(inventory, index, templateInventory, templateIndex, 1, 1);
    }

    @Override
    protected ValidatedSlot createSlotPeer(Inventory inventory, int index, int x, int y) {
        ValidatedSlot peer = super.createSlotPeer(inventory, index, x, y);
        this.setFilter(stack -> templateFilter(stack, index + templateIndexOffset, templateInventory));
        return peer;
    }
}
