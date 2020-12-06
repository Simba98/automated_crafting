package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.GRID_HEIGHT;
import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.GRID_WIDTH;

public class ComplexAutoCrafterGuiDescription extends AbstractAutoCrafterGuiDescription {
    public ComplexAutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }

    @Override
    protected int getTemplateX() {
        return 2 * GRID_PIXELS;
    }

    @Override
    protected int getInputX() {
        return getTemplateX();
    }

    @Override
    protected int getOutputX() {
        return (6 * GRID_PIXELS - 5);
    }
}