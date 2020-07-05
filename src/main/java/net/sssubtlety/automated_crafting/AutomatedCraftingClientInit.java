package net.sssubtlety.automated_crafting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class AutomatedCraftingClientInit implements ClientModInitializer {
    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(AutomatedCraftingInit.AUTO_CRAFTER, RenderLayer.getSolid());
        ScreenRegistry.<AutoCrafterGuiDescription, AutoCrafterScreen>register(AutomatedCraftingInit.AUTO_CRAFTER_SCREEN_HANDLER_TYPE, (gui, playerInventory, titleText) -> new AutoCrafterScreen(gui, playerInventory.player, titleText));
    }
}
