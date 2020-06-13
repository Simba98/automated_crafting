package net.sssubtlety.automated_crafting;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class AutoCrafterBlock extends BlockWithEntity implements AutoCrafterSharedData { //implements BlockEntityProvider {
    static final Identifier ID = new Identifier("automated_crafting", "auto_crafter");
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");


    public AutoCrafterBlock(Settings settings)
    {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ctx) {
//        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 0.5f);
//    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new AutoCrafterBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.CONSUME;

        BlockEntity be = world.getBlockEntity(pos);
        if (be!=null && be instanceof AutoCrafterBlockEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(AutoCrafterBlock.ID, player, (packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        if (QUASI_CONNECTIVITY && !isPowered) {
            isPowered = world.isReceivingRedstonePower(pos.up());
        }

        boolean wasPowered = state.get(POWERED);
        if(isPowered) {
            if(!wasPowered) {
                //isPowered && !wasPowered
                //powering on
                BlockState powered = state.with(POWERED, true);
                world.setBlockState(pos, powered, 2);
                world.getBlockTickScheduler().schedule(pos, this, 2);
            }
            //else is+was powered
            //staying powered
        } else if(wasPowered) {
            //!isPowered && wasPowered
            //de-powering
            world.setBlockState(pos, state.with(POWERED, false), 2);
        }
        //else !is+!was powered
        //staying un-powered
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPointerImpl blockPointerImpl = new BlockPointerImpl(world, pos);
        ((AutoCrafterBlockEntity)blockPointerImpl.getBlockEntity()).tryCraft();
        world.updateNeighborsAlways(pos, this);
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AutoCrafterBlockEntity) {
                ItemScatterer.spawn(world, pos, ((AutoCrafterBlockEntity)blockEntity).getInventory());
                world.updateHorizontalAdjacent(pos, this);
            }

            super.onBlockRemoved(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        AutoCrafterBlockEntity blockEntity = (AutoCrafterBlockEntity) world.getBlockEntity(pos);
        if(blockEntity == null) {
            System.err.println("automated_crafting: getComparatorOutput received null block entity. ");
        }

        DefaultedList<ItemStack> inventory = blockEntity.getInventory();
        int inputsOccupied = 0;

        for (int slot = (SIMPLE_MODE ? blockEntity.getInvSize() : 0); slot < OUTPUT_SLOT; slot++) {
            if(!inventory.get(slot).isEmpty()) { inputsOccupied++; }
        }
        float inputFillRatio = ((float)inputsOccupied) / blockEntity.getInvSize();

        ItemStack outputStack = inventory.get(OUTPUT_SLOT);
        float outputFillRatio = ((float)outputStack.getCount()) / outputStack.getMaxCount();

        int min = (inputFillRatio != 0 || outputFillRatio != 0 ? 1 : 0);

        float totalFillRatio = (inputFillRatio + outputFillRatio) / 2;

        return Math.max(Math.round(totalFillRatio * 15), min);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        neighborUpdate(state, world, pos, this, pos, false);
    }
}
