package org.sequoia.riverreeds.block;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.sequoia.riverreeds.item.ModItems;

public class CattailCropBlock extends CropBlock {
    public static final int LOWER_MAX_AGE = 0;
    public static final int UPPER_MAX_AGE = 7;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        int age = this.getAge(state);
        BlockState ground = world.getBlockState(pos.down());

        if(age <= LOWER_MAX_AGE) {
            boolean validGround = ground.isOf(Blocks.DIRT)
                    || ground.isOf(Blocks.GRASS_BLOCK)
                    || ground.isOf(Blocks.PODZOL)
                    || ground.isOf(Blocks.GRAVEL)
                    || ground.isOf(this) && ground.get(AGE) == 1;

            boolean isInWater = world.getFluidState(pos).isIn(FluidTags.WATER);
            boolean isWaterAbove = world.getFluidState(pos.up()).isIn(FluidTags.WATER);

            return validGround && isInWater && !isWaterAbove;
        }

        return ground.isOf(this) && ground.get(AGE) == LOWER_MAX_AGE;
    }

    public CattailCropBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) < 9) {
            return;
        }
        int age = this.getAge(state);

        if (age < LOWER_MAX_AGE) {
            if(random.nextInt(10) == 0) {
                world.setBlockState(pos, this.withAge(age + 1), 2);
            }
        } else if (age == LOWER_MAX_AGE) {
            if(world.getBlockState(pos.up()).isAir()) {
                world.setBlockState(pos.up(1), this.withAge(LOWER_MAX_AGE + 1), 2);
            }
        } else {
            if(random.nextInt(10) == 0) {
                world.setBlockState(pos, this.withAge(age + 1), 2);
            }
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        if(this.getAge(state) == UPPER_MAX_AGE) {
            return false;
        }
        BlockState above = world.getBlockState(pos.up(1));
        return !above.isOf(this);
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        int growth = this.getGrowthAmount(world);

        if(age == LOWER_MAX_AGE && world.getBlockState(pos.up(1)).isAir()) {
            int newAge = Math.min(getMaxAge(), LOWER_MAX_AGE + growth);
            world.setBlockState(pos.up(1), this.withAge(newAge), 2);
        } else if(age > LOWER_MAX_AGE) {
            int newAge = Math.min(getMaxAge(), age + growth);
            world.setBlockState(pos, this.withAge(newAge), 2);
        } else {
            int newAge = Math.min(age + growth, LOWER_MAX_AGE);
            world.setBlockState(pos, this.withAge(newAge), 2);
        }
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.CATTAIL_SEED;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[getAge(state)];
    }

    @Override
    public int getMaxAge() {
        return LOWER_MAX_AGE + UPPER_MAX_AGE;
    }

    @Override
    protected IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    //  necessary so that the cattails are not destroyed by water
    @Override
    public boolean canBucketPlace(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        int age = this.getAge(state);

        if(age <= LOWER_MAX_AGE) {
            return Fluids.WATER.getDefaultState();
        }
        return Fluids.EMPTY.getDefaultState();
    }
}