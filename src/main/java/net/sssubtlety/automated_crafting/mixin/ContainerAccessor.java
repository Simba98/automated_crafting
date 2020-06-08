package net.sssubtlety.automated_crafting.mixin;

import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Container.class)
public interface ContainerAccessor {

    @Accessor
    DefaultedList<ItemStack> getTrackedStacks();

    @Accessor
    void setTrackedStacks(DefaultedList<ItemStack> stacks);
}
