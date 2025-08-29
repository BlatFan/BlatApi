package ru.blatfan.blatapi.common.player_stages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;
import ru.blatfan.blatapi.fluffy_fur.common.network.CapacitySync;

public class PlayerStagesSync extends CapacitySync<PlayerStages> {
    public PlayerStagesSync(PlayerStages capacity) {
        super(capacity);
    }
    
    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PlayerStagesSync.class, PlayerStagesSync::toBuf, PlayerStagesSync::buf, CapacitySync::handler);
    }
    
    public static PlayerStagesSync buf(FriendlyByteBuf buf) {
        PlayerStages stages = new PlayerStages();
        stages.fromNBT(buf.readNbt());
        return new PlayerStagesSync(stages);
    }
    
    @Override
    public PlayerStages get(Player player) {
        return PlayerStages.get(player);
    }
}