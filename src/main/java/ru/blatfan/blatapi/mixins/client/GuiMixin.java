package ru.blatfan.blatapi.mixins.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage;
import ru.blatfan.blatapi.utils.BlockedStageHelper;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    protected ItemStack lastToolHighlight;
    
    @Shadow
    @Final
    protected Minecraft minecraft;
    
    @ModifyArg(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I")
    )
    private FormattedText blatapi$modifySelectedItemTooltip(FormattedText pText) {
        return blatapi$getText(Component.literal(pText.getString()));
    }
    @ModifyArg(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I")
    )
    private Component blatapi$modifySelectedItemTooltip(Component pText) {
        return blatapi$getText(pText);
    }
    
    @Unique
    private Component blatapi$getText(Component originalComponent){
        if (lastToolHighlight.isEmpty())
            return originalComponent;
        
        var player = minecraft.player;
        if(player==null || player.isCreative()) return originalComponent;
        
        if(lastToolHighlight.getItem() instanceof BlockItem blockItem){
            BlockedStage<BlockedStage.BlockStageValue> blockStage = BlockedStageHelper.getBlockStage(blockItem.getBlock());
            if(blockStage!=null){
                Block block = BuiltInRegistries.BLOCK.get(blockStage.getValue().getBlockPair().getValue());
                if(block!= Blocks.AIR)
                    return block.getName().copy();
            }
        }
        
        if (BlockedStageHelper.canUse(player, lastToolHighlight)) return originalComponent;
        
        var stage = BlockedStageHelper.getStageByItem(lastToolHighlight);
        if (stage != null && !stage.isVisible()) return originalComponent.copy().withStyle(ChatFormatting.OBFUSCATED);
        
        return originalComponent;
    }
}