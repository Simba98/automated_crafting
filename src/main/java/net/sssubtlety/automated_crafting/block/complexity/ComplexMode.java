package net.sssubtlety.automated_crafting.block.complexity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.sssubtlety.automated_crafting.blockEntity.ComplexAutoCrafterBlockEntity;

public class ComplexMode implements ComplexityMode {
    public BlockEntity getNewBlockEntity(BlockPos pos, BlockState state) {
        return new ComplexAutoCrafterBlockEntity(pos, state);
    }
}
