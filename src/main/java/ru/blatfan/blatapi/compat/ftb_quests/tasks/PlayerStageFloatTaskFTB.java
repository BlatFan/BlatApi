package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.compat.ftb_quests.config.FloatConfig;

public class PlayerStageFloatTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    private float min = 0;
    private float max = 0;
    
    public PlayerStageFloatTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        if (!PlayerStages.has(serverPlayer, stage)) return false;
        float val = PlayerStages.getFloat(serverPlayer, stage);
        return val >= min && val <= max;
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("min", new FloatConfig(), min, v -> min = v, 1f);
        config.add("max", new FloatConfig(), max, v -> max = v, 3f);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putFloat("min", min);
        nbt.putFloat("max", max);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        min = nbt.getFloat("min");
        max = nbt.getFloat("max");
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeFloat(min);
        buffer.writeFloat(max);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        min = buffer.readFloat();
        max = buffer.readFloat();
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
