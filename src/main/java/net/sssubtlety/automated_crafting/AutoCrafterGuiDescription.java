package net.sssubtlety.automated_crafting;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class AutoCrafterGuiDescription extends SyncedGuiDescription implements AutoCrafterSharedData {
    private final static int GRID_PIXELS = 18;
    private final static int CRAFT_GRID_Y_OFFSET = -4;

    public AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(AutomatedCraftingInit.AUTO_CRAFTER_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, 19), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        /* Let root auto-fit contents */
//        root.setSize(140, 80);

        WItemSlot inputSlot = WItemSlot.of(blockInventory, SIMPLE_MODE ? (GRID_WIDTH * GRID_HEIGHT)  : 0, GRID_WIDTH , GRID_HEIGHT);
        if (SIMPLE_MODE) { inputSlot.setInsertingAllowed(false); }
        root.add(inputSlot, (SIMPLE_MODE ? 4 : 2) * GRID_PIXELS, GRID_PIXELS + CRAFT_GRID_Y_OFFSET);

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, OUTPUT_SLOT).setInsertingAllowed(false);
        root.add(outputSlot, (6 * GRID_PIXELS - 5) + (SIMPLE_MODE ? 2 * GRID_PIXELS : 0), 2 * GRID_PIXELS);//103

        if(SIMPLE_MODE) {
            WItemSlot templateSlot;
            templateSlot = WItemSlot.of(blockInventory, 0, GRID_WIDTH, GRID_HEIGHT);
            root.add(templateSlot, 0, GRID_PIXELS + CRAFT_GRID_Y_OFFSET);

            WLabel templateLabel = new WLabel("Template");
            templateLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
            root.add(templateLabel, GRID_PIXELS, 4 * GRID_PIXELS);
            WLabel inputLabel = new WLabel("Input");
            inputLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
            root.add(inputLabel, 5 * GRID_PIXELS, 4 * GRID_PIXELS);
        }

        root.add(this.createPlayerInventoryPanel(), 0, 5 * GRID_PIXELS);

        //this validation must be last!
        root.validate(this);
    }
}