package ru.blatfan.blatapi.common.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public abstract class BlockWaterlogFlib extends BlatBlock implements SimpleWaterloggedBlock {

  public BlockWaterlogFlib(Properties prop, BlatBlock.Settings custom) {
    super(prop, custom);
    registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
  }

  public BlockWaterlogFlib(Properties prop) {
    this(prop, new BlatBlock.Settings());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState stateForPlacement = super.getStateForPlacement(context);
    if (stateForPlacement == null) {
      return null;
    }
    return stateForPlacement.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
  }
}
