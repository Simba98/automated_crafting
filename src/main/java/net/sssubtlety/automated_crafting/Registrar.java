package net.sssubtlety.automated_crafting;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.sssubtlety.automated_crafting.gui.AutoCrafterGuiDescription;

import static net.sssubtlety.automated_crafting.AutomatedCrafting.NAMESPACE;

public interface Registrar {
    static void init() { }

    Block BLOCK = Registry.register(Registry.BLOCK, AutoCrafterBlock.ID, createAutoCrafterBlock());

    BlockItem ITEM = Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "auto_crafter"),
            new BlockItem(Registrar.BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));

    BlockEntityType<AutoCrafterBlockEntity> BLOCK_ENTITY_TYPE =
            Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                NAMESPACE + ":auto_crafter_entity",
                    FabricBlockEntityTypeBuilder.create(AutoCrafterBlockEntity::new, Registrar.BLOCK).build(null)
            );

    ScreenHandlerType<AutoCrafterGuiDescription> SCREEN_HANDLER_TYPE =
            ScreenHandlerRegistry.registerSimple(AutoCrafterBlock.ID, (world, playerInventory) -> AutoCrafterGuiDescription.create(world, playerInventory, ScreenHandlerContext.EMPTY));


    private static Block createAutoCrafterBlock() throws RuntimeException {
        try {
            return new AutoCrafterBlock(FabricBlockSettings.of(new Material(MapColor.GRAY, false, true, true, true, false, false, PistonBehavior.BLOCK)).strength(1, 3).breakByHand(true).build()
            );
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Unable to construct auto crafter block!", e);
        }
    }
}
