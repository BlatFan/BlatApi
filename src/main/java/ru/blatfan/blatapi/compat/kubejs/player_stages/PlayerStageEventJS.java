package ru.blatfan.blatapi.compat.kubejs.player_stages;

import dev.latvian.mods.kubejs.event.EventExit;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.common.player_stages.PlayerStageEvent;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;

public class PlayerStageEventJS extends PlayerEventJS {
    private final PlayerStageEvent forgeEvent;
    
    public PlayerStageEventJS(PlayerStageEvent forgeEvent) {
        this.forgeEvent = forgeEvent;
    }
    
    @Override
    public boolean hasGameStage(String key) {
        ResourceLocation stage = ResourceLocation.parse(key);
        return PlayerStages.has(getPlayer(), stage) && PlayerStages.getBool(getPlayer(), stage);
    }
    public boolean hasPlayerStage(ResourceLocation stage) {
        return PlayerStages.has(getPlayer(), stage) && PlayerStages.getBool(getPlayer(), stage);
    }
    
    @Override
    public void addGameStage(String key) {
        ResourceLocation stage = ResourceLocation.parse(key);
        PlayerStages.setBool(getPlayer(), stage, true);
    }
    public void addPlayerStage(ResourceLocation stage) {
        PlayerStages.setBool(getPlayer(), stage, true);
    }
    
    @Override
    public void removeGameStage(String key) {
        ResourceLocation stage = ResourceLocation.parse(key);
        PlayerStages.remove(getPlayer(), stage);
    }
    public void removePlayerStage(ResourceLocation stage) {
        PlayerStages.remove(getPlayer(), stage);
    }
    
    @Override
    public Player getEntity() {
        return forgeEvent.getEntity();
    }
    
    public ResourceLocation getStage() {
        return forgeEvent.getKey();
    }
    
    public Object getValue() {
        if (forgeEvent instanceof PlayerStageEvent.PlayerValueEvent<?> pve && pve.getValue()!=null)
            return pve.getValue().getValue();
        return null;
    }
    
    @Override
    public Object cancel() throws EventExit {
        if (forgeEvent.isCancelable())
            forgeEvent.setCanceled(true);
        return super.cancel();
    }
}