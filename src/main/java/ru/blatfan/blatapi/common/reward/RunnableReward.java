package ru.blatfan.blatapi.common.reward;

import lombok.AllArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
public class RunnableReward extends Reward {
    private final PlayerRunnable runnable;
    
    @Override
    public void apply(Player player) {
        runnable.run(player);
    }
    
    @Override
    public boolean isVisible() {
        return false;
    }
    
    @Override
    public CompoundTag toTag() {
        return new CompoundTag();
    }
    
    @Override
    public Text text(Player player) {
        return Text.create();
    }
}

