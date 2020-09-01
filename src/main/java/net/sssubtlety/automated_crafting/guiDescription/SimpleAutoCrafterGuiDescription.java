package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.GRID_HEIGHT;
import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.GRID_WIDTH;

public class SimpleAutoCrafterGuiDescription extends AbstractAutoCrafterGuiDescription {

    public SimpleAutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
    }

//    protected WItemSlot getInputSlot() {
////        WItemSlot inputSlot = WItemSlot.of(blockInventory, GRID_WIDTH * GRID_HEIGHT, GRID_WIDTH , GRID_HEIGHT);
//        WItemSlot inputSlot = WItemSlot.of(blockInventory, 0, GRID_WIDTH , GRID_HEIGHT);
//        inputSlot.setInsertingAllowed(false);
//        return inputSlot;
//    }

    @Override
    protected void optionalInputSlotAdjustment(WItemSlot inputSlot) {
        inputSlot.setInsertingAllowed(false);

    }

    protected int getTemplateX() {
        return 0;
    }

    @Override
    protected int getInputX() {
        return 4 * GRID_PIXELS;
    }

    protected int getOutputX() {
        return 8 * GRID_PIXELS - 5;
    }

//    protected void optionalAddition(WPlainPanel root) {
//        WItemSlot templateSlot;
//        templateSlot = WItemSlot.of(blockInventory, 0, GRID_WIDTH, GRID_HEIGHT);
//        root.add(templateSlot, 0, GRID_PIXELS + CRAFT_GRID_Y_OFFSET);
//
//        WLabel templateLabel = new WLabel("Template");
//        templateLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        root.add(templateLabel, GRID_PIXELS, 4 * GRID_PIXELS);
//        WLabel inputLabel = new WLabel("Input");
//        inputLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        root.add(inputLabel, 5 * GRID_PIXELS, 4 * GRID_PIXELS);
//    }
}