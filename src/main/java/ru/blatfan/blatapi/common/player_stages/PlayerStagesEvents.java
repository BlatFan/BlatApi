package ru.blatfan.blatapi.common.player_stages;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.guide_book.GuideBookPaper;
import ru.blatfan.blatapi.common.task.CraftTask;
import ru.blatfan.blatapi.common.task.EatTask;
import ru.blatfan.blatapi.common.task.ItemTask;
import ru.blatfan.blatapi.common.task.KillTask;
import ru.blatfan.blatapi.utils.PlayerUtil;

import java.util.Map;

public class PlayerStagesEvents {
    @SubscribeEvent
    public static void onPlayerEat(LivingEntityUseItemEvent.Finish event){
        if(event.getEntity() instanceof Player player) {
            ItemStack stack = event.getItem();
            if(stack.isEdible()) {
                if(PlayerStages.allStages.contains(EatTask.getStage(stack.getItem())))
                    PlayerStages.setBool(player, EatTask.getStage(stack.getItem()), true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.side.isServer()) {
            for (ItemStack stack : event.player.getInventory().items)
                if (PlayerStages.allStages.contains(ItemTask.getStage(stack.getItem())))
                    PlayerStages.setBool(event.player, ItemTask.getStage(stack.getItem()), true);
            for(Map.Entry<ResourceLocation, GuideBookPaper> entry : GuideManager.papers().entrySet()) {
                String stage = entry.getKey().toString()+"_paper";
                if (entry.getValue().completed(event.player) && !PlayerStages.getBool(event.player, stage)) {
                    PlayerStages.setBool(event.player, stage, true);
                    PlayerUtil.addItem(event.player, entry.getValue().getItemStack());
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerCraft(PlayerEvent.ItemCraftedEvent event){
        ItemStack item = event.getCrafting();
        if(event.getEntity().level().isClientSide) return;
        if(PlayerStages.allStages.contains(CraftTask.getStage(item.getItem())))
            PlayerStages.setBool(event.getEntity(), CraftTask.getStage(item.getItem()), true);
    }
    
    @SubscribeEvent
    public static void onPlayerKill(LivingDeathEvent event){
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        if(!source.is(DamageTypes.PLAYER_ATTACK)) return;
        if(source.getDirectEntity() instanceof Player player)
            if(PlayerStages.allStages.contains(KillTask.getStage(entity.getType())))
                PlayerStages.setBool(player, KillTask.getStage(entity.getType()), true);
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
