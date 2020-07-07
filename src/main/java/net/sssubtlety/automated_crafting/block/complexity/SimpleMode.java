package net.sssubtlety.automated_crafting.block.complexity;

import net.minecraft.block.entity.BlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.AbstractAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.SimpleAutoCrafterBlockEntity;

public class SimpleMode implements ComplexityMode {
    public BlockEntity getNewBlockEntity() {
        return new SimpleAutoCrafterBlockEntity();
    }
}
