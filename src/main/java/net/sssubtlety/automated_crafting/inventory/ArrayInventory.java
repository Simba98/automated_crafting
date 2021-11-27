package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class ArrayInventory implements Inventory {
    protected final ItemStack[] stacks;

    public ArrayInventory(int size) {
        this.stacks = new ItemStack[size];
    }

    public ArrayInventory(Collection<ItemStack> stacks) {
        this(stacks.size());
        stacks.toArray(this.stacks);
    }

    public ArrayInventory(Stream<ItemStack> stackStream) {
        this.stacks = stackStream.toArray(ItemStack[]::new);
    }

    public ArrayInventory(ItemStack... stacks) {
        this.stacks = stacks;
    }

    @Override
    public int size() {
        return stacks.length;
    }

    @Override
    public boolean isEmpty() {
        // not optimised because not intended for use,
        //   and optimization would require auxiliary data
        for (ItemStack stack : stacks)
            if (!stack.isEmpty()) return false;

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return stacks[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack removedStack = stacks[slot].split(amount);
        if (stacks[slot].isEmpty()) stacks[slot] = ItemStack.EMPTY;
        return removedStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack removedStack = stacks[slot];
        stacks[slot] = ItemStack.EMPTY;
        return removedStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) return;
        stacks[slot] = stack;
    }

    @Override
    public void markDirty() { }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        Arrays.fill(stacks, ItemStack.EMPTY);
    }
}
