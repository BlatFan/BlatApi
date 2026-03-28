package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageStringTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    private String value = "";
    
    public PlayerStageStringTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        return PlayerStages.has(serverPlayer, stage) && PlayerStages.getString(serverPlayer, stage).equals(value);
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
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        value = nbt.getString("value");
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(value);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        value = buffer.readUtf();
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
