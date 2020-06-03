package net.fabricmc.automated_crafting;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.CraftingTableContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

public class AutoCrafterBlockEntity extends LootableContainerBlockEntity implements SidedInventory, RecipeInputProvider {
    private static final int OUTPUT_SLOT = 9;

//    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);

    private InventoryContainer inventoryContainer = new InventoryContainer(10);

    private CraftingInventory craftingInventory = new CraftingInventory(inventoryContainer, 3, 3);

    public AutoCrafterBlockEntity() {
        super(AutomatedCraftingInit.AUTO_CRAFTER_ENTITY);
    }

    // Serialize the BlockEntity
    public CompoundTag toTag(CompoundTag tag) {
        // Save the current value of the number to the tag
//        tag.putInt("number", number);
        Inventories.toTag(tag, inventoryContainer.getInventory());
        return super.toTag(tag);
    }

    // Deserialize the BlockEntity
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag, inventoryContainer.getInventory());
//        number = tag.getInt("number");
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventoryContainer.getInventory();
    }

    public void tryCraft() {
//        if(inventory.get(4).getItem() == Items.SPRUCE_LOG) {
//            inventory.set(4, ItemStack.EMPTY);
//            inventory.set(9, new ItemStack(Items.SPRUCE_PLANKS, 4));
//        }
        Recipe<?> recipe = this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.craftingInventory, this.world).orElse(null);
        if(recipe instanceof ShapedRecipe) {

        } else if(recipe instanceof ShapelessRecipe) {

        } else {
            throw new IllegalStateException("Received unexpected recipe type. A critical mixin failure has occurred.");
        }
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
        if(slot == OUTPUT_SLOT) { return false; }
        //players and top hoppers can insert to input slots
        return direction == null || direction == Direction.UP;
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction direction) {
        //players and hoppers can take from output,
        //hoppers can't take from input slots
        if(slot == OUTPUT_SLOT || direction == null) { return true; }
        return false;
    }
    /**
     * end of SidedInventory implementations
     * start of LootableContainerBlockEntity implementations
     */
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.getInventory();
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventoryContainer.setInventory(list);
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
        return getInventory().size();
    }
    /**
     * end of Inventory implementations
     * start of RecipeInputProvider implementations
     */
    @Override
    public void provideRecipeInputs(RecipeFinder recipeFinder) {

    }
    /**
     * end of RecipeInputProvider implementations
     */
}
