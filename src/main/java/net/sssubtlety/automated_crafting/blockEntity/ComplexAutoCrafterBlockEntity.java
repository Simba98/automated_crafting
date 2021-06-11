package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.ComplexAutoCrafterGuiDescription;

public class ComplexAutoCrafterBlockEntity extends AbstractAutoCrafterBlockEntity {
    public ComplexAutoCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

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
    protected boolean insertCheck(int slot, ItemStack stack) {
        return this.getInventory().get(slot).isEmpty();
    }

    @Override
    protected boolean extractCheck(int slot, ItemStack stack) {
        return true;
    }
}
