package net.sssubtlety.automated_crafting;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;

public class AutoCrafterController extends CottonCraftingController {
    public static final int INVENTORY_SIZE = 10;
    public static final int GRID_HEIGHT = 3;
    public static final int GRID_WIDTH = 3;
    public static final int OUTPUT_SLOT = INVENTORY_SIZE - 1;

    public AutoCrafterController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 140);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0, GRID_WIDTH, GRID_HEIGHT);
        root.add(itemSlot, 2, 1);

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, OUTPUT_SLOT).setInsertingAllowed(false);
        root.add(outputSlot, 6, 2);

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