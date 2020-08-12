package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.SimpleAutoCrafterGuiDescription;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.OUTPUT_SLOT;

public class SimpleAutoCrafterBlockEntity extends AbstractAutoCrafterBlockEntity {
//    private static final GuiConstructor<AbstractAutoCrafterGuiDescription> guiConstructor = SIMPLE_MODE ?
//            (syncId, playerInventory, _world, _pos) -> (new AutoCrafterSimpleGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos))):

    @Override
    protected GuiConstructor<AbstractAutoCrafterGuiDescription> getGuiConstructor() {
        return (syncId, playerInventory, _world, _pos) -> (new SimpleAutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos)));
    }

    @Override
    protected int getInvMaxStackCount() {
        return 1;
    }

    @Override
    protected int getApparentInvCount() {
        return 2;
    }

    @Override
    public int getInputSlotInd() {
        return size();
    }

    @Override
    protected boolean optionalOutputCheck() {
        return !recipeCache.matches(getIsolatedInputInv(), world);
    }

    @Override
    protected boolean insertCheck(int slot, ItemStack stack) {
        return isInputSlot(slot) &&
                this.getInventory().get(slot).isEmpty() &&
//                stack.getCount() == 1 &&
//                this.craftingInventory.isValid(slot, stack) &&
                inputSlotMatchesTemplate(slot, stack);
    }

    @Override
    protected boolean extractCheck(int slot, ItemStack stack) {
        return slot == OUTPUT_SLOT ||
        isInputSlot(slot) && !inputSlotMatchesTemplate(slot, stack);
    }

    protected boolean isInputSlot(int slot) {
        return slot != OUTPUT_SLOT && slot >= size();
    }

    protected boolean inputSlotMatchesTemplate(int slot, ItemStack stack) {
        return this.getInventory().get(slot - size()).isItemEqual(stack);
    }
}
