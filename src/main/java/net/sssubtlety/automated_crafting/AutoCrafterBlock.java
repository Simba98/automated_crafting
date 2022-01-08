package net.sssubtlety.automated_crafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class AutoCrafterBlock extends BlockWithEntity { //implements BlockEntityProvider {
    public static final Identifier ID = new Identifier("automated_crafting", "auto_crafter");
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    protected static void logWrongBlockEntityError() {
        AutomatedCrafting.LOGGER.error("AutoCrafterBlock found block entity that was not an AutoCrafterBlockEntity.");
    }

    public AutoCrafterBlock(Settings settings) throws IllegalAccessException, InstantiationException {
        super(settings);
        this.setDefaultState((((this.stateManager.getDefaultState()).with(POWERED, false).with(ACTIVATED, false))));
    }

    protected boolean isPowered(World world, BlockPos pos) {
        return world.isReceivingRedstonePower(pos) || Config.isQuasiConnected() && world.isReceivingRedstonePower(pos.up());
    }

    protected void tryCraft(ServerWorld world, BlockPos pos) {
        BlockPointerImpl blockPointerImpl = new BlockPointerImpl(world, pos);
        ((AutoCrafterBlockEntity)blockPointerImpl.getBlockEntity()).tryCraft();
        world.updateNeighborsAlways(pos, this);
    }

    @Override
    public AutoCrafterBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AutoCrafterBlockEntity(pos, state);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            if (world.getBlockEntity(pos) instanceof AutoCrafterBlockEntity autoCrafterBlockEntity) {
                player.openHandledScreen(autoCrafterBlockEntity);
                return ActionResult.CONSUME;
            } else {
                logWrongBlockEntityError();
                return ActionResult.PASS;
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        boolean isPowered = isPowered(world, pos);
        boolean wasPowered = state.get(POWERED);

        if(isPowered) {
            if(!wasPowered){
                //isPowered && !wasPowered
                //powering on
                world.setBlockState(pos, state.with(POWERED, true).with(ACTIVATED, true), 2);

                if (world instanceof ServerWorld)
                    world.scheduleBlockTick(pos, this, 2);

            }
        } else
            world.setBlockState(pos, state.with(POWERED, false).with(ACTIVATED, false), 2);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!Config.doesCraftContinuously() && state.get(ACTIVATED))
            world.setBlockState(pos, state.with(ACTIVATED, false), 2);

        tryCraft(world, pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof AutoCrafterBlockEntity autoCrafterBlockEntity) {
                ItemScatterer.spawn(world, pos, autoCrafterBlockEntity.getTrimmed());
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
        if (world.getBlockEntity(pos) instanceof AutoCrafterBlockEntity autoCrafterBlockEntity)
            return autoCrafterBlockEntity.getComparatorOutput();
        else {
            logWrongBlockEntityError();
            return 0;
        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return Config.doesRedirectRedstone();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(ACTIVATED);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        neighborUpdate(state, world, pos, this, pos, false);
    }
}
