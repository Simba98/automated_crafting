package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
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
import net.sssubtlety.automated_crafting.block.AutoCrafterBlock;
import net.sssubtlety.automated_crafting.blockEntity.AbstractAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.ComplexAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.blockEntity.SimpleAutoCrafterBlockEntity;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.ComplexAutoCrafterGuiDescription;
import net.sssubtlety.automated_crafting.guiDescription.SimpleAutoCrafterGuiDescription;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.*;

public class AutomatedCraftingInit implements ModInitializer {
	// an instance of our new item
	private static Block AUTO_CRAFTER;

	public static BlockEntityType<AbstractAutoCrafterBlockEntity> AUTO_CRAFTER_BLOCK_ENTITY;
	public static ScreenHandlerType<AbstractAutoCrafterGuiDescription> AUTO_CRAFTER_SCREEN_HANDLER_TYPE;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Register config class
		AutoConfig.register(AutomatedCraftingConfig.class, GsonConfigSerializer::new);

		AUTO_CRAFTER = getAutoCrafterBlock();

		Registry.register(Registry.BLOCK, AutoCrafterBlock.ID, AUTO_CRAFTER);
//		if (SIMPLE_MODE) {
//			if (QUASI_CONNECTIVITY) {
//				//both enabled
//			} else {
//				//only simple mode enabled
//			}
//		} else {
//			if (QUASI_CONNECTIVITY) {
//				//only quasi connectivity enabled
//			} else {
//				//neither enabled
//			}
//		}
		Registry.register(Registry.ITEM, new Identifier("automated_crafting", "auto_crafter"), new BlockItem(AUTO_CRAFTER, new Item.Settings().group(ItemGroup.MISC)));
		AUTO_CRAFTER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "automated_crafting:auto_crafter_entity", BlockEntityType.Builder.create((SIMPLE_MODE ? SimpleAutoCrafterBlockEntity::new : ComplexAutoCrafterBlockEntity::new), AUTO_CRAFTER).build(null));

		AUTO_CRAFTER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(AutoCrafterBlock.ID, SIMPLE_MODE ?
			(syncId, inventory) -> (new SimpleAutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)) :
			(syncId, inventory) -> (new ComplexAutoCrafterGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY))
		);
		//		ContainerProviderRegistry.INSTANCE.registerFactory(AutoCrafterBlock.ID, (syncId, id, player, buf) -> new AutoCrafterGuiDescription(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
	}

	private static Block getAutoCrafterBlock() throws RuntimeException {
		try {
//			if (SIMPLE_MODE) {
//				if (QUASI_CONNECTIVITY) {
//					//both enabled
//					return new AutoCrafterBlock<>(FabricBlockSettings.of(new Material(MaterialColor.WOOD, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build(), QuasiConnectivity.class, SimpleMode.class);
//				} else {
//					//only simple mode enabled
//					return new AutoCrafterBlock<>(FabricBlockSettings.of(new Material(MaterialColor.WOOD, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build(), BasicConnectivity.class, SimpleMode.class);
//				}
//			} else {
//				if (QUASI_CONNECTIVITY) {
//					//only quasi connectivity enabled
//					return new AutoCrafterBlock<>(FabricBlockSettings.of(new Material(MaterialColor.WOOD, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build(), QuasiConnectivity.class, ComplexMode.class);
//				} else {
//					//neither enabled
//					return new AutoCrafterBlock<>(FabricBlockSettings.of(new Material(MaterialColor.WOOD, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build(), BasicConnectivity.class, ComplexMode.class);
//				}
//			}
			return new AutoCrafterBlock<> (FabricBlockSettings.of(new Material(MaterialColor.WOOD, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build(),
					CONNECTIVITY_CLASS,
					COMPLEXITY_CLASS,
					REDIRECTS_REDSTONE
			);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("Unable to construct auto crafter block", e);
		}

	}

	public static Block getAutoCrafter() {
		return AUTO_CRAFTER;
	}
}
