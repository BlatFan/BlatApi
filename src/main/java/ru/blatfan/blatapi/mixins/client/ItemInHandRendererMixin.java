package ru.blatfan.blatapi.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.client.bow.BowHandler;
import ru.blatfan.blatapi.config.BlatApiClientConfig;
import ru.blatfan.blatapi.utils.ParticleItem;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Inject(method = "renderItem", at = @At("HEAD"))
    public void blatapi$renderItem(LivingEntity pEntity, ItemStack stack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack poseStack, MultiBufferSource pBuffer, int seed, CallbackInfo ci) {
        if(BlatApiClientConfig.ITEM_IN_HAND_PARTICLE.get() &&
            !stack.isEmpty() && stack.getItem() instanceof ParticleItem particleItem && particleItem.renderInHand())
            particleItem.guiParticle(poseStack, stack.getDescriptionId().length());
        
    }

    @Inject(at = @At("HEAD"), method = "evaluateWhichHandsToRender", cancellable = true)
    private static void blatapi$evaluateWhichHandsToRender(LocalPlayer pPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
        ItemStack itemStack = pPlayer.getUseItem();
        InteractionHand hand = pPlayer.getUsedItemHand();
        for (Item item : BowHandler.bows) {
            if (itemStack.is(item)) {
                cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
            }
        }
        for (Item item : BowHandler.crossbows) {
            if (itemStack.is(item)) {
                cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
            }
        }
    }
}
