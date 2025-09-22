package ru.blatfan.blatapi.common.player_stages;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.task.CraftTask;
import ru.blatfan.blatapi.common.task.KillTask;

public class PlayerStagesEvents {
    @SubscribeEvent
    public static void onPlayerCraft(PlayerEvent.ItemCraftedEvent event){
        Item item = event.getCrafting().getItem();
        if(event.getEntity().level().isClientSide) return;
        if(PlayerStages.allStages.contains(CraftTask.getStage(item)))
            PlayerStages.add(event.getEntity(), CraftTask.getStage(item));
    }
    @SubscribeEvent
    public static void onPlayerKill(LivingDeathEvent event){
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        if(!source.is(DamageTypes.PLAYER_ATTACK)) return;
        if(source.getDirectEntity() instanceof Player player)
            if(PlayerStages.allStages.contains(KillTask.getStage(entity.getType())))
                PlayerStages.add(player, KillTask.getStage(entity.getType()));
    }
    
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
