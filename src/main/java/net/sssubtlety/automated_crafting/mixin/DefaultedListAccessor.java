package net.sssubtlety.automated_crafting.mixin;

import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(DefaultedList.class)
public interface DefaultedListAccessor {
    @Invoker
    static <E> DefaultedList<E> createDefaultedList(List<E> delegate, E initialElement) {
        throw new UnsupportedOperationException();
    }
}
