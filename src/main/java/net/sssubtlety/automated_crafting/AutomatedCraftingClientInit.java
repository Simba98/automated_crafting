package net.sssubtlety.automated_crafting;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.render.RenderLayer;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;

import static net.sssubtlety.automated_crafting.AutomatedCraftingInit.MOD_ID;

public class AutomatedCraftingClientInit implements ClientModInitializer {

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(AutomatedCraftingInit.getAutoCrafter(), RenderLayer.getSolid());
        // don't be fooled, specifying the generics' types is necessary here
        ScreenRegistry.<AbstractAutoCrafterGuiDescription, AutoCrafterScreen>register(AutomatedCraftingInit.AUTO_CRAFTER_SCREEN_HANDLER_TYPE, (gui, playerInventory, titleText) -> new AutoCrafterScreen(gui, playerInventory.player, titleText));
        CrowdinTranslate.downloadTranslations("automated-crafting", MOD_ID);

        try {
            if (((SemanticVersion)FabricLoader.getInstance()
                    .getModContainer("fabric").get()
                    .getMetadata().getVersion())
                    .compareTo(SemanticVersion.parse("0.28.4+1.16")) >= 0)
            {
                ResourceRegistration.register();
            }
        } catch (VersionParsingException e) {
            e.printStackTrace();
        }

//        FabricLoader.getInstance().getModContainer("fabric").ifPresent(fabricApiContainer -> {
//            try {
//                if (((SemanticVersion)fabricApiContainer.getMetadata().getVersion()).compareTo(SemanticVersion.parse("0.28.4+1.16")) >= 0) {
//                    ResourceRegistration.register();
//                }
//            } catch (VersionParsingException e) {
//                e.printStackTrace();
//            }
//        });


    }
}
