package net.sssubtlety.automated_crafting;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

import static javax.swing.text.StyleConstants.Alignment;

public class AutoCrafterGuiDescription extends SyncedGuiDescription implements AutoCrafterSharedData {

    public AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(140, 140);

        WItemSlot inputSlot = WItemSlot.of(blockInventory, SIMPLE_MODE ? GRID_WIDTH * GRID_HEIGHT : 0, GRID_WIDTH, GRID_HEIGHT);
        if (SIMPLE_MODE) { inputSlot.setInsertingAllowed(false); }
        root.add(inputSlot, SIMPLE_MODE ? 4 : 2, 1);

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, OUTPUT_SLOT).setInsertingAllowed(false);
        root.add(outputSlot, SIMPLE_MODE ? 8 : 6, 2);

        if(SIMPLE_MODE) {
            WItemSlot templateSlot;
            templateSlot = WItemSlot.of(blockInventory, 0, GRID_WIDTH, GRID_HEIGHT);
            root.add(templateSlot, 0, 1);

            WLabel templateLabel = new WLabel("Template");
            templateLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
            root.add(templateLabel, 1, 0);
            WLabel inputLabel = new WLabel("Input");
            inputLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
            root.add(inputLabel, 5, 0);
        }

        root.add(this.createPlayerInventoryPanel(), 0, 5);

        //this validation must be last!
        root.validate(this);
    }

////    @Override
//    public int getCraftingResultSlotIndex() {
//        return AutoCrafterGuiDescription.OUTPUT_SLOT;
//    }
//
////    @Override
//    public int getCraftingWidth() {
//        return AutoCrafterGuiDescription.GRID_WIDTH;
//    }
//
////    @Override
//    public int getCraftingHeight() {
//        return AutoCrafterGuiDescription.GRID_HEIGHT;
//    }
//
////    @Override
//    @Environment(EnvType.CLIENT)
//    public int getCraftingSlotCount() {
//        return AutoCrafterGuiDescription.OUTPUT_SLOT;
//    }
}