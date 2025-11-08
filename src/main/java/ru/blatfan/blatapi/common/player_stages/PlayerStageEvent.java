package ru.blatfan.blatapi.common.player_stages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@AllArgsConstructor@Getter@Cancelable
public class PlayerStageEvent extends Event {
    private final Player player;
    private final String stage;
    
    @Getter
    public static class Set<T> extends PlayerStageEvent {
        @Setter
        private PlayerStages.Value<T> value;
        
        public Set(Player player, String stage, PlayerStages.Value<T> value) {
            super(player, stage);
            this.value=value;
        }
    }
    
    public static class Remove extends PlayerStageEvent {
        public Remove(Player player, String stage) {
            super(player, stage);
        }
    }
}