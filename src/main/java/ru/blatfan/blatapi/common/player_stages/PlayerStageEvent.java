package ru.blatfan.blatapi.common.player_stages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

@AllArgsConstructor@Getter
public class PlayerStageEvent extends Event {
    private final Player player;
    private final String stage;
    @Setter
    private boolean value;
}