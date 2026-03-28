package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageLongTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    private long min = 0;
    private long max = 0;
    
    public PlayerStageLongTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        if (!PlayerStages.has(serverPlayer, stage)) return false;
        long val = PlayerStages.getLong(serverPlayer, stage);
        return val >= min && val <= max;
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addLong("min", min, v -> min = v, 1, Long.MIN_VALUE, Long.MAX_VALUE);
        config.addLong("max", max, v -> max = v, 3, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putLong("min", min);
        nbt.putLong("max", max);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        min = nbt.getLong("min");
        max = nbt.getLong("max");
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeLong(min);
        buffer.writeLong(max);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        min = buffer.readLong();
        max = buffer.readLong();
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
