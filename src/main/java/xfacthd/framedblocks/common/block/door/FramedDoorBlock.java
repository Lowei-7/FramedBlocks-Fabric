package xfacthd.framedblocks.common.block.door;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import xfacthd.framedblocks.api.block.*;
import xfacthd.framedblocks.api.block.render.FramedBlockRenderProperties;
import xfacthd.framedblocks.common.data.BlockType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class FramedDoorBlock extends DoorBlock implements IFramedBlock
{
    private final BlockType type;

    private FramedDoorBlock(BlockType type, Properties props, BlockSetType blockSet)
    {
        super(blockSet, props);
        this.type = type;
        registerDefaultState(defaultBlockState()
                .setValue(FramedProperties.SOLID, false)
                .setValue(FramedProperties.GLOWING, false)
                .setValue(FramedProperties.PROPAGATES_SKYLIGHT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FramedProperties.SOLID, FramedProperties.GLOWING, FramedProperties.PROPAGATES_SKYLIGHT);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    )
    {
        InteractionResult result = handleUse(state, level, pos, player, hand, hit);
        return result.consumesAction() ? result : InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        //noinspection ConstantConditions
        super.setPlacedBy(level, pos, state, placer, stack);

        tryApplyCamoImmediately(level, pos, placer, stack);
        tryApplyCamoImmediately(level, pos.above(), placer, stack); //Apply to upper half as well
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess tickAccess,
            BlockPos currentPos,
            Direction facing,
            BlockPos facingPos,
            BlockState facingState,
            RandomSource random
    )
    {
        BlockState newState = super.updateShape(state, level, tickAccess, currentPos, facing, facingPos, facingState, random);
        if (newState == state)
        {
            updateCulling(level, currentPos);
        }
        return newState;
    }

    @Override
    public void neighborChanged(
            BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean isMoving
    )
    {
        super.neighborChanged(state, level, pos, block, orientation, isMoving);
        updateCulling(level, pos);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return useCamoOcclusionShapeForLightOcclusion(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state)
    {
        return getCamoOcclusionShape(state, null);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx)
    {
        return getCamoVisualShape(state, level, pos, ctx);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos)
    {
        return getCamoShadeBrightness(state, level, pos, super.getShadeBrightness(state, level, pos));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state)
    {
        return state.getValue(FramedProperties.PROPAGATES_SKYLIGHT);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
    {
        return getCamoDrops(super.getDrops(state, builder), builder);
    }

    @Override
    public BlockType getBlockType()
    {
        return type;
    }

    public void initializeClient(Consumer<IClientBlockExtensions> consumer)
    {
        consumer.accept(FramedBlockRenderProperties.INSTANCE);
    }



    public static FramedDoorBlock wood()
    {
        return new FramedDoorBlock(
                BlockType.FRAMED_DOOR,
                IFramedBlock.createProperties(BlockType.FRAMED_DOOR),
                BlockSetType.OAK
        );
    }

    public static FramedDoorBlock iron()
    {
        return new FramedDoorBlock(
                BlockType.FRAMED_IRON_DOOR,
                IFramedBlock.createProperties(BlockType.FRAMED_IRON_DOOR)
                        .requiresCorrectToolForDrops(),
                BlockSetType.IRON
        );
    }
}