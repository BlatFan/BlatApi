package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageDoubleTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    private double min = 0;
    private double max = 0;
    
    public PlayerStageDoubleTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        if (!PlayerStages.has(serverPlayer, stage)) return false;
        double val = PlayerStages.getDouble(serverPlayer, stage);
        return val >= min && val <= max;
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addDouble("min", min, v -> min = v, 1, Double.MIN_VALUE, Double.MAX_VALUE);
        config.addDouble("max", max, v -> max = v, 3, Double.MIN_VALUE, Double.MAX_VALUE);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putDouble("min", min);
        nbt.putDouble("max", max);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        min = nbt.getDouble("min");
        max = nbt.getDouble("max");
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeDouble(min);
        buffer.writeDouble(max);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        min = buffer.readDouble();
        max = buffer.readDouble();
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
