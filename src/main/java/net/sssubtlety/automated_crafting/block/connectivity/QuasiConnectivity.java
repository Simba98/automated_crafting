package net.sssubtlety.automated_crafting.block.connectivity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class QuasiConnectivity implements Connectivity {
//    static boolean isPowered(World world, BlockPos pos) {
//        return getBasicPower(world, pos) || world.isReceivingRedstonePower(pos.up());
//    }
    @Override
    public boolean isPowered(World world, BlockPos pos) {
        return BasicConnectivity.isPoweredImplementation(world, pos) || world.isReceivingRedstonePower(pos.up());
    }

}
