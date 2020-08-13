package net.sssubtlety.automated_crafting.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
import net.sssubtlety.automated_crafting.AutomatedCraftingInit;
import net.sssubtlety.automated_crafting.CraftingInventoryWithOutput;
import net.sssubtlety.automated_crafting.CraftingInventoryWithoutHandler;
import net.sssubtlety.automated_crafting.block.AutoCrafterBlock;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;

import java.util.function.Supplier;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.*;

public abstract class AbstractAutoCrafterBlockEntity extends LootableContainerBlockEntity implements SidedInventory, NamedScreenHandlerFactory {
    protected final CraftingInventoryWithOutput craftingInventory;
    private int validationKey = Integer.MIN_VALUE;
    protected Recipe<CraftingInventory> recipeCache;

    protected abstract GuiConstructor<AbstractAutoCrafterGuiDescription> getGuiConstructor();
    protected abstract int getInvMaxStackCount();
    protected abstract int getApparentInvCount();
    public abstract int getInputSlotInd();
    protected abstract boolean optionalOutputCheck();
    protected abstract boolean insertCheck(int slot, ItemStack stack);
    protected abstract boolean extractCheck(int slot, ItemStack stack);

    public AbstractAutoCrafterBlockEntity() {
        super(AutomatedCraftingInit.AUTO_CRAFTER_BLOCK_ENTITY);
        craftingInventory = new CraftingInventoryWithOutput(GRID_WIDTH, GRID_HEIGHT, 1, getInvMaxStackCount(), getApparentInvCount());//SIMPLE_MODE ? 2 : 1
        recipeCache = null;
    }

    // Serialize the BlockEntity
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        // Save the current value of the number to the tag
        Inventories.toTag(tag, ((CraftingInventoryAccessor)this.craftingInventory).getInventory());
        return super.toTag(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, ((CraftingInventoryAccessor)this.craftingInventory).getInventory());
    }

    public DefaultedList<ItemStack> getInventory() {
        return ((CraftingInventoryAccessor)this.craftingInventory).getInventory();
    }

    public void tryCraft() {
        Recipe<CraftingInventory> recipe = getRecipe();
        if(recipe != null) {
            OutputAction outputAction = canOutput(recipe.getOutput());
            if(outputAction != OutputAction.FAIL) {//tryOutput(recipe.getOutput())) {
                for (int iSlot = getInputSlotInd(); iSlot < OUTPUT_SLOT; iSlot++) {
                    Item slotItem = this.internalGetStack(iSlot).getItem();
                    if (slotItem.hasRecipeRemainder())
                        //replace with remainders
                        this.craftingInventory.setStack(iSlot, new ItemStack(slotItem.getRecipeRemainder()));
                    else
                        //decrement stack, should be empty afterward
                        this.craftingInventory.removeStack(iSlot, 1);
                }

                ItemStack output = recipe.getOutput().copy();
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

    protected CraftingInventory getIsolatedInputInv() {
        CraftingInventory tempInventory = new CraftingInventoryWithoutHandler(GRID_WIDTH, GRID_HEIGHT);//((CraftingScreenHandlerAccessor)(new CraftingScreenHandler(0, new PlayerInventory(null)))).getInput();

        for(int slot = size(); slot < OUTPUT_SLOT; slot++) {
            tempInventory.setStack(slot - size(), getInvStackList().get(slot));
        }
        return tempInventory;
    }

    private Recipe<CraftingInventory> getRecipe() {
        assert world != null;
        RecipeManager recipeManager = this.world.getRecipeManager();

        Supplier<Recipe<CraftingInventory>> recipeFetcher = () ->
            recipeManager.getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);

        if(recipeCache == null) {
            recipeCache = recipeFetcher.get();
        } else if (validationKey != getValidationKey()) {
            validationKey = getValidationKey();
            recipeCache = recipeFetcher.get();
        }
        else if(!recipeCache.matches(this.craftingInventory, world)) {
            recipeCache = recipeFetcher.get();
        }

        return recipeCache;
    }

    protected ItemStack internalGetStack(int slot) {
        return slot >= OUTPUT_SLOT ? ItemStack.EMPTY : ((CraftingInventoryAccessor)this.craftingInventory).getInventory().get(slot);
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
        int[] result = new int[getInventory().size()];

        // Create an array of indices of slots that can be inserted into
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        if (slot == OUTPUT_SLOT) {
            return false;
        }

        return insertCheck(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir)
    {
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
        super.setStack(slot, stack);
        tryCraftContinuously();
    }

    protected void setStackWithoutCrafting(int slot, ItemStack stack) {
        super.setStack(slot, stack);
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

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
//        return new AutoCrafterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
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
