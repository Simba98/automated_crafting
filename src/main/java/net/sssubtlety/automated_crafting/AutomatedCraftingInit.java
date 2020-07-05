package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AutomatedCraftingInit implements ModInitializer {
	// an instance of our new item
	public static final AutoCrafterBlock AUTO_CRAFTER = new AutoCrafterBlock(FabricBlockSettings.of(new Material(MaterialColor.WOOD, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build());
	//.breakByTool(FabricToolTags.PICKAXES, 5)
	public static BlockEntityType<AutoCrafterBlockEntity> AUTO_CRAFTER_BLOCK_ENTITY;
	public static ScreenHandlerType<AutoCrafterGuiDescription> AUTO_CRAFTER_SCREEN_HANDLER_TYPE;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.BLOCK, AutoCrafterBlock.ID, AUTO_CRAFTER);
		Registry.register(Registry.ITEM, new Identifier("automated_crafting", "auto_crafter"), new BlockItem(AUTO_CRAFTER, new Item.Settings().group(ItemGroup.MISC)));
		AUTO_CRAFTER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "automated_crafting:auto_crafter_entity", BlockEntityType.Builder.create(AutoCrafterBlockEntity::new, AUTO_CRAFTER).build(null));

		//Register config class
		AUTO_CRAFTER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(AutoCrafterBlock.ID, (syncId,  inventory) -> new AutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));

		//		ContainerProviderRegistry.INSTANCE.registerFactory(AutoCrafterBlock.ID, (syncId, id, player, buf) -> new AutoCrafterGuiDescription(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));


		AutoConfig.register(AutomatedCraftingConfig.class, GsonConfigSerializer::new);
	}
}
