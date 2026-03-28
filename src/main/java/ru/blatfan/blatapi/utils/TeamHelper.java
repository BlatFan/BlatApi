package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class TeamHelper {
    public static final Map<String, ITeam> TEAMS = new ConcurrentHashMap<>();
    
    public static List<ServerPlayer> getTeammates(Player player){
        List<ServerPlayer> teammates = new ArrayList<>();
        
        TEAMS.values().forEach(value -> teammates.addAll(value.getTeammates(player)));
        
        return teammates;
    }
    
    public static void sendMessage(Player player, String text){
        TEAMS.values().forEach(value -> value.sendMessage(player, text));
    }
    
    public interface ITeam {
        List<ServerPlayer> getTeammates(Player player);
        void sendMessage(Player player, String text);
    }
}