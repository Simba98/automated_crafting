package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class ExposedSyncedGuiDescription extends SyncedGuiDescription {
    public ExposedSyncedGuiDescription(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, @Nullable Inventory blockInventory, @Nullable PropertyDelegate propertyDelegate) {
        super(type, syncId, playerInventory, blockInventory, propertyDelegate);
    }

    @Override
    public Slot addSlot(Slot slot) {
        return super.addSlot(slot);
    }

    @Override
    public Property addProperty(Property property) {
        return super.addProperty(property);
    }

    @Override
    public void addProperties(PropertyDelegate propertyDelegate) {
        super.addProperties(propertyDelegate);
    }

    @Override
    public void dropInventory(PlayerEntity player, Inventory inventory) {
        super.dropInventory(player, inventory);
    }

    @Override
    public boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    @Override
    public void endQuickCraft() {
        super.endQuickCraft();
    }

    public Inventory getBlockInventory() {
        return this.blockInventory;
    }
}
