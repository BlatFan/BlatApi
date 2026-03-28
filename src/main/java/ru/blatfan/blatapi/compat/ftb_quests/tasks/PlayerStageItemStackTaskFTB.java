package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.utils.ItemHelper;

public class PlayerStageItemStackTaskFTB extends AbstractPlayerStageFTBTask {
    public static TaskType TYPE;
    private ItemStack value = ItemStack.EMPTY;
    private boolean tagCheck=false;
    private boolean countCheck=false;
    
    public PlayerStageItemStackTaskFTB(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        ItemStack stageItem = PlayerStages.getItemStack(serverPlayer, stage);
        return PlayerStages.has(serverPlayer, stage) &&
            (tagCheck ? ItemHelper.areStacksEqual(stageItem, value) : stageItem.is(value.getItem())) &&
            (!countCheck || stageItem.getCount() == value.getCount());
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addItemStack("value", value, v->value=v, ItemStack.EMPTY, false, false);
        config.addBool("tag_check", tagCheck, v->tagCheck=v, false);
        config.addBool("count_check", countCheck, v->countCheck=v, false);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.put("value", value.serializeNBT());
        nbt.putBoolean("tag_check", tagCheck);
        nbt.putBoolean("count_check", countCheck);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        value=ItemStack.of(nbt.getCompound("value"));
        tagCheck = nbt.getBoolean("tag_check");
        countCheck = nbt.getBoolean("count_check");
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeItemStack(value, false);
        buffer.writeBoolean(tagCheck);
        buffer.writeBoolean(countCheck);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        value = buffer.readItem();
        tagCheck = buffer.readBoolean();
        countCheck = buffer.readBoolean();
    }
    
    @Override
    public TaskType getType() {
        return TYPE;
    }
}
