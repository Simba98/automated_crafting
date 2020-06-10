package net.sssubtlety.automated_crafting;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.sssubtlety.automated_crafting.mixin.WItemSlotAccessor;
import org.spongepowered.asm.mixin.Mixin;

public class LimitedStackSlot extends WItemSlot {
    private int stackLimit;
    public LimitedStackSlot(Inventory inventory, int startIndex, int slotsWide, int slotsHigh, boolean big, boolean ltr, int stackLimit) {
        super(inventory, startIndex, slotsWide, slotsHigh, big, ltr);
        this.stackLimit = stackLimit;
    }

    public static LimitedStackSlot of(Inventory inventory, int startIndex, int slotsWide, int slotsHigh, int stackLimit) {
        LimitedStackSlot w = new LimitedStackSlot(inventory, startIndex, slotsWide, slotsHigh, false, false, stackLimit);
        ((WItemSlotAccessor)w).setInventory(inventory);
        ((WItemSlotAccessor)w).setStartIndex(startIndex);
        ((WItemSlotAccessor)w).setSlotsWide(slotsWide);
        ((WItemSlotAccessor)w).setSlotsHigh(slotsHigh);
        w.stackLimit = stackLimit;
        return w;
    }

    public boolean isModifiable() {
        return this.isTakingAllowed() || this.isInsertingAllowed();
    }

    public boolean isInsertingAllowed() {
        System.out.println("isInsertingAllowed has index: " + ((WItemSlotAccessor)this).getInventory().getInvStack(((WItemSlotAccessor)this).getStartIndex()));
        System.out.println("isInsertingAllowed returning: " + (((WItemSlotAccessor)this).getInventory().getInvStack(((WItemSlotAccessor)this).getStartIndex()).getCount() < stackLimit));
        return ((WItemSlotAccessor)this).getInventory().getInvStack(((WItemSlotAccessor)this).getStartIndex()).getCount() < stackLimit;
    }
}
