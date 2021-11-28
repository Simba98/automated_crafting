package net.sssubtlety.automated_crafting;

import com.google.common.collect.Streams;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.sssubtlety.automated_crafting.gui.AutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.inventory.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static net.sssubtlety.automated_crafting.AutomatedCrafting.LOGGER;

/*
  Not actually a sided inventory, but needs `getAvailableSlots`, `canInsert`, `canExtract`.
  These allow for separate internal inventories and configs.
*/
public class AutoCrafterBlockEntity extends LootableContainerBlockEntity implements SidedInventory, TrimmableInventory, NamedScreenHandlerFactory {
    public static final int MAX_STACK_SIZE = 1;
    public static final int[] AVAILABLE_INDICES;
    public static final Validator validator = new Validator();
    public static final TranslatableText NAME = new TranslatableText("block.automated_crafting.auto_crafter");

    public static boolean templatePredicate(ItemStack inputStack, ItemStack templateStack) {
        return templateStack.isItemEqual(inputStack) && ItemStack.areNbtEqual(templateStack, inputStack);
    }

    static {
        // An array of indices of slots that can be interacted with using automation
        AVAILABLE_INDICES = new int[Slots.INPUT_PLUS_OUTPUT_SIZE];
        // pull from output first
        AVAILABLE_INDICES[0] = Slots.OUTPUT_SLOT;
        for (int i = 1; i < Slots.INPUT_PLUS_OUTPUT_SIZE; i++) {
            // PRE_FIRST_INPUT_SLOT because we're starting at i = 1
            // slotIndices[i] = i + FIRST_INPUT_SLOT - 1;
            AVAILABLE_INDICES[i] = i + Slots.PRE_FIRST_INPUT_SLOT;
        }
    }

    private final DefaultedStackView combinedStacks;
    private final TemplateInventory templateInventory;
    private final InputInventory inputInventory;
    private final DefaultedStackView.Singleton output;



    protected final Validator.Validation validation;
    protected Recipe<CraftingInventory> recipeCache;

    public AutoCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(Registrar.BLOCK_ENTITY_TYPE, pos, state);

        this.combinedStacks = new DefaultedStackView(Slots.INVENTORY_SIZE);
        this.templateInventory = new TemplateInventory(this.combinedStacks.subList(0, Slots.INPUT_START));
        this.inputInventory = new InputInventory(this.combinedStacks.subList(Slots.INPUT_START, Slots.OUTPUT_SLOT), this.templateInventory);
        this.output = this.combinedStacks.subStack(Slots.OUTPUT_SLOT);

