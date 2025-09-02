package org.sequoia.riverreeds.block;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.sequoia.riverreeds.item.ModItems;

public class CattailStemBlock extends CropBlock {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final int LOWER_MAX_AGE = 7;
    private static final VoxelShape[] LOWER_AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public CattailStemBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState ground = world.getBlockState(pos.down());

        boolean isValidGround = ground.isOf(Blocks.DIRT) ||
                                ground.isOf(Blocks.CLAY) ||
                                ground.isOf(Blocks.GRAVEL) ||
                                ground.isOf(Blocks.MUD);
        boolean isInStillWater = world.getFluidState(pos).isStill();
        boolean isInShallowWater = world.getFluidState(pos.up()).isEmpty();

        return isValidGround && isInStillWater && isInShallowWater;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) < 9) {
            return;
        }

        int currentAge = this.getAge(state);

        if(currentAge < LOWER_MAX_AGE) {
            world.setBlockState(pos, this.withAge(currentAge + 1), 2);
            currentAge++;
        }

        if(currentAge >= LOWER_MAX_AGE) {
            BlockPos above = pos.up();
            if(world.getBlockState(above).isAir()) {
                world.setBlockState(above, ModBlocks.CATTAIL_CROP.getDefaultState().with(CattailCropBlock.AGE, 0), 2);
            }
        }
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int newAge = this.getAge(state) + this.getGrowthAmount(world);

        if(newAge > LOWER_MAX_AGE) {
            newAge = LOWER_MAX_AGE;
            world.setBlockState(pos, this.withAge(newAge));
            if(world.getBlockState(pos.up(1)).isOf(Blocks.AIR)){
                world.setBlockState(pos.up(1), ModBlocks.CATTAIL_CROP.withAge(0), 2);
            }
        } else {
            world.setBlockState(pos, this.withAge(newAge), 2);
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !world.getBlockState(pos.up()).isOf(ModBlocks.CATTAIL_CROP);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.CATTAIL_SEED;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return LOWER_AGE_TO_SHAPE[getAge(state)];
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, WATERLOGGED);
    }

    @Override
    public boolean canBucketPlace(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getDefaultState() : Fluids.EMPTY.getDefaultState();
    }
}
