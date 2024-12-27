package ru.blatfan.blatapi.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import ru.blatfan.blatapi.fluffy_fur.client.animation.ItemAnimation;
import ru.blatfan.blatapi.fluffy_fur.client.bow.BowHandler;
import ru.blatfan.blatapi.fluffy_fur.common.item.ICustomAnimationItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.fluffy_fur.config.FluffyFurClientConfig;
import ru.blatfan.blatapi.utils.ParticleItem;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), method = "renderArmWithItem")
    public void fluffy_fur$renderArmWithItem(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (stack.getItem() instanceof ICustomAnimationItem item) {
            ItemAnimation animation = item.getAnimation(stack);
            if (animation != null) {
                boolean use = true;
                if (animation.isOnlyItemUse()) use = ItemAnimation.isItemUse(player) && player.getUsedItemHand() == hand;
                if (use) item.getAnimation(stack).renderArmWithItem(player, partialTicks, pitch, hand, swingProgress, stack, equippedProgress, poseStack, buffer, combinedLight);
            }
        }
    }
    
    @Inject(method = "renderItem", at = @At("HEAD"))
    public void fluffy_fur$renderItem(LivingEntity pEntity, ItemStack stack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack poseStack, MultiBufferSource pBuffer, int seed, CallbackInfo ci) {
        if(FluffyFurClientConfig.ITEM_IN_HAND_PARTICLE.get() &&
            !stack.isEmpty() && stack.getItem() instanceof ParticleItem particleItem && particleItem.renderInHand())
            particleItem.guiParticle(poseStack, seed);
    }

    @Inject(at = @At("HEAD"), method = "evaluateWhichHandsToRender", cancellable = true)
    private static void fluffy_fur$evaluateWhichHandsToRender(LocalPlayer pPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
        ItemStack itemStack = pPlayer.getUseItem();
        InteractionHand hand = pPlayer.getUsedItemHand();
        for (Item item : BowHandler.getBows()) {
            if (itemStack.is(item)) {
                cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
            }
        }
    }
}
