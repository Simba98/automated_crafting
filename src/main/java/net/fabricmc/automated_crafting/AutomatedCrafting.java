package net.fabricmc.automated_crafting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.BlockContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AutomatedCrafting implements ModInitializer {
	// an instance of our new item
	public static final AutoCrafter AUTO_CRAFTER = new AutoCrafter(FabricBlockSettings.of(Material.ANVIL).build());
	public static BlockEntityType<AutoCrafterEntity> AUTO_CRAFTER_ENTITY;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.BLOCK, AutoCrafter.ID, AUTO_CRAFTER);
		Registry.register(Registry.ITEM, new Identifier("automated_crafting", "auto_crafter"), new BlockItem(AUTO_CRAFTER, new Item.Settings().group(ItemGroup.MISC)));
		AUTO_CRAFTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "automated_crafting:auto_crafter_entity", BlockEntityType.Builder.create(AutoCrafterEntity::new, AUTO_CRAFTER).build(null));

		ContainerProviderRegistry.INSTANCE.registerFactory(AutoCrafter.ID, (syncId, id, player, buf) -> new AutoCrafterController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

	}
}
