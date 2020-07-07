package disabled_java;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.sssubtlety.automated_crafting.mixin.CraftingScreenHandlerAccessor;

import java.util.Iterator;
import java.util.LinkedList;

public class OLDAutoCrafterBlockEntity extends LootableContainerBlockEntity implements SidedInventory, AutoCrafterSharedData, NamedScreenHandlerFactory {
    private final CraftingInventoryWithOutput craftingInventory;
    private Recipe<CraftingInventory> recipeCache;
    private static final GuiConstructor<AbstractAutoCrafterGuiDescription> guiConstructor = SIMPLE_MODE ?
            (syncId, playerInventory, _world, _pos) -> (new AutoCrafterSimpleGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos))):
            (syncId, playerInventory, _world, _pos) -> (new AutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(_world, _pos)));


    private static final LinkedList<OLDAutoCrafterBlockEntity> allInstances = new LinkedList<>();

    public OLDAutoCrafterBlockEntity() {
        super(AutomatedCraftingInit.AUTO_CRAFTER_BLOCK_ENTITY);
        craftingInventory = new CraftingInventoryWithOutput(GRID_WIDTH, GRID_HEIGHT, SIMPLE_MODE ? 2 : 1, SIMPLE_MODE);
        recipeCache = null;
    }

    public static void untrackInstance(OLDAutoCrafterBlockEntity blockEntity) {
        allInstances.remove(blockEntity);
    }

    public static void trackInstance(OLDAutoCrafterBlockEntity blockEntity) {
        allInstances.push(blockEntity);
    }

    // Serialize the BlockEntity
    public CompoundTag toTag(CompoundTag tag) {
        // Save the current value of the number to the tag
        Inventories.toTag(tag, ((CraftingInventoryAccessor)this.craftingInventory).getInventory());
        return super.toTag(tag);
    }

    // Deserialize the BlockEntity
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, ((CraftingInventoryAccessor)this.craftingInventory).getInventory());
    }

    public DefaultedList<ItemStack> getInventory() {
        return ((CraftingInventoryAccessor)this.craftingInventory).getInventory();
    }

    public void tryCraft() {
        Recipe<CraftingInventory> recipe = getRecipe();//this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
        if(recipe != null) {
            if(tryOutput(recipe.getOutput())) {
                for (int iSlot = SIMPLE_MODE ? size() : 0; iSlot < OUTPUT_SLOT; iSlot++) {
                    this.craftingInventory.removeStack(iSlot, 1);
                }
            }
            else {
                world.syncWorldEvent(1001, pos, 0);
            }
        }
        else {
            world.syncWorldEvent(1001, pos, 0);
//            System.out.println("tryCraft found no valid recipe. ");
        }
    }

    private boolean tryOutput(ItemStack output) {
        if (SIMPLE_MODE && !recipeCache.matches(getIsolatedInputInv(), world)) {
//            System.out.println("tryOutput found insufficient resources. ");

            return false;
        }
        ItemStack oldOutput = this.getInventory().get(OUTPUT_SLOT);
        if (oldOutput.isEmpty()) {
            this.setStack(OUTPUT_SLOT, output.copy());
            return true;
        } else if (output.isItemEqual(oldOutput) && oldOutput.getMaxCount() > oldOutput.getCount() + output.getCount()){
            //outputs are same item and output can fit in stack
            oldOutput.increment(output.getCount());
            return true;
        }
//        System.out.println("tryOutput found no space in output slot. ");
        return false;
    }

    private CraftingInventory getIsolatedInputInv() {
        CraftingInventory tempInventory = ((CraftingScreenHandlerAccessor) (new CraftingScreenHandler(0, new PlayerInventory(null)))).getInput();

        for(int slot = size(); slot < OUTPUT_SLOT; slot++)
        {
            tempInventory.setStack(slot - size(), getInvStackList().get(slot));
        }
        return tempInventory;
    }

    private Recipe<CraftingInventory> getRecipe() {
        if(recipeCache == null) {
            recipeCache = this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
//            if(recipeCache == null) {
//                System.out.println("getRecipe replaced null cache with null. ");
//            } else {
//                System.out.println("getRecipe replaced null cache with new recipe for: " + recipeCache.getOutput().getItem().getTranslationKey());
//            }
        }
        else if(!recipeCache.matches(this.craftingInventory, world)) {
//            System.out.println("getRecipe replacing miss-matched cache recipe for: " + recipeCache.getOutput().getItem().getTranslationKey());
            recipeCache = this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
//            if(recipeCache == null) {
//                System.out.println("getRecipe replaced miss-matched cache with null");
//            } else {
//                System.out.println("getRecipe replaced miss-matched cache with new recipe for: " + recipeCache.getOutput().getItem().getTranslationKey());
//            }
        }
//        else {
//            System.out.println("getRecipe found cached recipe matches. ");
//        }
        return recipeCache;
    }

    public static void clearRecipeCaches() {
//        System.out.println("clearRecipeCaches called with allInstances.size() = " + allInstances.size());
        for (Iterator<OLDAutoCrafterBlockEntity> iterator = allInstances.iterator(); iterator.hasNext();) {
            OLDAutoCrafterBlockEntity instance = iterator.next();
            if (instance == null) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            } else {
                instance.recipeCache = null;
            }
        }
    }

    /**
     * start of SidedInventory implementations
     */
    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getInventory().size()];
        int firstAvailableSlot = SIMPLE_MODE ? size() : 0;

        // Create an array of indices of slots that can be inserted into
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        //nothing external can insert into output slot
        if (slot == OUTPUT_SLOT) {
            return false;
        }

        if(SIMPLE_MODE) {
            return slot >= size() && this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot - size()).isItemEqual(stack);
        }
        else {
            return this.getInventory().get(slot).isEmpty();
        }
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        //players and hoppers can take from output,
        //hoppers can't take from input slots
