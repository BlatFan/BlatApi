package ru.blatfan.blatapi.mixins.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.client.TOTHandler;
import ru.blatfan.blatapi.client.registry.BARenderTypes;
import ru.blatfan.blatapi.client.render.RenderBuilder;
import ru.blatfan.blatapi.common.item.IGuiParticleItem;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Inject(at = @At(value = "TAIL"), method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    private void blatapi$renderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        GuiGraphics self = (GuiGraphics) ((Object) this);
        if (BlatApiClientConfig.ITEM_GUI_PARTICLE.get())
            if (stack.getItem() instanceof IGuiParticleItem guiParticleItem)
                guiParticleItem.renderParticle(self.pose(), entity, level, stack, x, y, seed, guiOffset);
        
        
        for (RenderBuilder builder : BARenderTypes.customItemRenderBuilderGui)
            builder.endBatch();
        
        BARenderTypes.customItemRenderBuilderGui.clear();
        TOTHandler.render(self, stack, x, y);
    }
}