        this.recipeCache = null;
        this.validation = validator.getValidation();
    }

    protected boolean matchesTemplate(int slot, ItemStack inputStack) {
        ItemStack templateStack = this.templateInventory.getStack(slot);
        return templatePredicate(inputStack, templateStack);
    }

    @Override
    public int getMaxCountPerStack() {
        return MAX_STACK_SIZE;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot > Slots.OUTPUT_SLOT) return false;
        else if (slot == Slots.OUTPUT_SLOT) {
            return true;
        } else {
            if (slot >= Slots.INPUT_START)
                return inputInventory.isValid(Slots.toInputSlot(slot), stack);
            else
                return templateInventory.isValid(slot, stack);
        }
    }

    @Override
    public int count(Item item) {
        int count = templateInventory.count(item);
        count += inputInventory.count(item);
        if (output.getItem() == item) count += output.getCount();
        return count;
    }

    @Override
    public boolean containsAny(Set<Item> items) {
        return  items.contains(output.getItem()) ||
                templateInventory.containsAny(items) ||
                inputInventory.containsAny(items);
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        DefaultedList<ItemStack> invStackList = getInvStackList();
        Inventories.writeNbt(nbt, invStackList);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        DefaultedList<ItemStack> invStackList = DefaultedList.ofSize(Slots.INVENTORY_SIZE, ItemStack.EMPTY);
        Inventories.readNbt(nbt, invStackList);
        setInvStackList(invStackList);
    }

    public void tryCraft() {
        Optional<Recipe<CraftingInventory>> optRecipe = getRecipe();
        if(optRecipe.isPresent()) {
            Recipe<CraftingInventory> recipe = optRecipe.get();
            ItemStack output = recipe.craft(templateInventory);
            OutputAction outputAction = checkOutput(output);
            if(outputAction != OutputAction.FAIL) {
                DefaultedList<ItemStack> remainingStacks = recipe.getRemainder(this.inputInventory);
                ItemStack slotRemainder;
                for (int i = 0; i < CraftingView.Grid.SIZE; i++) {
                    slotRemainder = remainingStacks.get(i);
                    if (slotRemainder.isEmpty())
                        //decrement stack
                        this.inputInventory.removeStack(i, 1);
                    else
                        //set remainder
                        this.inputInventory.setStack(i, slotRemainder);
                }

                if (outputAction == OutputAction.SET)
                    this.output.set(output);
                else //outputAction == OutputAction.INCREMENT
                    this.output.get().increment(output.getCount());
            } else tryPlayFailSound();
        } else tryPlayFailSound();
    }

    protected void tryPlayFailSound() {
        if (world != null && !Config.doesCraftContinuously())
            world.syncWorldEvent(1001, pos, 0);
    }

    protected OutputAction checkOutput(ItemStack output) {
        if (this.output.isStackEmpty()) return OutputAction.SET;
        else if (output.isItemEqual(this.output.get()) && this.output.getMaxCount() >= this.output.getCount() + output.getCount())
            //outputs are same item and output can fit in stack
            return OutputAction.INCREMENT;

        return OutputAction.FAIL;
    }

    protected Optional<Recipe<CraftingInventory>> getRecipe() {
        if (world == null) {
            LOGGER.error("Trying to get recipe before world is initialized!");
            return Optional.empty();
        }

        if (Config.isSimpleMode() && inputMisMatchesTemplate()) return Optional.empty();

        if(
            recipeCache == null ||
            validation.invalid() ||
            !recipeCache.matches(inputInventory, world)
        ) {
            recipeCache = this.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inputInventory, this.world)
                    .orElse(null);
        }

        return Optional.ofNullable(recipeCache);
    }

    private boolean inputMisMatchesTemplate() {
        for (int slot = 0; slot < CraftingView.Grid.SIZE; slot++)
            if (!matchesTemplate(slot, inputInventory.getStack(slot))) return true;

        return false;
    }

    protected void tryCraftContinuously() {
        if (
            Config.doesCraftContinuously() &&
            world != null &&
            world.getBlockState(pos).get(AutoCrafterBlock.POWERED)
        ) tryCraft();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_INDICES;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        // this is meant ot be called after isValid, so slot should be empty and match template (if simple)
        int inputSlot = Slots.toInputSlot(slot);
        return CraftingView.Grid.contains(inputSlot);
//        && this.inputInventory.getStack(inputSlot).isEmpty();
//                && this.inputInventory.isValid(inputSlot, stack);
                // && matchesTemplate(inputSlot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (slot == Slots.OUTPUT_SLOT || !Config.isSimpleMode()) return true;

        int inputSlot = Slots.toInputSlot(slot);
        return CraftingView.Grid.contains(inputSlot) && !matchesTemplate(inputSlot, stack);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        final ItemStack removedStack;
        if (slot == Slots.OUTPUT_SLOT) removedStack = output.split(amount);
        else {
            removedStack = slot >= Slots.INPUT_START ?
                inputInventory.removeStack(Slots.toInputSlot(slot), amount) :
                templateInventory.removeStack(slot, amount);
        }

        tryCraftContinuously();
        return removedStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        final ItemStack removedStack;
        if (slot == Slots.OUTPUT_SLOT) {
            removedStack = output.get();
            output.set(ItemStack.EMPTY);
        } else {
            if (slot >= Slots.INPUT_START)
                removedStack = inputInventory.removeStack(Slots.toInputSlot(slot));
            else
                removedStack = templateInventory.removeStack(slot);
        }

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
        if (slot == Slots.OUTPUT_SLOT) output.set(stack);
        else {
            if (slot >= Slots.INPUT_START)
                inputInventory.setStack(Slots.toInputSlot(slot), stack);
            else
                templateInventory.setStack(slot, stack);
        }

        markDirty();
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return combinedStacks;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        final int size = list.size();
        int i;
        for (i = 0; i < Slots.INPUT_START; i++)
            templateInventory.setStack(i,i < size ? list.get(i) : ItemStack.EMPTY);

        for (int iInput = 0; i < Slots.OUTPUT_SLOT; iInput++, i++)
            inputInventory.setStack(iInput, i < size ? list.get(i) : ItemStack.EMPTY);

        output.set(i < size ? list.get(i) : ItemStack.EMPTY);
    }

    @Override
    protected Text getContainerName() {
        return NAME;
    }

    @Override
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return AutoCrafterGuiDescription.create(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public int size() {
        return Slots.INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return output.isEmpty() && templateInventory.isEmpty() && inputInventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot == Slots.OUTPUT_SLOT) return output.get();
        else if (slot >= Slots.INPUT_START) return inputInventory.getStack(Slots.toInputSlot(slot));
        else return templateInventory.getStack(slot);
    }

    @Override
    public void clear() {
        templateInventory.clear();
        inputInventory.clear();
        output.set(ItemStack.EMPTY);
    }

    public int getComparatorOutput() {
        if (Config.doesComparatorReadOutput() && !output.isEmpty()) return 15;
        return inputInventory.getComparatorOutput();
    }

    @Override
    public Inventory getTrimmed() {
        return new ArrayInventory(getTrimmedStream());
    }

    @Override
    public Stream<ItemStack> getTrimmedStream() {
        // don't include template because it's a 'ghost' inventory
        Stream<ItemStack> trimmedInput = inputInventory.getTrimmedStream();
        if (output.isStackEmpty()) return trimmedInput;
        else return Streams.concat(trimmedInput, output.stream());
    }

    public enum OutputAction {
        FAIL, SET, INCREMENT
    }

    public interface Slots {
        int INPUT_START = CraftingView.Grid.SIZE;
        int PRE_FIRST_INPUT_SLOT = INPUT_START - 1;

        int INPUT_PLUS_OUTPUT_SIZE = CraftingView.Grid.SIZE + 1;

        int OUTPUT_SLOT = CraftingView.Grid.SIZE * 2;
        int INVENTORY_SIZE = OUTPUT_SLOT + 1;

        static int toInputSlot(int slot) {
            return slot - CraftingView.Grid.SIZE;
        }
    }
}
