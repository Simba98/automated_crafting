package net.sssubtlety.automated_crafting.mixin;

import net.minecraft.server.MinecraftServer;
import net.sssubtlety.automated_crafting.Validator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(at = @At("TAIL"), method = "reloadResources")
    private void onReloadDataPacks(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        Validator.update();
    }
}