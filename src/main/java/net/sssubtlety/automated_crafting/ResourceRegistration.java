package net.sssubtlety.automated_crafting;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack;
import static net.sssubtlety.automated_crafting.AutomatedCrafting.NAMESPACE;

public interface ResourceRegistration {
    String POWERED = "powered";

    static void register() {
        FabricLoader.getInstance().getModContainer(NAMESPACE).ifPresent(modContainer -> {
            registerBuiltinResourcePack(new Identifier(NAMESPACE, POWERED + "_icon"), modContainer, ResourcePackActivationType.NORMAL);
            registerBuiltinResourcePack(new Identifier(NAMESPACE, POWERED + "_block"), modContainer, ResourcePackActivationType.NORMAL);
        });
    }
}
