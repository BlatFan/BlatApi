package ru.blatfan.blatapi.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage;
import ru.blatfan.blatapi.utils.BlockedStageHelper;

@Mixin(ItemRenderer.class)
public abstract class ItemRenderMixin {
    @Shadow
    public abstract BakedModel getModel(ItemStack pStack, @Nullable Level pLevel, @Nullable LivingEntity pEntity, int pSeed);
    
    @Inject(at = @At("RETURN"), method = "getModel", cancellable = true)
    private void getModel(ItemStack pStack, Level pLevel, LivingEntity pEntity, int pSeed, CallbackInfoReturnable<BakedModel> cir) {
        Player player = pEntity instanceof Player p ? p : Minecraft.getInstance().player;
        if(player==null || player.isCreative()) return;
        
        if(pStack.getItem() instanceof BlockItem blockItem){
            BlockedStage<BlockedStage.BlockStageValue> blockStage = BlockedStageHelper.getBlockStage(blockItem.getBlock());
            if(blockStage!=null){
                Block block = BuiltInRegistries.BLOCK.get(blockStage.getValue().getBlockPair().getValue());
                if(block!=Blocks.AIR) {
                    cir.setReturnValue(getModel(new ItemStack(block), pLevel, pEntity, pSeed));
                    return;
                }
            }
        }
        
        ItemStack q = new ItemStack(BARegistry.Items.QUESTION.get());
        var bs = BlockedStageHelper.getStageByItem(pStack);
        if(!BlockedStageHelper.canUse(player, pStack) && bs!=null && !bs.isVisible())
            cir.setReturnValue(getModel(q, pLevel, pEntity, pSeed));
    }
}