package ru.blatfan.blatapi.common.player_stages;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.BlatApi;

public class PlayerStagesEvents {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)) {
            event.addCapability(BlatApi.loc("player_stages"), new PlayerStagesProvider());
        }
    }
    
    @SubscribeEvent
    public static void onPlayerLogged(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            PlayerStages.get(event.getEntity()).sync(event.getEntity());
        }
    }
    
    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            PlayerStages.get(event.getEntity()).sync(event.getEntity());
        }
    }
    
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            PlayerStages.get(event.getEntity()).sync(event.getEntity());
        }
    }
    
    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().revive();
        PlayerStages original = PlayerStages.get(event.getOriginal());
        PlayerStages clone = PlayerStages.get(event.getEntity());
        clone.copy(original);
        event.isWasDeath();
    }
}
