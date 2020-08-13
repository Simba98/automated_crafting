package net.sssubtlety.automated_crafting.block.connectivity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Connectivity {
    boolean isPowered(World world, BlockPos pos);
}
