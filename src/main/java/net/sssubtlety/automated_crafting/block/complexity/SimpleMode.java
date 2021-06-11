package net.sssubtlety.automated_crafting.block.complexity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.sssubtlety.automated_crafting.blockEntity.AbstractAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.SimpleAutoCrafterBlockEntity;

public class SimpleMode implements ComplexityMode {
    public BlockEntity getNewBlockEntity(BlockPos pos, BlockState state) {
        return new SimpleAutoCrafterBlockEntity(pos, state);
    }
}
