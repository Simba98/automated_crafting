package net.sssubtlety.automated_crafting;

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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.sssubtlety.automated_crafting.inventory.RecipeInventory;
import org.jetbrains.annotations.NotNull;

import static net.sssubtlety.automated_crafting.AutomatedCrafting.NAMESPACE;
import static net.sssubtlety.automated_crafting.AutoCrafterGuiDescription.Measurements.*;

public class AutoCrafterGuiDescription extends SyncedGuiDescription {
    public static final Text NAME = new TranslatableText("block.automated_crafting.auto_crafter");

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
        WPlainPanel root = createRoot(instance);

        if (Config.isSimpleMode()) populateSimple(inventory, root);
        else populateComplex(inventory, root);

        finishSetup(instance, root);

        return instance;
    }

    protected static void populateSimple(Inventory inventory, WPlainPanel root) {
        addLabeledTemplateGrid(root, inventory);
        addInputGrid(root, SIMPLE_INPUT_X, inventory, RecipeInventory.Grid.SIZE);
        addOutputSlot(root, SIMPLE_OUTPUT_X, inventory);
        addLabel(root, ".input", INPUT_LABEL_X);
    }

    protected static void populateComplex(Inventory inventory, WPlainPanel root) {
        addInputGrid(root, COMPLEX_INPUT_X, inventory, 0);
        addOutputSlot(root, COMPLEX_OUTPUT_X, inventory);
    }

    @NotNull
    protected static WPlainPanel createRoot(AutoCrafterGuiDescription instance) {
        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        instance.setRootPanel(root);
        return root;
    }
    
    protected static void addLabeledTemplateGrid(WPlainPanel root, Inventory inventory) {
        root.add(createGrid(inventory, 0), TEMPLATE_X, GRID_Y);
        addLabel(root, ".template", TEMPLATE_LABEL_X);
    }

    protected static void addInputGrid(WPlainPanel root, int x, Inventory inventory, int startIndex) {
        root.add(
                createGrid(inventory, startIndex)
                        .setInsertingAllowed(!Config.isSimpleMode()),
                x,
                GRID_Y
        );
    }

    @NotNull
    private static WItemSlot createGrid(Inventory inventory, int startIndex) {
        return WItemSlot.of(
                inventory,
                startIndex,
                RecipeInventory.Grid.WIDTH,
                RecipeInventory.Grid.HEIGHT
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

    protected static void finishSetup(AutoCrafterGuiDescription instance, WPlainPanel root) {
        root.add(instance.createPlayerInventoryPanel(), 0, PLAYER_INVENTORY_Y);
        // this validation must be last!
        root.validate(instance);
    }

    protected static void addLabel(WPlainPanel root, String s, int inputLabelX) {
        root.add(
                new WLabel(new TranslatableText("label." + NAMESPACE + s))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER),
                inputLabelX,
                GRID_LABEL_Y
        );
    }
    
//    public AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory) {
//        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.world, playerInventory.player.getBlockPos()));
//
//        WPlainPanel root = new WPlainPanel();
//        root.setInsets(Insets.ROOT_PANEL);
//        setRootPanel(root);
//
//        WItemSlot template = WItemSlot.of(blockInventory,
//                AutoCrafterBlockEntity.Slots.TEMPLATE_START,
//                RecipeInventory.Grid.WIDTH,
//                RecipeInventory.Grid.HEIGHT
//        );
//        root.add(template, getTemplateX(), GRID_PIXELS + CRAFT_GRID_Y_OFFSET);
//
//        WItemSlot input;
//        input = WItemSlot.of(blockInventory, AutoCrafterBlockEntity.Slots.INPUT_START, RecipeInventory.Grid.WIDTH, RecipeInventory.Grid.HEIGHT);
//        input.setInsertingAllowed(false);
//        root.add(input, getInputX(), GRID_PIXELS + CRAFT_GRID_Y_OFFSET);
//
//        WItemSlot output = WItemSlot.outputOf(blockInventory, AutoCrafterBlockEntity.Slots.OUTPUT_SLOT).setInsertingAllowed(false);
//        root.add(output, getOutputX(), 2 * GRID_PIXELS);
//
//        WLabel templateLabel = new WLabel(new TranslatableText("label." + NAMESPACE + ".template"));
//        templateLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        root.add(templateLabel, GRID_PIXELS, 4 * GRID_PIXELS);
//        WLabel inputLabel = new WLabel(new TranslatableText("label." + NAMESPACE + ".input"));
//        inputLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        root.add(inputLabel, 5 * GRID_PIXELS, 4 * GRID_PIXELS);
//
//        root.add(this.createPlayerInventoryPanel(), 0, 5 * GRID_PIXELS);
//
//        //this validation must be last!
//        root.validate(this);
//    }
//
//    public AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
//        super(Registrar.SCREEN_HANDLER_TYPE, syncId, playerInventory,
//                ExposedSyncedGuiDescription.getBlockInventory(context, AutoCrafterBlockEntity.Slots.OUTPUT_SLOT),
//                ExposedSyncedGuiDescription.getBlockPropertyDelegate(context)
//        );
//    }

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