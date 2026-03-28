package ru.blatfan.blatapi.compat.ftb_quests.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageItemStackFTBReward extends AbstractPlayerStageReward {
    public static RewardType TYPE;
    private ItemStack value;
    
    public PlayerStageItemStackFTBReward(long id, Quest q) {
        super(id, q);
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addItemStack("value", value, v->value=v, ItemStack.EMPTY, false, false);
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.put("value", value.serializeNBT());
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeItemStack(value, false);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        value = ItemStack.of(nbt.getCompound("value"));
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        value = buffer.readItem();
    }
    
    @Override
    protected void setStage(Player player) {
        PlayerStages.setItemStack(player, stage, value);
    }
    
    @Override
    public RewardType getType() {
        return TYPE;
    }
    
}
