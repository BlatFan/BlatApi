package ru.blatfan.blatapi.compat.ftb_quests;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import ru.blatfan.blatapi.compat.ftb_quests.reward.*;
import ru.blatfan.blatapi.compat.ftb_quests.tasks.*;

import java.util.function.Supplier;

import static ru.blatfan.blatapi.BlatApi.guiLoc;
import static ru.blatfan.blatapi.BlatApi.loc;

public class FTBQuestsBA {
    public static final Supplier<Icon> icon = ()-> Icon.getIcon(guiLoc("ftb/player_stage"));
    
    public static void init(){
        PlayerStageBoolTaskFTB.TYPE=TaskTypes.register(loc("player_stage_bool"), PlayerStageBoolTaskFTB::new, icon);
        PlayerStageIntTaskFTB.TYPE=TaskTypes.register(loc("player_stage_int"), PlayerStageIntTaskFTB::new, icon);
        PlayerStageDoubleTaskFTB.TYPE=TaskTypes.register(loc("player_stage_double"), PlayerStageDoubleTaskFTB::new, icon);
        PlayerStageFloatTaskFTB.TYPE=TaskTypes.register(loc("player_stage_float"), PlayerStageFloatTaskFTB::new, icon);
        PlayerStageLongTaskFTB.TYPE=TaskTypes.register(loc("player_stage_long"), PlayerStageLongTaskFTB::new, icon);
        PlayerStageStringTaskFTB.TYPE=TaskTypes.register(loc("player_stage_string"), PlayerStageStringTaskFTB::new, icon);
        PlayerStageItemStackTaskFTB.TYPE=TaskTypes.register(loc("player_stage_itemstack"), PlayerStageItemStackTaskFTB::new, icon);
        
        PlayerStageBoolFTBReward.TYPE=RewardTypes.register(loc("player_stage_bool"), PlayerStageBoolFTBReward::new, icon);
        PlayerStageIntFTBReward.TYPE=RewardTypes.register(loc("player_stage_int"), PlayerStageIntFTBReward::new, icon);
        PlayerStageDoubleFTBReward.TYPE=RewardTypes.register(loc("player_stage_double"), PlayerStageDoubleFTBReward::new, icon);
        PlayerStageFloatFTBReward.TYPE=RewardTypes.register(loc("player_stage_float"), PlayerStageFloatFTBReward::new, icon);
        PlayerStageLongFTBReward.TYPE=RewardTypes.register(loc("player_stage_long"), PlayerStageLongFTBReward::new, icon);
        PlayerStageStringFTBReward.TYPE=RewardTypes.register(loc("player_stage_string"), PlayerStageStringFTBReward::new, icon);
        PlayerStageItemStackFTBReward.TYPE=RewardTypes.register(loc("player_stage_itemstack"), PlayerStageItemStackFTBReward::new, icon);
    }
}