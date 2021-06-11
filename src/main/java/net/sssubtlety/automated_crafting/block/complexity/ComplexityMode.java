package net.sssubtlety.automated_crafting.block.complexity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public interface ComplexityMode {
    BlockEntity getNewBlockEntity(BlockPos pos, BlockState state);
}
