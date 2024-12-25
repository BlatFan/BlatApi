package ru.blatfan.blatapi.entity;

import java.lang.ref.WeakReference;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import ru.blatfan.blatapi.block.BlatBlock;

public abstract class BlatBlockEntity extends BlockEntity {

  public BlatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    //    timer = tag.getInt("timer");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    //    tag.putInt("timer", timer);
    super.saveAdditional(tag);
  }

  public abstract void setField(int field, int value);

  public abstract int getField(int field);

  public void setLitProperty(boolean lit) {
    BlockState st = this.getBlockState();
    if (st.hasProperty(BlatBlock.LIT)) {
      boolean previous = st.getValue(BlatBlock.LIT);
      if (previous != lit) {
        this.level.setBlockAndUpdate(worldPosition, st.setValue(BlatBlock.LIT, lit));
      }
    }
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag syncData = super.getUpdateTag();
    this.saveAdditional(syncData);
    return syncData;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    this.load(pkt.getTag());
    super.onDataPacket(net, pkt);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public boolean isPowered() {
    return this.getLevel().hasNeighborSignal(this.getBlockPos());
  }

  public int getRedstonePower() {
    return this.getLevel().getBestNeighborSignal(this.getBlockPos());
  }

  public static InteractionResult playerAttackBreakBlock(WeakReference<FakePlayer> fakePlayer, Level world, BlockPos targetPos, InteractionHand hand, Direction facing) {
    if (fakePlayer == null) {
      return InteractionResult.FAIL;
    }
    try {
      fakePlayer.get().gameMode.handleBlockBreakAction(targetPos, ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, facing, world.getMaxBuildHeight(), 0);
      return InteractionResult.SUCCESS;
    }
    catch (Exception e) {
      return InteractionResult.FAIL;
    }
  }

  public static boolean tryHarvestBlock(WeakReference<FakePlayer> fakePlayer, Level world, BlockPos targetPos) {
    if (fakePlayer == null) {
      return false;
    }
    return fakePlayer.get().gameMode.destroyBlock(targetPos);
  }
}
