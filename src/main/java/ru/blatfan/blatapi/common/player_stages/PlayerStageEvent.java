package ru.blatfan.blatapi.common.player_stages;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class PlayerStageEvent extends PlayerEvent {
    @Getter
    @Setter
    private ResourceLocation key;
    
    public PlayerStageEvent(Player player, ResourceLocation key) {
        super(player);
        this.key = key;
    }
    
    public static class Create extends PlayerStageEvent {
        public Create(Player player, ResourceLocation key) {
            super(player, key);
        }
    }
    
    public static class PlayerValueEvent<T> extends PlayerStageEvent {
        @Setter@Getter
        private PlayerStages.Value<T> value;
        
        public PlayerValueEvent(ResourceLocation key, Player player, PlayerStages.Value<T> value) {
            super(player, key);
            this.value = value;
        }
    }
    
    public static class Add<T> extends PlayerValueEvent<T> {
        public Add(ResourceLocation key, Player player, PlayerStages.Value<T> value) {
            super(key, player, value);
        }
    }
    
    public static class Set<T> extends PlayerValueEvent<T> {
        public Set(ResourceLocation key, Player player, PlayerStages.Value<T> value) {
            super(key, player, value);
        }
    }
    
    public static class Remove extends PlayerStageEvent {
        public Remove(Player player, ResourceLocation key) {
            super(player, key);
        }
    }
}