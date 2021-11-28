package net.sssubtlety.automated_crafting.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.sssubtlety.automated_crafting.AutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.Config;
import net.sssubtlety.automated_crafting.Registrar;
import net.sssubtlety.automated_crafting.inventory.CraftingView;
import org.jetbrains.annotations.NotNull;

import static net.sssubtlety.automated_crafting.AutomatedCrafting.NAMESPACE;
import static net.sssubtlety.automated_crafting.gui.AutoCrafterGuiDescription.Measurements.*;

public class AutoCrafterGuiDescription extends SyncedGuiDescription {
    public static final ArrayPropertyDelegate EMPTY_DELEGATE = new ArrayPropertyDelegate(0);

    public static void init() { }
    
    protected AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Registrar.SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, EMPTY_DELEGATE);
    }

    public static AutoCrafterGuiDescription create(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        return create(syncId, playerInventory, getBlockInventory(context, AutoCrafterBlockEntity.Slots.INVENTORY_SIZE));
    }

    public static AutoCrafterGuiDescription create(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        final AutoCrafterGuiDescription instance = new AutoCrafterGuiDescription(syncId, playerInventory, inventory);
        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        instance.setRootPanel(root);

        if (Config.isSimpleMode()) populateSimple(inventory, root);
        else populateComplex(inventory, root);

        root.add(instance.createPlayerInventoryPanel(), 0, PLAYER_INVENTORY_Y);
        // this validation must be last!
        root.validate(instance);

        return instance;
    }

    protected static void populateSimple(Inventory inventory, WPlainPanel root) {
        root.add(createGrid(inventory, 0), TEMPLATE_X, GRID_Y);
        addLabel(root, ".template", TEMPLATE_LABEL_X);
        addSimpleInputGrid(root, inventory);
        addOutputSlot(root, SIMPLE_OUTPUT_X, inventory);
    }

    protected static void populateComplex(Inventory inventory, WPlainPanel root) {
        root.add(
                createGrid(inventory, CraftingView.Grid.SIZE),
                Measurements.COMPLEX_INPUT_X,
                GRID_Y
        );
        addOutputSlot(root, COMPLEX_OUTPUT_X, inventory);
    }

    protected static void addSimpleInputGrid(WPlainPanel root, Inventory inventory) {
//        WTemplatedSlot grid = WTemplatedSlot.of(
//                inventory, CraftingView.Grid.SIZE,
//                inventory, -CraftingView.Grid.SIZE,
//                CraftingView.Grid.WIDTH,
//                CraftingView.Grid.HEIGHT
//        );

        WItemSlot grid = createGrid(inventory, CraftingView.Grid.SIZE);

        root.add(
                grid,
                Measurements.SIMPLE_INPUT_X,
                GRID_Y
        );

        addLabel(root, ".input", INPUT_LABEL_X);
    }

    @NotNull
    private static WItemSlot createGrid(Inventory inventory, int startIndex) {
        return WItemSlot.of(
                inventory,
                startIndex,
                CraftingView.Grid.WIDTH,
                CraftingView.Grid.HEIGHT
        );
    }

    protected static void addOutputSlot(WPlainPanel root, int outputX, Inventory inventory) {
        root.add(
                WItemSlot.outputOf(inventory, AutoCrafterBlockEntity.Slots.OUTPUT_SLOT)
                        .setInsertingAllowed(false),
                outputX,
                OUTPUT_Y
        );
    }

    protected static void addLabel(WPlainPanel root, String s, int inputLabelX) {
        root.add(
                new WLabel(new TranslatableText("label." + NAMESPACE + s))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER),
                inputLabelX,
                GRID_LABEL_Y
        );
    }

    public ScreenHandlerType<?> getType() {
        return Registrar.SCREEN_HANDLER_TYPE;
    }
    
    public interface Measurements {
        int SLOT_WIDTH = 18;
        int COMPLEX_OUTPUT_X = 6 * SLOT_WIDTH - 5;
        int COMPLEX_INPUT_X = 2 * SLOT_WIDTH;
        int SIMPLE_INPUT_X = 4 * SLOT_WIDTH;
        int SIMPLE_OUTPUT_X = 8 * SLOT_WIDTH - 5;
        int OUTPUT_Y = 2 * SLOT_WIDTH;
        int GRID_LABEL_Y = 4 * SLOT_WIDTH;
        int TEMPLATE_LABEL_X = SLOT_WIDTH;
        int INPUT_LABEL_X = 5 * SLOT_WIDTH;
        int PLAYER_INVENTORY_Y = 5 * SLOT_WIDTH;
        int GRID_Y_OFFSET = -4;
        int GRID_Y = SLOT_WIDTH + GRID_Y_OFFSET;
        int TEMPLATE_X = 0;
    } 
}