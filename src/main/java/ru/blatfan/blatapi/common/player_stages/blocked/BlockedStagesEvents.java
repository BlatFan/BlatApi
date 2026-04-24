package ru.blatfan.blatapi.common.player_stages.blocked;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.BlockedStageHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;

public class BlockedStagesEvents {
    @Mod.EventBusSubscriber(modid = BlatApi.MOD_ID)
    public static class Forge {
        @SubscribeEvent
        public static void onDimensionTravel(EntityTravelToDimensionEvent event) {
            if (event.getEntity() instanceof Player player)
                if (!BlockedStageHelper.canAccessDimension(player, event.getDimension().location())) {
                    Text taskText = BlockedStageHelper.getLastDimensionTask(player, event.getDimension().location());
                    if(!taskText.isEmpty()) player.displayClientMessage(taskText, true);
                    event.setCanceled(true);
                }
        }
        
        @SubscribeEvent
        public static void onLivingDrops(LivingDropsEvent event) {
            if (event.getSource().getEntity() instanceof Player player)
                event.getDrops().removeIf(drop -> !BlockedStageHelper.canGetLoot(player, drop.getItem()));
        }
        
        @SubscribeEvent
        public static void playerUseItem(PlayerInteractEvent.LeftClickBlock event){
            if (event.getSide().isClient()) return;
            if (!BlockedStageHelper.canUse(event.getEntity(), event.getItemStack()))
                event.setCanceled(true);
        }
        @SubscribeEvent
        public static void playerUseItem(PlayerInteractEvent.RightClickBlock event){
            if (event.getSide().isClient()) return;
            if (!BlockedStageHelper.canUse(event.getEntity(), event.getItemStack()))
                event.setCanceled(true);
        }
        @SubscribeEvent
        public static void playerUseItem(PlayerInteractEvent.RightClickItem event){
            if (event.getSide().isClient()) return;
            if (!BlockedStageHelper.canUse(event.getEntity(), event.getItemStack()))
                event.setCanceled(true);
        }
        @SubscribeEvent
        public static void playerUseItem(AttackEntityEvent event){
            if (event.getEntity().level().isClientSide) return;
            if (!BlockedStageHelper.canUse(event.getEntity(), event.getEntity().getMainHandItem()))
                event.setCanceled(true);
        }
        @SubscribeEvent
        public static void playerUseItem(PlayerEvent.BreakSpeed event){
            if (event.getEntity().level().isClientSide) return;
            if (!BlockedStageHelper.canUse(event.getEntity(), event.getEntity().getMainHandItem()))
                event.setNewSpeed(0.001f);
        }
        @SubscribeEvent
        public static void playerUseItem(BlockEvent.EntityPlaceEvent event){
            if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof Player player)) return;
            if (BlockedStageHelper.cannotInteractBlock(player, event.getPlacedBlock().getBlock()))
                event.setCanceled(true);
        }
        
        @SubscribeEvent
        public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
            if (event.getEntity().level().isClientSide()) return;
            if (!(event.getEntity() instanceof ServerPlayer player)) return;
            
            ItemStack newItem = event.getTo();
            if (newItem.isEmpty()) return;
            
            EquipmentSlot slot = event.getSlot();
            if (slot.getType() != EquipmentSlot.Type.ARMOR && slot != EquipmentSlot.OFFHAND) return;
            
            if (!BlockedStageHelper.canUse(player, newItem))
                try {
                    player.setItemSlot(slot, ItemStack.EMPTY);
                    if (!player.getInventory().add(newItem.copy()))
                        player.drop(newItem.copy(), false);
                    player.containerMenu.broadcastChanges();
                } catch (Exception ignored) {}
        }
        
        @SubscribeEvent
        public static void onContainerOpen(PlayerContainerEvent.Open event) {
            Player player = event.getEntity();
            if (player.level().isClientSide()) return;
            
            if (event.getContainer().slots.isEmpty()) return;
            Container container = event.getContainer().slots.get(0).container;
            
            if (container == null) return;
            
            if (!container.getClass().getName().toLowerCase().contains("lootr")) return;
            
            boolean changed = false;
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (stack.isEmpty()) continue;
                
                if (!BlockedStageHelper.canGetLoot(player, stack)) {
                    container.setItem(i, ItemStack.EMPTY);
                    changed = true;
                }
            }
            
            if (changed) event.getContainer().broadcastChanges();
        }
    }
    
    @Mod.EventBusSubscriber(modid = BlatApi.MOD_ID, value = Dist.CLIENT)
    public static class ForgeClient {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void tooltip(ItemTooltipEvent event){
            List<Component> list = new ArrayList<>(event.getToolTip());
            if(!BlockedStageHelper.canUse(event.getEntity(), event.getItemStack())){
                var bs = BlockedStageHelper.getStageByItem(event.getItemStack());
                if(bs!=null && !bs.isVisible()) {
                    list.clear();
                    list.add(Text.create(event.getItemStack().getHoverName()).withStyle(ChatFormatting.OBFUSCATED));
                }
                List<Task> tasks = BlockedStageHelper.getItemStageTasks(event.getEntity(), event.getItemStack());
                list.add(Text.create());
                for (Task task : tasks) {
                    Text text = task.text(event.getEntity());
                    if(text.equals(Text.create())) continue;
                    list.add(text.asComponent());
                }
                if(event.getItemStack().getItem() instanceof BlockItem blockItem){
                    BlockedStage<BlockedStage.BlockStageValue> blockStage = BlockedStageHelper.getBlockStage(blockItem.getBlock());
                    if(blockStage!=null){
                        Block block = BuiltInRegistries.BLOCK.get(blockStage.getValue().getBlockPair().getValue());
                        if(block != Blocks.AIR){
                            list.clear();
                            list.add(Text.create(block.getName()));
                        }
                    }
                }
            }
            event.getToolTip().clear();
            event.getToolTip().addAll(list);
        }
        
        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null)
                BlockedStagesManager.updateRenderOverrides(Minecraft.getInstance().player);
        }
    }
}