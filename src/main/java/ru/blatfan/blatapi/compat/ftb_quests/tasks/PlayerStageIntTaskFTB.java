package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageIntTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    private int min = 0;
    private int max = 0;
    
    public PlayerStageIntTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        if (!PlayerStages.has(serverPlayer, stage)) return false;
        int val = PlayerStages.getInt(serverPlayer, stage);
        return val >= min && val <= max;
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addInt("min", min, v -> min = v, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        config.addInt("max", max, v -> max = v, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putInt("min", min);
        nbt.putInt("max", max);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        min = nbt.getInt("min");
        max = nbt.getInt("max");
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarInt(min);
        buffer.writeVarInt(max);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        min = buffer.readVarInt();
        max = buffer.readVarInt();
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
