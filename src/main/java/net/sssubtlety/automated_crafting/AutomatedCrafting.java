package net.sssubtlety.automated_crafting;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sssubtlety.automated_crafting.block.AutoCrafterBlock;
import net.sssubtlety.automated_crafting.blockEntity.AbstractAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.ComplexAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.SimpleAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.ComplexAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.SimpleAutoCrafterGuiDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.*;

public class AutomatedCrafting {
	public static final String NAMESPACE = "automated_crafting";

	public static final Logger LOGGER = LogManager.getLogger();

	private static Block AUTO_CRAFTER;
	public static BlockEntityType<AbstractAutoCrafterBlockEntity> AUTO_CRAFTER_BLOCK_ENTITY_TYPE;
	public static ScreenHandlerType<AbstractAutoCrafterGuiDescription> AUTO_CRAFTER_SCREEN_HANDLER_TYPE;

	public static class Init implements ModInitializer {
		@Override
		public void onInitialize () {
			// This code runs as soon as Minecraft is in a mod-load-ready state.
			// However, some things (like resources) may still be uninitialized.
			// Proceed with mild caution.

			//Register config class
			AutoConfig.register(AutomatedCraftingConfig.class, GsonConfigSerializer::new);

			AUTO_CRAFTER = Registry.register(Registry.BLOCK, AutoCrafterBlock.ID, getAutoCrafterBlock());
			Registry.register(Registry.ITEM, new Identifier("automated_crafting", "auto_crafter"), new BlockItem(AUTO_CRAFTER, new Item.Settings().group(ItemGroup.REDSTONE)));
			AUTO_CRAFTER_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "automated_crafting:auto_crafter_entity", FabricBlockEntityTypeBuilder.create((SIMPLE_MODE ? SimpleAutoCrafterBlockEntity::new : ComplexAutoCrafterBlockEntity::new), AUTO_CRAFTER).build(null));

			AUTO_CRAFTER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(AutoCrafterBlock.ID, SIMPLE_MODE ?
					(syncId, inventory) -> (new SimpleAutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)) :
					(syncId, inventory) -> (new ComplexAutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY))
			);
		}
	}

	public static class ClientInit implements ClientModInitializer {
		@Override
		@Environment(EnvType.CLIENT)
		public void onInitializeClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(AutomatedCrafting.getAutoCrafter(), RenderLayer.getSolid());
			// don't be fooled, specifying the generics' types is necessary here
			ScreenRegistry.<AbstractAutoCrafterGuiDescription, AutoCrafterScreen>register(AutomatedCrafting.AUTO_CRAFTER_SCREEN_HANDLER_TYPE, (gui, playerInventory, titleText) -> new AutoCrafterScreen(gui, playerInventory.player, titleText));
			CrowdinTranslate.downloadTranslations("automated-crafting", NAMESPACE);

			try {
				if (((SemanticVersion) FabricLoader.getInstance()
						.getModContainer("fabric").get()
						.getMetadata().getVersion())
						.compareTo(SemanticVersion.parse("0.28.4+1.16")) >= 0)
				{
					ResourceRegistration.register();
				}
			} catch (VersionParsingException e) {
				e.printStackTrace();
			}
		}
	}

	private static Block getAutoCrafterBlock() throws RuntimeException {
		try {
			return new AutoCrafterBlock<> (FabricBlockSettings.of(new Material(MapColor.OAK_TAN, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build(),
					CONNECTIVITY_CLASS,
					COMPLEXITY_CLASS
			);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("Unable to construct auto crafter block", e);
		}

	}

	public static Block getAutoCrafter() {
		return AUTO_CRAFTER;
	}
}
