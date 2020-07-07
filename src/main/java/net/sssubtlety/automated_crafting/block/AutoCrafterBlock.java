package net.sssubtlety.automated_crafting.block;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.sssubtlety.automated_crafting.block.complexity.ComplexityMode;
import net.sssubtlety.automated_crafting.block.connectivity.Connectivity;
import net.sssubtlety.automated_crafting.blockEntity.AbstractAutoCrafterBlockEntity;

import java.util.Random;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.OUTPUT_SLOT;

public class AutoCrafterBlock<C extends Connectivity, M extends ComplexityMode> extends BlockWithEntity { //implements BlockEntityProvider {
    public static final Identifier ID = new Identifier("automated_crafting", "auto_crafter");
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");

    C connectivity;
    M mode;
    public AutoCrafterBlock(Settings settings, Class<C> connectivity, Class<M> mode) throws IllegalAccessException, InstantiationException {
        super(settings);
        this.connectivity = connectivity.newInstance();
        this.mode = mode.newInstance();
    }

    protected boolean isPowered(World world, BlockPos pos) {
        return connectivity.isPowered(world, pos);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return mode.getNewBlockEntity();
    }

//    protected abstract boolean getPowered(World world, BlockPos pos);
////                world.isReceivingRedstonePower(pos);
////        if (QUASI_CONNECTIVITY && !isPowered) {
////            isPowered = world.isReceivingRedstonePower(pos.up());
////        }
//
//    @Override
//    public abstract BlockEntity createBlockEntity(BlockView blockView);
//    {
//        return new AutoCrafterBlockEntity();
//    }
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if (world.isClient) return ActionResult.CONSUME;
//
//        BlockEntity be = world.getBlockEntity(pos);
//        if (be!=null && be instanceof AutoCrafterBlockEntity) {
//            ContainerProviderRegistry.INSTANCE.openContainer(AutoCrafterBlock.ID, player, (packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
//        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        boolean isPowered = isPowered(world, pos);

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
        //else !is && !was powered
        //staying un-powered
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPointerImpl blockPointerImpl = new BlockPointerImpl(world, pos);
        ((AbstractAutoCrafterBlockEntity)blockPointerImpl.getBlockEntity()).tryCraft();
        world.updateNeighborsAlways(pos, this);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AbstractAutoCrafterBlockEntity) {
                ItemScatterer.spawn(world, pos, (AbstractAutoCrafterBlockEntity)blockEntity);
                world.updateNeighbors(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof AbstractAutoCrafterBlockEntity)) {
            throw new IllegalStateException("AbstractAutoCrafterBlock's getComparatorOutput found unknown block entity. ");
        }
//        if(blockEntity == null) {
//            System.err.println("automated_crafting: getComparatorOutput received null block entity. ");
//        }

        DefaultedList<ItemStack> inventory = ((AbstractAutoCrafterBlockEntity)blockEntity).getInventory();
        int inputsOccupied = 0;

        for (int slot = ((AbstractAutoCrafterBlockEntity)blockEntity).getInputSlotInd(); slot < OUTPUT_SLOT; slot++) {
            if(!inventory.get(slot).isEmpty()) { inputsOccupied++; }
        }
        float inputFillRatio = ((float)inputsOccupied) / ((AbstractAutoCrafterBlockEntity)blockEntity).size();

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

//    public static boolean getBasicPower(World world, BlockPos pos) {
//        return world.isReceivingRedstonePower(pos);
//    }
}
