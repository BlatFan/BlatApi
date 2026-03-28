package ru.blatfan.blatapi.compat.ftb_quests.tasks;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.task.AbstractBooleanTask;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.compat.ftb_quests.config.ResourceLocationConfig;

public abstract class AbstractPlayerStageFTBTask extends AbstractBooleanTask {
    protected ResourceLocation stage = BlatApi.loc("empty");
    
    public AbstractPlayerStageFTBTask(long id, Quest quest) {
        super(id, quest);
    }
    
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("stage", new ResourceLocationConfig(), stage, v -> stage = v, BlatApi.loc("empty"));
    }
    
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return getType().getDisplayName().copy().append(": ")
            .append(Component.literal(this.stage.toString()).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
    
    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }
    
    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("stage", stage.toString());
    }
    
    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(stage);
    }
    
    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        String stageStr = nbt.getString("stage");
        this.stage = stageStr.isEmpty() ? BlatApi.loc("empty") : ResourceLocation.parse(stageStr);
    }
    
    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        stage = buffer.readResourceLocation();
    }
}
