package ru.blatfan.blatapi.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.blatfan.blatapi.block.BlatBlock;

public abstract class EntityBlatBlock extends BlatBlock implements EntityBlock {

  public EntityBlatBlock(Properties prop, BlatBlock.Settings custom) {
    super(prop, custom);
  }

  /**
   * Has tooltip by default
   * 
   * @param prop
   */
  public EntityBlatBlock(Properties prop) {
    this(prop, new BlatBlock.Settings().tooltip());
  }

  @Override
  public RenderShape getRenderShape(BlockState bs) {
    return RenderShape.MODEL;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean triggerEvent(BlockState bs, Level level, BlockPos pos, int a, int b) {
    super.triggerEvent(bs, level, pos, a, b);
    BlockEntity blockentity = level.getBlockEntity(pos);
    return blockentity == null ? false : blockentity.triggerEvent(a, b);
  }

  @Override
  public MenuProvider getMenuProvider(BlockState bs, Level level, BlockPos pos) {
    BlockEntity blockentity = level.getBlockEntity(pos);
    return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
  }

  @SuppressWarnings("unchecked")
  protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> etype, BlockEntityTicker<? super E> ticker) {
    return etype == type ? (BlockEntityTicker<A>) ticker : null;
  }
}
