package ru.blatfan.blatapi.common.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class BlockAxisPillar extends Block {
  public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

  public BlockAxisPillar(Properties p) {
    super(p);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    return RotatedPillarBlock.rotatePillar(state, rot);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder.add(AXIS));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return super.getStateForPlacement(ctx).setValue(AXIS, ctx.getClickedFace().getAxis());
  }
}
