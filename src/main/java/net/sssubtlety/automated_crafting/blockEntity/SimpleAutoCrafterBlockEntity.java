package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.sssubtlety.automated_crafting.CraftingInventoryWithoutHandler;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.SimpleAutoCrafterGuiDescription;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.*;

public class SimpleAutoCrafterBlockEntity extends AbstractAutoCrafterBlockEntity {
    public SimpleAutoCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

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
        return slot == OUTPUT_SLOT ||
        isInputSlot(slot) && !inputSlotMatchesTemplate(slot, stack);
    }

    protected boolean isInputSlot(int slot) {
        return slot >= FIRST_INPUT_SLOT && slot < OUTPUT_SLOT;
    }

    protected boolean inputSlotMatchesTemplate(int slot, ItemStack stack) {
        ItemStack templateStack = this.getInventory().get(slot - FIRST_INPUT_SLOT);
        return templateStack.isItemEqual(stack) && ItemStack.areNbtEqual(templateStack, stack);
    }
}
