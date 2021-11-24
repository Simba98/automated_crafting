package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.sssubtlety.automated_crafting.AutoCrafterSharedData;
import net.sssubtlety.automated_crafting.AutomatedCrafting;
import net.sssubtlety.automated_crafting.CraftingInventoryWithOutput;
import net.sssubtlety.automated_crafting.CraftingInventoryWithoutHandler;
import net.sssubtlety.automated_crafting.block.AutoCrafterBlock;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;

import java.util.function.Supplier;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.*;

public abstract class AbstractAutoCrafterBlockEntity extends LootableContainerBlockEntity implements SidedInventory, NamedScreenHandlerFactory {
    private static final int PRE_FIRST_INPUT_SLOT = FIRST_INPUT_SLOT - 1;
    protected final CraftingInventoryWithOutput craftingInventory;
    private int currentKey;
    protected Recipe<CraftingInventory> recipeCache;

    protected abstract GuiConstructor<AbstractAutoCrafterGuiDescription> getGuiConstructor();
    protected abstract int getInvMaxStackCount();
    protected abstract int getApparentInvCount();
//    public abstract int getInputSlotInd();
    protected boolean optionalOutputCheck() {
        return false;
    }
    protected abstract boolean insertCheck(int slot, ItemStack stack);
    protected abstract boolean extractCheck(int slot, ItemStack stack);

    @Override
    public int getMaxCountPerStack() {
        return getInvMaxStackCount();
    }

    public AbstractAutoCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(AutomatedCrafting.AUTO_CRAFTER_BLOCK_ENTITY_TYPE, pos, state);
        craftingInventory = new CraftingInventoryWithOutput(GRID_WIDTH, GRID_HEIGHT, 1, getInvMaxStackCount(), getApparentInvCount());
        recipeCache = null;
        currentKey = getValidationKey();
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.craftingInventory.getInventory());
        super.writeNbt(nbt);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.craftingInventory.getInventory());
    }

    public DefaultedList<ItemStack> getInventory() {
        return this.craftingInventory.getInventory();
    }

    public void tryCraft() {
        CraftingInventory templateInv = this.getIsolatedTemplateInv();
        Recipe<CraftingInventory> recipe = getRecipe(templateInv);
        if(recipe != null) {
            ItemStack output = recipe.craft(templateInv);
            OutputAction outputAction = canOutput(output);
            if(outputAction != OutputAction.FAIL) {
                DefaultedList<ItemStack> remainingStacks = recipe.getRemainder(this.craftingInventory);
                ItemStack slotRemainder;
                for (int iSlot = FIRST_INPUT_SLOT; iSlot < OUTPUT_SLOT; iSlot++) {
                    slotRemainder = remainingStacks.get(iSlot);
                    if (slotRemainder.isEmpty())
                        //decrement stack
                        this.craftingInventory.removeStack(iSlot, 1);
                    else
                        //set remainder
                        this.craftingInventory.setStack(iSlot, slotRemainder);
                }


                if (outputAction == OutputAction.SET)
                    this.setStackWithoutCrafting(OUTPUT_SLOT, output);
                else //outputAction == OutputAction.INCREMENT
                    this.getInventory().get(OUTPUT_SLOT).increment(output.getCount());
            }
            else if (!AutoCrafterSharedData.CRAFTS_CONTINUOUSLY && world != null) {
                world.syncWorldEvent(1001, pos, 0);
            }
        }
        else if (!AutoCrafterSharedData.CRAFTS_CONTINUOUSLY && world != null) {
            world.syncWorldEvent(1001, pos, 0);
        }
    }

    private OutputAction canOutput(ItemStack output) {
        if (optionalOutputCheck()) {
            return OutputAction.FAIL;
        }
        ItemStack oldOutput = this.getInventory().get(OUTPUT_SLOT);
        if (oldOutput.isEmpty()) {
            return OutputAction.SET;
        } else if (output.isItemEqual(oldOutput) && oldOutput.getMaxCount() >= oldOutput.getCount() + output.getCount()) {
            //outputs are same item and output can fit in stack
            return OutputAction.INCREMENT;
        }
        return OutputAction.FAIL;
    }

    private Recipe<CraftingInventory> getRecipe(CraftingInventory inventory) {
        if (world == null) throw new IllegalStateException("World is null. ");
        RecipeManager recipeManager = this.world.getRecipeManager();

        Supplier<Recipe<CraftingInventory>> recipeFetcher = () ->
            recipeManager.getFirstMatch(RecipeType.CRAFTING, inventory, this.world).orElse(null);

        if(recipeCache == null) {
            recipeCache = recipeFetcher.get();
        } else if (currentKey != getValidationKey()) {
            currentKey = getValidationKey();
            recipeCache = recipeFetcher.get();
        }
        else if(!recipeCache.matches(this.getIsolatedInputInv(), world)) {
            recipeCache = recipeFetcher.get();
        }

        return recipeCache;
    }

    protected CraftingInventory getIsolatedInputInv() {
        return new CraftingInventoryWithoutHandler(GRID_WIDTH, GRID_HEIGHT, this.craftingInventory.getInventorySubList(FIRST_INPUT_SLOT, GRID_SIZE));
    }

    protected CraftingInventory getIsolatedTemplateInv() {
        return new CraftingInventoryWithoutHandler(GRID_WIDTH, GRID_HEIGHT, this.craftingInventory.getInventorySubList(FIRST_TEMPLATE_SLOT, GRID_SIZE));
    }

    protected void tryCraftContinuously() {
        if (CRAFTS_CONTINUOUSLY && world != null && world.getBlockState(pos).get(AutoCrafterBlock.POWERED))
            tryCraft();
    }

    public enum OutputAction {
        FAIL, SET, INCREMENT
    }

    /**
     * start of SidedInventory implementations
     */
    @Override
    public int[] getAvailableSlots(Direction side) {
        // Create an array of indices of slots that can be interacted with using automation
        int[] slotIndices = new int[SIZE];

        // pull from output first
        slotIndices[0] = OUTPUT_SLOT;
        for (int i = 1; i < SIZE; i++) {
            // -1 because we're starting at i = 1
            // slotIndices[i] = i + FIRST_INPUT_SLOT - 1;
            slotIndices[i] = i + PRE_FIRST_INPUT_SLOT;
        }

        return slotIndices;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        if (slot == OUTPUT_SLOT) {
            return false;
        }

        return insertCheck(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return extractCheck(slot, stack);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack removedStack = super.removeStack(slot, amount);
        tryCraftContinuously();
        return removedStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack removedStack = super.removeStack(slot);
        tryCraftContinuously();
        return removedStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        setStackWithoutCrafting(slot, stack);
        tryCraftContinuously();
    }

    protected void setStackWithoutCrafting(int slot, ItemStack stack) {
        // Calling super.setStack() would result in the output slot's contents being truncated
        // So we have to directly set the slot's contents
        this.getInvStackList().set(slot, stack);
        markDirty();
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
        this.craftingInventory.setInventory(list);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("block.automated_crafting.auto_crafter", new Object[0]);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return getGuiConstructor().construct(syncId, playerInventory, world, pos);
    }

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
    protected interface GuiConstructor<C extends AbstractAutoCrafterGuiDescription> {
        C construct(int syncId, PlayerInventory playerInventory, World world, BlockPos pos);
    }

}
