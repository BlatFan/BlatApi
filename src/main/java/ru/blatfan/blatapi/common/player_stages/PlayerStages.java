package ru.blatfan.blatapi.common.player_stages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import ru.blatfan.blatapi.fluffy_fur.common.network.FluffyFurPacketHandler;
import ru.blatfan.blatapi.utils.ICapacity;

import java.util.*;

public class PlayerStages implements ICapacity<PlayerStages> {
    public static final List<String> allStages = new ArrayList<>();
    private final Map<String, Boolean> data = new HashMap<>();
    
    private static boolean sendEvent(Player player, String key, boolean value){
        PlayerStageEvent event = new PlayerStageEvent(player, key, value);
        MinecraftForge.EVENT_BUS.post(event);
        return event.isValue();
    }
    
    @Override
    public void sync(Entity entity) {
        if(entity instanceof Player player)
            FluffyFurPacketHandler.sendTo(player, new PlayerStagesSync(this));
    }
    
    public Map<String, Boolean> getAll(){
        return Collections.unmodifiableMap(data);
    }
    
    public static PlayerStages get(Player player){
        return player.getCapability(PlayerStagesProvider.CAPABILITY, null).orElse(new PlayerStages());
    }
    
    public static boolean get(Player player, String key){
        PlayerStages stages = get(player);
        return stages.data.getOrDefault(key, false);
    }
    
    public static void add(Player player, String key){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.data.put(key, sendEvent(player, key, true));
            playerStages.sync(player);
        });
    }
    
    public static void set(Player player, String key, boolean b){
        if(!allStages.contains(key)) allStages.add(key);
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.data.put(key, sendEvent(player, key, b));
            playerStages.sync(player);
        });
    }
    
    public static void remove(Player player, String key){
        player.getCapability(PlayerStagesProvider.CAPABILITY, null).ifPresent(playerStages -> {
            playerStages.data.put(key, sendEvent(player, key, false));
            playerStages.sync(player);
        });
    }
    
    @Override
    public Tag toNBT() {
        CompoundTag nbt = new CompoundTag();
        for(String key : data.keySet())
            nbt.putBoolean(key, data.get(key));
        return nbt;
    }
    
    @Override
    public void fromNBT(Tag tag) {
        data.clear();
        CompoundTag nbt = (CompoundTag) tag;
        for(String key : nbt.getAllKeys())
            data.put(key, nbt.getBoolean(key));
    }
    
    @Override
    public void copy(PlayerStages instance) {
        data.clear();
        for(String key : instance.data.keySet())
            data.put(key, instance.data.get(key));
    }
    
    @Override
    public PlayerStages clone() {
        PlayerStages instance = new PlayerStages();
        for(String key : data.keySet())
            instance.data.put(key, data.get(key));
        return instance;
    }
}
