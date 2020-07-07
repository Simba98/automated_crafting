package net.sssubtlety.automated_crafting.block.connectivity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicConnectivity implements Connectivity {
    @Override
    public boolean isPowered(World world, BlockPos pos) {
        return isPoweredImplementation(world, pos);
    }

    public static boolean isPoweredImplementation(World world, BlockPos pos) {
        return world.isReceivingRedstonePower(pos);
    }
}
