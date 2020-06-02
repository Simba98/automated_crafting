package net.fabricmc.automated_crafting;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AutoCrafter extends Block implements BlockEntityProvider {
    static final Identifier ID = new Identifier("automated_crafting", "auto_crafter");

    public AutoCrafter(Settings settings)
    {
        super(settings);
    }
//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ctx) {
//        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 0.5f);
//    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new AutoCrafterEntity();
    }
    @Override

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;
        Inventory blockEntity = (Inventory) world.getBlockEntity(pos);

        BlockEntity be = world.getBlockEntity(pos);
        if (be!=null && be instanceof AutoCrafterEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(AutoCrafter.ID, player, (packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
        }



//        if (player.getStackInHand(hand).isEmpty()) {
//            // If the player is not holding anything we'll get give him the items in the block entity one by one
//
//            // Find the first slot that has an item and give it to the player
//            if (!blockEntity.getInvStack(1).isEmpty()) {
//                // Give the player the stack in the inventory
//                player.inventory.offerOrDrop(world, blockEntity.getInvStack(1));
//                // Remove the stack from the inventory
//                blockEntity.removeInvStack(1);
//            } else if (!blockEntity.getInvStack(0).isEmpty()) {
//                player.inventory.offerOrDrop(world, blockEntity.getInvStack(0));
//                blockEntity.removeInvStack(0);
//            }
//        } else {
//            // player is holding an item
//
//            // Check what is the first open slot and put an item from the player's hand there
//            if (blockEntity.getInvStack(0).isEmpty()) {
//                // Put the stack the player is holding into the inventory
//                blockEntity.setInvStack(0, player.getStackInHand(hand).copy());
//                // Remove the stack from the player's hand
//                player.getStackInHand(hand).setCount(0);
//            } else if (blockEntity.getInvStack(1).isEmpty()) {
//                blockEntity.setInvStack(1, player.getStackInHand(hand).copy());
//                player.getStackInHand(hand).setCount(0);
//            } else {
//                // If the inventory is full we'll print it's contents
//                System.out.println("The first slot holds "
//                        + blockEntity.getInvStack(0) + " and the second slot holds " + blockEntity.getInvStack(1));
//            }
//        }
        return ActionResult.SUCCESS;
    }
}
