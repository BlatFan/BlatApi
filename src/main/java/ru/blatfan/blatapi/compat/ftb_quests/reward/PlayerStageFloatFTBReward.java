package ru.blatfan.blatapi.compat.ftb_quests.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.compat.ftb_quests.config.FloatConfig;

public class PlayerStageFloatFTBReward extends AbstractPlayerStageReward {
    public static RewardType TYPE;
    private float value;
    
    public PlayerStageFloatFTBReward(long id, Quest q) {
        super(id, q);
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("value", new FloatConfig(), value, v->value=v, 0f);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putFloat("value", value);
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeFloat(value);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        value = nbt.getFloat("value");
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        value = buffer.readFloat();
    }
    
    @Override
    protected void setStage(Player player) {
        PlayerStages.setFloat(player, stage, value);
    }
    
    @Override
    public RewardType getType() {
        return TYPE;
    }
    
}
