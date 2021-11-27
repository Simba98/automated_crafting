package net.sssubtlety.automated_crafting.guiDescription;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.sssubtlety.automated_crafting.Config;
import net.sssubtlety.automated_crafting.Registrar;
import net.sssubtlety.automated_crafting.blockEntity.ArrayInventory;
import net.sssubtlety.automated_crafting.blockEntity.AutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.inventory.RecipeInventory;
import org.jetbrains.annotations.NotNull;

import static net.sssubtlety.automated_crafting.AutomatedCrafting.NAMESPACE;
import static net.sssubtlety.automated_crafting.guiDescription.AutoCrafterGuiDescription.Measurements.*;

public class AutoCrafterGuiDescription extends SyncedGuiDescription {
    public static final Text NAME = new TranslatableText("block.automated_crafting.auto_crafter");

    public static final ArrayPropertyDelegate EMPTY_DELEGATE = new ArrayPropertyDelegate(0);

    public static void init() { }
    
    protected AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Registrar.SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, EMPTY_DELEGATE);
//                ExposedSyncedGuiDescription.getBlockPropertyDelegate(
//                        ScreenHandlerContext.create(playerInventory.player.world, playerInventory.player.getBlockPos())
//                )

    }

    protected AutoCrafterGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Registrar.SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, AutoCrafterBlockEntity.Slots.INVENTORY_SIZE), getBlockPropertyDelegate(context));
    }

    public static AutoCrafterGuiDescription create(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
//        AutoCrafterGuiDescription instance = new AutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.EMPTY);
        return create(syncId, playerInventory, getBlockInventory(context, AutoCrafterBlockEntity.Slots.INVENTORY_SIZE));
        //(AutoCrafterBlockEntity) getBlockInventory(ScreenHandlerContext.EMPTY, AutoCrafterBlockEntity.Slots.INVENTORY_SIZE)
    }

    public static AutoCrafterGuiDescription create(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        return Config.isSimpleMode() ? 
                createSimple(syncId, playerInventory, inventory) :
                createComplex(syncId, playerInventory, inventory);
    }
//    input = createGrid(inventory, RecipeInventory.Grid.SIZE);


//    public static AutoCrafterGuiDescription create(int syncId, PlayerInventory playerInventory, AutoCrafterBlockEntity autoCrafterBlockEntity) {
//        return Config.isSimpleMode() ?
//                createSimple(syncId, playerInventory, autoCrafterBlockEntity) :
//                createComplex(syncId, playerInventory, autoCrafterBlockEntity);
//    }
    
//    protected static AutoCrafterGuiDescription createSimple(int syncId, PlayerInventory playerInventory, AutoCrafterBlockEntity autoCrafterBlockEntity) {
//        AutoCrafterGuiDescription instance = new AutoCrafterGuiDescription(syncId, playerInventory, autoCrafterBlockEntity);
//
//        WPlainPanel root = createRoot(instance);
//
//        populateSimple(autoCrafterBlockEntity, root);
//
//        addLabel(root, ".input", INPUT_LABEL_X);
//
//        finishSetup(instance, root);
//
//        return instance;
//    }

    protected static AutoCrafterGuiDescription createSimple(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        AutoCrafterGuiDescription instance = new AutoCrafterGuiDescription(syncId, playerInventory, inventory);

        WPlainPanel root = createRoot(instance);

//        if (inventory instanceof AutoCrafterBlockEntity autoCrafterBlockEntity)
//            populateSimple(root, autoCrafterBlockEntity.templateInventory, autoCrafterBlockEntity.inputInventory, 0, autoCrafterBlockEntity.output);
//        else
            populateSimple(root, inventory, inventory, RecipeInventory.Grid.SIZE, inventory.getStack(AutoCrafterBlockEntity.Slots.INVENTORY_SIZE));

        addLabel(root, ".input", INPUT_LABEL_X);

        finishSetup(instance, root);

        return instance;
    }

    protected static void populateSimple(WPlainPanel root, Inventory templateInventory, Inventory inputInventory, int inputStart, ItemStack outputStack) {
        addLabeledTemplateGrid(root, templateInventory);
        addInputGrid(root, SIMPLE_INPUT_X, inputInventory, inputStart);
        addOutputSlot(root, SIMPLE_OUTPUT_X, outputStack);
    }

    protected static AutoCrafterGuiDescription createComplex(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        AutoCrafterGuiDescription instance = new AutoCrafterGuiDescription(syncId, playerInventory, inventory);

        WPlainPanel root = createRoot(instance);

//        if (inventory instanceof AutoCrafterBlockEntity autoCrafterBlockEntity)
//            populateComplex(root, autoCrafterBlockEntity.inputInventory, autoCrafterBlockEntity.output);
//        else
            populateComplex(root, inventory, inventory.getStack(AutoCrafterBlockEntity.Slots.INVENTORY_SIZE));

        finishSetup(instance, root);

        return instance;
    }

    protected static void populateComplex(WPlainPanel root, Inventory inputInventory, ItemStack outputStack) {
        addInputGrid(root, COMPLEX_INPUT_X, inputInventory, 0);
        addOutputSlot(root, COMPLEX_OUTPUT_X, outputStack);
    }

    @NotNull
    protected static WPlainPanel createRoot(AutoCrafterGuiDescription instance) {
        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        instance.setRootPanel(root);
        return root;
    }
    
    protected static void addLabeledTemplateGrid(WPlainPanel root, Inventory inventory) {
        final WItemSlot template = createGrid(inventory, 0);

        root.add(template, TEMPLATE_X, GRID_Y);

        addLabel(root, ".template", TEMPLATE_LABEL_X);
    }

    protected static void addInputGrid(WPlainPanel root, int x, Inventory inventory, int startIndex) {
        WItemSlot input;

        input = createGrid(inventory, startIndex);

        input.setInsertingAllowed(!Config.isSimpleMode());
        root.add(input, x, GRID_Y);
    }

    @NotNull
    private static WItemSlot createGrid(Inventory inventory, int startIndex) {
        WItemSlot grid;
        grid = WItemSlot.of(
                inventory,
                startIndex,
                RecipeInventory.Grid.WIDTH,
                RecipeInventory.Grid.HEIGHT
        );
        return grid;
    }

    protected static void addOutputSlot(WPlainPanel root, int outputX, ItemStack output) {
        WItemSlot outputSlot = WItemSlot.outputOf(output == null ? new SimpleInventory(1) : new ArrayInventory(output), 0).setInsertingAllowed(false);
        root.add(outputSlot, outputX, OUTPUT_Y);
    }

    protected static void finishSetup(AutoCrafterGuiDescription instance, WPlainPanel root) {
        root.add(instance.createPlayerInventoryPanel(), 0, PLAYER_INVENTORY_Y);

        //this validation must be last!
        root.validate(instance);
    }

    protected static void addLabel(WPlainPanel root, String s, int inputLabelX) {
        WLabel inputLabel = new WLabel(new TranslatableText("label." + NAMESPACE + s));
        inputLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(inputLabel, inputLabelX, GRID_LABEL_Y);
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