package ru.blatfan.blatapi.compat.ftb_quests.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageStringFTBReward extends AbstractPlayerStageReward {
    public static RewardType TYPE;
    private String value;
    
    public PlayerStageStringFTBReward(long id, Quest q) {
        super(id, q);
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString("value", value, v->value=v, "");
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("value", value);
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(value);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        value = nbt.getString("value");
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        value = buffer.readUtf();
    }
    
    @Override
    protected void setStage(Player player) {
        PlayerStages.setString(player, stage, value);
    }
    
    @Override
    public RewardType getType() {
        return TYPE;
    }
    
}
