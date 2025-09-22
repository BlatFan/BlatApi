package ru.blatfan.blatapi.common.task;

import com.google.gson.JsonObject;
import com.mojang.math.Axis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.GuiUtil;
import ru.blatfan.blatapi.utils.Text;

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
        ResourceLocation entity = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        return "player_kill_"+entity.getNamespace()+"_"+entity.getPath();
    }
    
    @Override
    public boolean get(Player player) {
        return PlayerStages.get(player, getStage(entity));
    }
    
    public EntityType<?> getEntity(){
        return BuiltInRegistries.ENTITY_TYPE.get(entity);
    }
    
    public static KillTask fromJson(JsonObject json){
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        ResourceLocation entity = ResourceLocation.tryParse(json.get("entity").getAsString());
        PlayerStages.allStages.add(getStage(entity));
        return new KillTask(b, entity);
    }
    
    @Override
    public Component text(Player player) {
        return Text.create("task.blatapi.kill").add(getEntity().getDescription()).withColor(Color.WHITE);
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
}