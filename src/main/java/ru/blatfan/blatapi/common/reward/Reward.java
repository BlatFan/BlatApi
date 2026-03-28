package ru.blatfan.blatapi.common.reward;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;
import ru.blatfan.blatapi.common.network.RewardPacket;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Reward {
    public static final Map<String, Function<JsonObject, Reward>> TYPES = new HashMap<>();
    public static final Map<String, Function<CompoundTag, Reward>> TYPES_TAG = new HashMap<>();
    
    private static boolean init = false;
    public static void init(){
        if(init) return;
        TYPES.put("attribute", AttributeReward::fromJson);
        TYPES_TAG.put("attribute", AttributeReward::fromTag);
        TYPES.put("command", CommandReward::fromJson);
        TYPES_TAG.put("command", CommandReward::fromTag);
        TYPES_TAG.put("close_menu", CloseMenuReward::fromTag);
        init=true;
    }
    
    public static void applyList(List<Reward> rewards){
        List<Reward> r = new ArrayList<>();
        for (Reward reward : rewards)
            if(reward instanceof ClientReward) reward.apply(Minecraft.getInstance().player);
            else r.add(reward);
        if(!r.isEmpty()) BlatApiPacketHandler.sendToServer(new RewardPacket(r));
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
    
    public abstract Text text(Player player);
}