//        if(slot == OUTPUT_SLOT || direction == null) { return true; }
//        return false;
        if(SIMPLE_MODE) {
            return slot == OUTPUT_SLOT;
        } else {
            return true;
        }
    }
    /**
     * end of SidedInventory implementations
     * start of LootableContainerBlockEntity implementations
     */
    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.getInventory();
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        ((CraftingInventoryAccessor)this.craftingInventory).setInventory(list);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("block.automated_crafting.auto_crafter", new Object[0]);
    }

//    @Override
//    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
//        BlockState state = this.world.getBlockState(pos);
//        NamedScreenHandlerFactory factory = state.getBlock().createScreenHandlerFactory(state, world, pos);
//        if(factory != null) {
//            return factory.createMenu(syncId, playerInventory, playerInventory.player);
//        } else {
//            throw new IllegalStateException("AutoCrafterBlockEntity's createScreenHandler could not create factory. ");
//        }
//    }

//    @Override
//    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
//        return new AutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.create(world, pos));
//    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
//        return new AutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
        return guiConstructor.construct(syncId, playerInventory, world, pos);
    }

//    @Override
//    public Text getDisplayName() {
//        // Using the block name as the screen title
//        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
//    }
//
//    @Override
//    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
//        return new AutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.create(world, pos));
//    }

//    @Override
//    public void setInvStack(int slot, ItemStack stack) {
//        this.craftingInventory.setInvStack(slot, stack);
//    }
    /**
     * end of LootableContainerBlockEntity implementations
     * start of Inventory implementations
     */
    @Override
    public int size() {
        return craftingInventory.size();
    }


    /**
     * end of Inventory implementations
     */

    @FunctionalInterface
    private interface GuiConstructor<C extends AbstractAutoCrafterGuiDescription> {
        C construct(int syncId, PlayerInventory playerInventory, World world, BlockPos pos);
    }

}
