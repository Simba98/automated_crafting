package net.fabricmc.automated_crafting;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;

public class AutoCrafterController extends CottonCraftingController {

    public AutoCrafterController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 140);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0, 3, 3);
        root.add(itemSlot, 2, 1);

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, 9).setModifiable(false);
        root.add(outputSlot, 6, 2);

        root.add(this.createPlayerInventoryPanel(), 0, 5);

        //this validation must be last!
        root.validate(this);
    }
}