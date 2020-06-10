package net.sssubtlety.automated_crafting;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;

public class AutoCrafterController extends CottonCraftingController implements AutoCrafterSharedData {

    public AutoCrafterController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

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
            templateLabel.setAlignment(Alignment.CENTER);
            root.add(templateLabel, 1, 0);
            WLabel inputLabel = new WLabel("Input");
            inputLabel.setAlignment(Alignment.CENTER);
            root.add(inputLabel, 5, 0);
        }

        root.add(this.createPlayerInventoryPanel(), 0, 5);

        //this validation must be last!
        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return AutoCrafterController.OUTPUT_SLOT;
    }

    @Override
    public int getCraftingWidth() {
        return AutoCrafterController.GRID_WIDTH;
    }

    @Override
    public int getCraftingHeight() {
        return AutoCrafterController.GRID_HEIGHT;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getCraftingSlotCount() {
        return AutoCrafterController.OUTPUT_SLOT;
    }
}