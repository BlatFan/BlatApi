package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.server.level.ServerPlayer;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageBoolTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    
    public PlayerStageBoolTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        return PlayerStages.has(serverPlayer, stage) && PlayerStages.getBool(serverPlayer, stage);
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
