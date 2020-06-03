package net.fabricmc.automated_crafting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.container.BlockContext;

import static net.fabricmc.automated_crafting.AutomatedCraftingInit.AUTO_CRAFTER;

public class AutomatedCraftingClientInit implements ClientModInitializer {
    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(AUTO_CRAFTER, RenderLayer.getTranslucent());
        ScreenProviderRegistry.INSTANCE.registerFactory(AutoCrafterBlock.ID, (syncId, identifier, player, buf) -> new AutoCrafterScreen(new AutoCrafterController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }

}
