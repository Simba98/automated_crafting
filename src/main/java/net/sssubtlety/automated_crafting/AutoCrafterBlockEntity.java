package net.sssubtlety.automated_crafting;

import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.Iterator;
import java.util.LinkedList;

public class AutoCrafterBlockEntity extends LootableContainerBlockEntity implements SidedInventory, AutoCrafterSharedData {
    private final CraftingInventoryWithOutput craftingInventory;
    private Recipe<CraftingInventory> recipeCache;


    private static LinkedList<AutoCrafterBlockEntity> allInstances = new LinkedList<>();

    public AutoCrafterBlockEntity() {
        super(AutomatedCraftingInit.AUTO_CRAFTER_BLOCK_ENTITY);
        craftingInventory = new CraftingInventoryWithOutput(GRID_WIDTH, GRID_HEIGHT, SIMPLE_MODE ? 2 : 1, SIMPLE_MODE);
        recipeCache = null;
    }

    public static void untrackInstance(AutoCrafterBlockEntity blockEntity) {
        allInstances.remove(blockEntity);
    }

    public static void trackInstance(AutoCrafterBlockEntity blockEntity) {
        allInstances.push(blockEntity);
    }

    // Serialize the BlockEntity
    public CompoundTag toTag(CompoundTag tag) {
        // Save the current value of the number to the tag
        Inventories.toTag(tag, ((CraftingInventoryAccessor)this.craftingInventory).getInventory());
        return super.toTag(tag);
    }

    // Deserialize the BlockEntity
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag, ((CraftingInventoryAccessor)this.craftingInventory).getInventory());
//        number = tag.getInt("number");
    }

    public DefaultedList<ItemStack> getInventory() {
        return ((CraftingInventoryAccessor)this.craftingInventory).getInventory();
    }

    public void tryCraft() {
        Recipe<CraftingInventory> recipe = getRecipe();//this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
        if(recipe != null) {
            if(tryOutput(recipe.getOutput())) {
                for (int iSlot = SIMPLE_MODE ? getInvSize() : 0; iSlot < OUTPUT_SLOT; iSlot++) {
                    this.craftingInventory.takeInvStack(iSlot, 1);
                }
            }
            else {
                world.playLevelEvent(1001, pos, 0);
            }
        }
        else {
            world.playLevelEvent(1001, pos, 0);
            System.out.println("tryCraft found no valid recipe. ");
        }
    }

    private boolean tryOutput(ItemStack output) {
        if (SIMPLE_MODE && !recipeCache.matches(getIsolatedInputInv(), world)) {
            System.out.println("tryOutput found insufficient resources. ");

            return false;
        }
        ItemStack oldOutput = this.getInventory().get(OUTPUT_SLOT);
        if(oldOutput.isEmpty()) {
            this.getInventory().set(OUTPUT_SLOT, output.copy());
            return true;
        }
        else if (output.isItemEqual(oldOutput) && oldOutput.getMaxCount() > oldOutput.getCount() + output.getCount()){
            //outputs are same item and output can fit in stack
            oldOutput.increment(output.getCount());
            return true;
        }
        System.out.println("tryOutput found no space in output slot. ");
        return false;
    }

    private CraftingInventory getIsolatedInputInv() {
        CraftingInventory tempInventory = new CraftingInventory(new InventoryContainer(0), 3, 3);
        for(int slot = getInvSize(); slot < OUTPUT_SLOT; slot++)
        {
            tempInventory.setInvStack(slot - getInvSize(), getInvStackList().get(slot));
        }
        return tempInventory;
    }

    private Recipe<CraftingInventory> getRecipe() {
        if(recipeCache == null) {
            recipeCache = this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
            if(recipeCache == null) {
                System.out.println("getRecipe replaced null cache with null. ");
            } else {
                System.out.println("getRecipe replaced null cache with new recipe for: " + recipeCache.getOutput().getItem().getTranslationKey());
            }
        }
        else if(!recipeCache.matches(this.craftingInventory, world)) {
            System.out.println("getRecipe replacing miss-matched cache recipe for: " + recipeCache.getOutput().getItem().getTranslationKey());
            recipeCache = this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
            if(recipeCache == null) {
                System.out.println("getRecipe replaced miss-matched cache with null");
            } else {
                System.out.println("getRecipe replaced miss-matched cache with new recipe for: " + recipeCache.getOutput().getItem().getTranslationKey());
            }
        }
        else {
            System.out.println("getRecipe found cached recipe matches. ");
        }
        return recipeCache;
    }

    public static void clearRecipeCaches() {
        System.out.println("clearRecipeCaches called with allInstances.size() = " + allInstances.size());
        for (Iterator<AutoCrafterBlockEntity> iterator = allInstances.iterator(); iterator.hasNext();) {
            AutoCrafterBlockEntity instance = iterator.next();
            if (instance == null) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            } else {
                instance.recipeCache = null;
            }
        }
    }

    private void isPowered() {

    }

    /**
     * start of SidedInventory implementations
     */
    @Override
    public int[] getInvAvailableSlots(Direction var1) {
        // Just return an array of all slots
        int[] result = new int[getInventory().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction direction) {
        //nothing external can insert into output slot
        if (slot == OUTPUT_SLOT) {
            return false;
        }

        if(SIMPLE_MODE) {
//            if(this.getInventory().get(slot).getCount() > 1) {
//                return false;
//            }
//            return !this.getInventory().get(slot).isEmpty();

            return slot >= getInvSize() && this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot - getInvSize()).isItemEqual(stack);

        }
        else {
            return this.getInventory().get(slot).isEmpty();
        }
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction direction) {
        //players and hoppers can take from output,
        //hoppers can't take from input slots
//        if(slot == OUTPUT_SLOT || direction == null) { return true; }
//        return false;
        if(SIMPLE_MODE) {
            return slot == OUTPUT_SLOT;
        }
        else {
            return true;
        }
    }
    /**
     * end of SidedInventory implementations
     * start of LootableContainerBlockEntity implementations
     */
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.getInventory();
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        ((CraftingInventoryAccessor)this.craftingInventory).setInventory(list);
    }

    protected Text getContainerName() {
        return new TranslatableText("container.dispenser", new Object[0]);
    }

    protected Container createContainer(int i, PlayerInventory playerInventory) {
        throw new IllegalStateException("Dummy method body invoked. A critical mixin failure has occurred.");
//        return new CraftingTableContainer(i, playerInventory, BlockContext.create(world, pos));
                //new Generic3x3Container(i, playerInventory, this);
    }
    /**
     * end of LootableContainerBlockEntity implementations
     * start of Inventory implementations
     */
    @Override
    public int getInvSize() {
        return craftingInventory.getInvSize();
    }
    /**
     * end of Inventory implementations
     */

}
