package ru.blatfan.blatapi.common.reward;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.fluffy_fur.common.network.FluffyFurPacketHandler;
import ru.blatfan.blatapi.fluffy_fur.common.network.RewardPacket;

import java.util.*;
import java.util.function.Function;

public abstract class Reward {
    public static final Map<String, Function<JsonObject, Reward>> TYPES = new HashMap<>();
    public static final Map<String, Function<CompoundTag, Reward>> TYPES_TAG = new HashMap<>();
    static {
        TYPES.put("attribute", AttributeReward::fromJson);
        TYPES_TAG.put("attribute", AttributeReward::fromTag);
        TYPES.put("command", CommandReward::fromJson);
        TYPES_TAG.put("command", CommandReward::fromTag);
        TYPES_TAG.put("close_menu", CloseMenuReward::fromTag);
    }
    
    public static void applyList(List<Reward> rewards){
        List<Reward> r = new ArrayList<>();
        for (Reward reward : rewards)
            if(reward instanceof ClientReward) reward.apply(Minecraft.getInstance().player);
            else r.add(reward);
        if(!r.isEmpty()) FluffyFurPacketHandler.sendToServer(new RewardPacket(r));
    }
    
    public abstract void apply(Player player);
    public abstract boolean isVisible();
    
    public static Reward fromJson(JsonObject json){
        JsonObject data = json.get("data").getAsJsonObject();
        String type = json.get("type").getAsString();
        return TYPES.containsKey(type) ? TYPES.get(type).apply(data) : null;
    }
    public abstract CompoundTag toTag();
    public static Reward fromTag(CompoundTag tag){
        String type = tag.getString("type");
        return TYPES_TAG.containsKey(type) ? TYPES_TAG.get(type).apply(tag) : null;
    }
    
    public abstract Component text(Player player);
}