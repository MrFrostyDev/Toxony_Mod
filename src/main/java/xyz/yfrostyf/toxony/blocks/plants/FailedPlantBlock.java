package xyz.yfrostyf.toxony.blocks.plants;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import xyz.yfrostyf.toxony.blocks.PoisonFarmBlock;

public class FailedPlantBlock extends BushBlock {
    public static final MapCodec<FailedPlantBlock> CODEC = simpleCodec(FailedPlantBlock::new);
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    public FailedPlantBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState belowBlockState = level.getBlockState(blockpos);
        net.neoforged.neoforge.common.util.TriState soilDecision = belowBlockState.canSustainPlant(level, blockpos, Direction.UP, state);
        return soilDecision.isDefault() && belowBlockState.getBlock() instanceof PoisonFarmBlock;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }
}
