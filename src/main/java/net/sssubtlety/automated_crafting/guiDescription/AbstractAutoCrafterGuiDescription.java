package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.sssubtlety.automated_crafting.AutoCrafterSharedData;
import net.sssubtlety.automated_crafting.AutomatedCraftingInit;

public abstract class AbstractAutoCrafterGuiDescription extends SyncedGuiDescription implements AutoCrafterSharedData {
    protected final static int GRID_PIXELS = 18;
    protected final static int CRAFT_GRID_Y_OFFSET = -4;

    public AbstractAutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(AutomatedCraftingInit.AUTO_CRAFTER_SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, SIMPLE_MODE ? 19 : 10), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        /* Let root auto-fit contents */
//        root.setSize(140, 80);

        WItemSlot inputSlot = getInputSlot();
        root.add(inputSlot, getInputX(), GRID_PIXELS + CRAFT_GRID_Y_OFFSET);

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, OUTPUT_SLOT).setInsertingAllowed(false);
        root.add(outputSlot, getOutputX(), 2 * GRID_PIXELS);

//        if(SIMPLE_MODE) {
//            WItemSlot templateSlot;
//            templateSlot = WItemSlot.of(blockInventory, 0, GRID_WIDTH, GRID_HEIGHT);
//            root.add(templateSlot, 0, GRID_PIXELS + CRAFT_GRID_Y_OFFSET);
//
//            WLabel templateLabel = new WLabel("Template");
//            templateLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            root.add(templateLabel, GRID_PIXELS, 4 * GRID_PIXELS);
//            WLabel inputLabel = new WLabel("Input");
//            inputLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            root.add(inputLabel, 5 * GRID_PIXELS, 4 * GRID_PIXELS);
//        }

//        optionalAddition(root);
        if (SIMPLE_MODE) {
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

    protected abstract WItemSlot getInputSlot();

    protected abstract int getInputX();

    protected abstract int getOutputX();

    protected abstract void optionalAddition(WPlainPanel root);
}