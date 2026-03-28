package ru.blatfan.blatapi.common.player_stages;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import ru.blatfan.blatapi.utils.capacity.CapacityProvider;

public class PlayerStagesProvider extends CapacityProvider<PlayerStages> {
    public static final Capability<PlayerStages> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    
    public PlayerStagesProvider() {
        super(CAPABILITY, PlayerStages::new);
    }
}