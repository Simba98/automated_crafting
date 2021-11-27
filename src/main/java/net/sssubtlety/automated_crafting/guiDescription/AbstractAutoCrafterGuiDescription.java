package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.sssubtlety.automated_crafting.Config;
import net.sssubtlety.automated_crafting.Registrar;
import net.sssubtlety.automated_crafting.blockEntity.AutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.inventory.RecipeInventory;

public abstract class AbstractAutoCrafterGuiDescription extends SyncedGuiDescription {
    protected final static int GRID_PIXELS = 18;
    protected final static int CRAFT_GRID_Y_OFFSET = -4;

    public AbstractAutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Registrar.SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, Config.isSimpleMode() ? 19 : 10), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        WItemSlot templateSlot = WItemSlot.of(blockInventory, AutoCrafterBlockEntity.Slots.TEMPLATE_START, RecipeInventory.Grid.WIDTH, RecipeInventory.Grid.HEIGHT);
        root.add(templateSlot, getTemplateX(), GRID_PIXELS + CRAFT_GRID_Y_OFFSET);

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, AutoCrafterBlockEntity.Slots.OUTPUT_SLOT).setInsertingAllowed(false);
        root.add(outputSlot, getOutputX(), 2 * GRID_PIXELS);

        optionalAddition(root);

        root.add(this.createPlayerInventoryPanel(), 0, 5 * GRID_PIXELS);

        //this validation must be last!
        root.validate(this);
    }

    protected void optionalAddition(WPlainPanel root) { }

    protected abstract int getTemplateX();

    protected abstract int getInputX();

    protected abstract int getOutputX();
}