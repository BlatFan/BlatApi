package ru.blatfan.blatapi.compat.ftb_teams;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.utils.TeamHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IFTBTeam implements TeamHelper.ITeam {
    
    @Override
    public List<ServerPlayer> getTeammates(Player rplayer) {
        if (!(rplayer instanceof ServerPlayer player)) return List.of();
        
        Optional<Team> team = getTeam(player);
        List<ServerPlayer> list = new ArrayList<>();
        
        if (team.isPresent()) {
            for (UUID memberId : team.get().getMembers()) {
                ServerPlayer uPlayer = player.server.getPlayerList().getPlayer(memberId);
                if (uPlayer != null)
                    list.add(uPlayer);
            }
        }
        return list;
    }
    
    @Override
    public void sendMessage(Player rplayer, String text) {
        if (!(rplayer instanceof ServerPlayer player)) return;
        getTeam(player).ifPresent(team -> team.sendMessage(Util.NIL_UUID, text));
    }
    
    private static Optional<Team> getTeam(ServerPlayer player){
        return FTBTeamsAPI.api().getManager().getTeamForPlayer(player);
    }
}
