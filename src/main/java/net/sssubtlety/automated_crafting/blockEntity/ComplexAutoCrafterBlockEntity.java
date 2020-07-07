package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.ComplexAutoCrafterGuiDescription;

public class ComplexAutoCrafterBlockEntity extends AbstractAutoCrafterBlockEntity {
//    private static final GuiConstructor<AbstractAutoCrafterGuiDescription> guiConstructor = SIMPLE_MODE ?
//            (syncId, playerInventory, _world, _pos) -> (new AutoCrafterSimpleGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos))):

    @Override
    protected AbstractAutoCrafterBlockEntity.GuiConstructor<AbstractAutoCrafterGuiDescription> getGuiConstructor() {
        return (syncId, playerInventory, _world, _pos) -> (new ComplexAutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos)));
    }

    @Override
    protected int getInvMaxStackCount() {
        return 1;
    }

    @Override
    protected int getApparentInvCount() {
        return 1;
    }

    @Override
    public int getInputSlotInd() {
        return 0;
    }

    @Override
    protected boolean optionalOutputCheck() {
        return false;
    }

    @Override
    protected boolean insertCheck(int slot, ItemStack stack) {
        return this.getInventory().get(slot).isEmpty();
    }

    @Override
    protected boolean extractCheck(int slot) {
        return true;
    }
}
