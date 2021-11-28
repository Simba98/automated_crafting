package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Arrays;
import java.util.List;

public class DefaultedStackView extends DefaultedList<ItemStack> {
    public DefaultedStackView(int size) {
        this(Arrays.asList(new ItemStack[size]));
    }

    public DefaultedStackView(List<ItemStack> stacks) {
        super(fillEmpty(stacks), ItemStack.EMPTY);
    }

    protected static List<ItemStack> fillEmpty(List<ItemStack> stacks) {
        final int size = stacks.size();
        for (int i = 0; i < size; i++) {
            if (stacks.get(i) == null)
                stacks.set(i, ItemStack.EMPTY);
        }
        return stacks;
    }

    @Override
    public DefaultedStackView subList(int fromIndex, int toIndex) {
        return new DefaultedStackView(super.subList(fromIndex, toIndex));
    }

    public Singleton subStack(int index) {
        return new Singleton(index, this);
    }

    public static class Singleton extends DefaultedStackView {

        protected Singleton(int index, DefaultedStackView parent) {
            super(parent.subList(index, index + 1));
        }

        public ItemStack get() {
            return this.get(0);
        }

        public void set(ItemStack stack) {
            this.set(0, stack);
        }

        public Item getItem() {
            return this.get().getItem();
        }

        public int getCount() {
            return this.get().getCount();
        }

        public int getMaxCount() {
            return this.get().getMaxCount();
        }

        public ItemStack split(int amount) {
            return this.get().split(amount);
        }

        public boolean isStackEmpty() {
            return this.get().isEmpty();
        }
    }
}
