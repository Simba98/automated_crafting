package net.sssubtlety.automated_crafting.mixin;

import net.sssubtlety.automated_crafting.blockEntity.ComplexAutoCrafterBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "removeBlockEntity", at = @At("HEAD"))
    public void preRemoveBlockEntity(BlockPos blockPos, CallbackInfo info) {
        if(((World)(Object)this).isClient()) { return; }
        BlockEntity blockEntity = ((World)(Object)this).getBlockEntity(blockPos);
        if(blockEntity instanceof ComplexAutoCrafterBlockEntity) {
            ComplexAutoCrafterBlockEntity.untrackInstance((ComplexAutoCrafterBlockEntity)blockEntity);
        }
    }

    @Inject(method = "addBlockEntity", at = @At("HEAD"))
    public void preAddBlockEntity(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> infoReturnable) {
        if(((World)(Object)this).isClient()) { return; }
        if(blockEntity instanceof ComplexAutoCrafterBlockEntity) {
            ComplexAutoCrafterBlockEntity.trackInstance((ComplexAutoCrafterBlockEntity)blockEntity);
        }
    }
}
