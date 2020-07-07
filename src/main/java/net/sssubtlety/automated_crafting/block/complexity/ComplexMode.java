package net.sssubtlety.automated_crafting.block.complexity;

import net.minecraft.block.entity.BlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.ComplexAutoCrafterBlockEntity;

public class ComplexMode implements ComplexityMode {
    public BlockEntity getNewBlockEntity() {
        return new ComplexAutoCrafterBlockEntity();
    }
}
