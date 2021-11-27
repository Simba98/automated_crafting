package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.SimpleAutoCrafterGuiDescription;

public class SimpleAutoCrafterBlockEntity extends AbstractAutoCrafterBlockEntity {
    public SimpleAutoCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected GuiConstructor<AbstractAutoCrafterGuiDescription> getGuiConstructor() {
        return (syncId, playerInventory, _world, _pos) -> (new SimpleAutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos)));
    }

    @Override
    protected int getApparentInvCount() {
        return 2;
    }

    @Override
    protected boolean optionalOutputCheck() {
        return !recipeCache.matches(this.getIsolatedInputInv(), world);
    }

    @Override
    protected boolean insertCheck(int slot, ItemStack stack) {
        return isInputSlot(slot) &&
                this.getInventory().get(slot).isEmpty() &&
                this.craftingInventory.isValid(slot, stack) &&
                inputSlotMatchesTemplate(slot, stack);
    }

    @Override
    protected boolean extractCheck(int slot, ItemStack stack) {
        return slot == AutoCrafterBlockEntity.Slots.OUTPUT_SLOT ||
        isInputSlot(slot) && !inputSlotMatchesTemplate(slot, stack);
    }

    protected boolean isInputSlot(int slot) {
        return slot >= AutoCrafterBlockEntity.Slots.INPUT_START && slot < AutoCrafterBlockEntity.Slots.OUTPUT_SLOT;
    }

    protected boolean inputSlotMatchesTemplate(int slot, ItemStack stack) {
        ItemStack templateStack = this.getInventory().get(slot - AutoCrafterBlockEntity.Slots.INPUT_START);
        return templateStack.isItemEqual(stack) && ItemStack.areNbtEqual(templateStack, stack);
    }
}
