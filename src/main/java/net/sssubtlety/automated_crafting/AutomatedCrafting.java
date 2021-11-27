package net.sssubtlety.automated_crafting;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.sssubtlety.automated_crafting.guiDescription.AutoCrafterGuiDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack;

public class AutomatedCrafting {
	public static final String NAMESPACE = "automated_crafting";

	public static final Logger LOGGER = LogManager.getLogger();

	public static Block getBLOCK() {
		return Registrar.BLOCK;
	}

	public static class Init implements ModInitializer {
		@Override
		public void onInitialize () {
			Config.init();
			Registrar.init();

			AutoCrafterGuiDescription.init();
		}
	}

	public static class ClientInit implements ClientModInitializer {
		public static final String POWERED = "powered";

		@Override
		@Environment(EnvType.CLIENT)
		public void onInitializeClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(AutomatedCrafting.getBLOCK(), RenderLayer.getSolid());
			// don't be fooled, specifying the generics' types is necessary here
			ScreenRegistry.<AutoCrafterGuiDescription, AutoCrafterScreen>register(Registrar.SCREEN_HANDLER_TYPE, (gui, playerInventory, titleText) -> new AutoCrafterScreen(gui, playerInventory.player, titleText));
			CrowdinTranslate.downloadTranslations("automated-crafting", NAMESPACE);

			ModContainer modContainer = FabricLoader.getInstance().getModContainer(NAMESPACE).get();
			registerBuiltinResourcePack(new Identifier(NAMESPACE, POWERED + "_icon"), modContainer, ResourcePackActivationType.NORMAL);
			registerBuiltinResourcePack(new Identifier(NAMESPACE, POWERED + "_block"), modContainer, ResourcePackActivationType.NORMAL);
		}
	}
}
