package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.sssubtlety.automated_crafting.mixin.CraftingInventoryAccessor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static net.sssubtlety.automated_crafting.AutoCrafterBlockEntity.MAX_STACK_SIZE;

public abstract class CraftingView extends CraftingInventory implements TrimmableInventory {
    private static final DummyHandler dummyHandler = new DummyHandler();

    protected final Set<Integer> occupiedSlots;

    public CraftingView(DefaultedList<ItemStack> stacks) {
        super(dummyHandler, Grid.WIDTH, Grid.HEIGHT);
        ((CraftingInventoryAccessor)this).setStacks(stacks);
        occupiedSlots = new LinkedHashSet<>(Grid.SIZE, 1);
    }

    @Override
    public boolean isEmpty() {
        return occupiedSlots.isEmpty();
    }

    @Override
    public boolean containsAny(Set<Item> items) {
        for (int slot : occupiedSlots)
            if (items.contains(getStack(slot).getItem())) return true;

        return false;
    }

    @Override
    public int count(Item item) {
        int count = 0;
        for (int slot : occupiedSlots) {
            ItemStack stack = getStack(slot);
            if (stack.getItem() == item) count += stack.getCount();
        }
        return count;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack storedStack = getStack(slot);
        ItemStack removedStack = storedStack.split(amount);
        if (storedStack.isEmpty()) setEmpty(slot);
        return removedStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = getStack(slot);
        setEmpty(slot);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (occupiedSlots.contains(slot)) {
            if (stack.isEmpty()) setEmpty(slot);
            else super.setStack(slot, stack);
        } else {
            if (!stack.isEmpty()) {
                occupiedSlots.add(slot);
                super.setStack(slot, stack);
            }// else empty->empty: do nothing
        }
    }

    @Override
    public void clear() {
        occupiedSlots.forEach(this::setEmpty);
        occupiedSlots.clear();
    }

    protected void setEmpty(int slot) {
        occupiedSlots.remove(slot);
        super.setStack(slot, ItemStack.EMPTY);
    }

    @Override
    public Inventory getTrimmed() {
        return new ArrayInventory(getTrimmedStream());
    }

    @Override
    public Stream<ItemStack> getTrimmedStream() {
        return occupiedSlots.stream().map(this::getStack);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return getStack(slot).isEmpty();
    }

    @Override
    public int getMaxCountPerStack() {
        return MAX_STACK_SIZE;
    }

    public interface Grid {
        int HEIGHT = 3;
        int WIDTH = 3;
        int SIZE = WIDTH * HEIGHT;

        static boolean contains(int slot) {
            return slot >= 0 && slot < SIZE;
        }
    }

    private static final class DummyHandler extends ScreenHandler {
        protected DummyHandler() {
            super(null, 0);
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return false;
        }

        @Override
        public void onContentChanged(Inventory inventory) { }
    }
}
