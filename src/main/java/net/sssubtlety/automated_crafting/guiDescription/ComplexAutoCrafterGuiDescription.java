package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class ComplexAutoCrafterGuiDescription extends AbstractAutoCrafterGuiDescription {
    public ComplexAutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }

    protected WItemSlot getInputSlot() {
        return WItemSlot.of(blockInventory, 0, GRID_WIDTH , GRID_HEIGHT);
    }

    protected int getInputX() {
        return 2 * GRID_PIXELS;
    }

    protected int getOutputX() {
        return (6 * GRID_PIXELS - 5);
    }

    protected void optionalAddition(WPlainPanel root) { }
}