package ru.blatfan.blatapi.common.events;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlatBlockEvents extends BlatEvent {

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack().isEmpty()) {
      return;
    }
    BlockState stateHit = event.getLevel().getBlockState(event.getPos());
    if (stateHit.getBlock() instanceof BlatBlock block) {
      block.onRightClickBlock(event, stateHit);
    }
  }
}
