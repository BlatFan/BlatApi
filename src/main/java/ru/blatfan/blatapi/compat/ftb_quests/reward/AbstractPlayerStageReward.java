package ru.blatfan.blatapi.compat.ftb_quests.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.net.DisplayRewardToastMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.compat.ftb_quests.FTBQuestsBA;
import ru.blatfan.blatapi.compat.ftb_quests.config.ResourceLocationConfig;
import ru.blatfan.blatapi.utils.TeamHelper;
import ru.blatfan.blatapi.utils.collection.Text;

public abstract class AbstractPlayerStageReward extends Reward {
    protected ResourceLocation stage = BlatApi.loc("empty");
    protected boolean forTeam = true;
    
    public AbstractPlayerStageReward(long id, Quest q) {
        super(id, q);
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("stage", new ResourceLocationConfig(), stage, v -> stage = v, BlatApi.loc("empty"));
        config.addBool("for_team", forTeam, v -> forTeam = v, true);
    }
    
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return getType().getDisplayName().copy().append(": ")
            .append(Component.literal(this.stage.toString()).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("stage", stage.toString());
        nbt.putBoolean("for_team", forTeam);
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(stage);
        buffer.writeBoolean(forTeam);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        String stageStr = nbt.getString("stage");
        this.stage = stageStr.isEmpty() ? BlatApi.loc("empty") : ResourceLocation.parse(stageStr);
        this.forTeam = !nbt.contains("for_team") || nbt.getBoolean("for_team");
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.stage = buffer.readResourceLocation();
        this.forTeam = buffer.readBoolean();
    }
    
    @Override
    public final void claim(ServerPlayer serverPlayer, boolean notify) {
        claimStage(serverPlayer, notify);
        
        DisplayRewardToastMessage msg = new DisplayRewardToastMessage(id, Text.create("Stage Reward"), FTBQuestsBA.icon.get());
        
        if (forTeam) {
            TeamHelper.getTeammates(serverPlayer).forEach(pl -> {
                setStage(pl);
                if (notify)
                    msg.sendTo(pl);
            });
            TeamHelper.sendMessage(serverPlayer, Component.literal("Stage " + stage + " achieved").getString());
        } else {
            setStage(serverPlayer);
            if (notify)
                msg.sendTo(serverPlayer);
        }
    }
    
    protected abstract void setStage(Player player);
    
    public void claimStage(ServerPlayer player, boolean notify) {}
}