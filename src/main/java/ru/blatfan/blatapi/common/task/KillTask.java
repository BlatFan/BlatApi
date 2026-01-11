package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import com.mojang.math.Axis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.GuiUtil;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;

@AllArgsConstructor
@Getter
public class KillTask extends Task {
    private boolean visible;
    private ResourceLocation entity;
    
    public static String getStage(ResourceLocation entity){
        return "player_kill_"+entity.getNamespace()+"_"+entity.getPath();
    }
    public static String getStage(EntityType<?> type){
        ResourceLocation entity = ForgeRegistries.ENTITY_TYPES.getKey(type);
        if(entity==null) return "EMPTY";
        return getStage(entity);
    }
    
    @Override
    public boolean get(Player player) {
        return PlayerStages.getBool(player, getStage(entity));
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("kill");
    }
    
    public EntityType<?> getEntity(){
        return BuiltInRegistries.ENTITY_TYPE.get(entity);
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.kill").add(getEntity().getDescriptionId()).withColor(Color.WHITE);
    }
    
    @Override
    public void render(GuiGraphics gui, int x, int y, int mX, int mY, Player player) {
        super.render(gui, x+16, y, mX, mY, player);
        Entity e = BuiltInRegistries.ENTITY_TYPE.get(entity).create(GuideClient.level);
        if(!(e instanceof LivingEntity living)) return;
        living.yHeadRot=0;
        living.yHeadRotO=0;
        gui.enableScissor(x, y, x+16, y+16);
        GuiUtil.renderEntityQuaternionf(gui, x+8, y+16, 8, Axis.YP.rotationDegrees(ClientTicks.ticks), (Axis.XP.rotationDegrees(180)), living);
        gui.disableScissor();
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer(){}
        @Override
        public Task fromJson(JsonObject json) {
            boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation entity = ResourceLocation.tryParse(json.get("entity").getAsString());
            PlayerStages.allStages.add(getStage(entity));
            return new KillTask(b, entity);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean b = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation entity = ResourceLocation.tryParse(tag.getString("entity"));
            PlayerStages.allStages.add(getStage(entity));
            return new KillTask(b, entity);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if(task instanceof KillTask task1){
                tag.putBoolean("visible", task1.visible);
                tag.putString("entity", task1.entity.toString());
            }
            return tag;
        }
    }
}