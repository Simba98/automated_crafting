package net.fabricmc.automated_crafting.mixin;

import net.fabricmc.automated_crafting.AutoCrafterBlockEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(at = @At("TAIL"), method = "reloadDataPacks")
    private void onReloadDataPacks(CallbackInfo info) {
        System.out.println("CustomPiglinBartering: refreshing data after reloadDataPacks. ");
        AutoCrafterBlockEntity.clearRecipeCaches();
    }
}