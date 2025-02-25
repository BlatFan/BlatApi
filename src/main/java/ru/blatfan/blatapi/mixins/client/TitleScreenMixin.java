package ru.blatfan.blatapi.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import ru.blatfan.blatapi.fluffy_fur.FluffyFurClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.blatfan.blatapi.utils.ColorHelper;

import java.awt.*;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Unique
    private int fluffy_fur$ticks = 0;

    @Inject(at = @At("RETURN"), method = "render")
    public void fluffy_fur$render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        TitleScreen self = (TitleScreen) ((Object) this);
        Font font = Minecraft.getInstance().font;
        PoseStack poseStack = guiGraphics.pose();
        if (FluffyFurClient.optifinePresent) {
            for (int i = 0; i < 26; i++) {
                float ticks = fluffy_fur$ticks + partialTick + i;
                float y = (float) (Math.sin(Math.toRadians(ticks)) * (self.height / 3f));
                if (i == 25) {
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                } else {
                    Color color = ColorHelper.rainbowColor(ticks * 0.05f);
                    RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.1f);
                }
                poseStack.pushPose();
                poseStack.translate(self.width / 2f, self.height / 2f + y, 0);
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (Math.sin(Math.toRadians(ticks * 1.3f)) * 30f)));
                String string = Component.translatable("gui.blatapi.menu.optifine.0").getString();
                guiGraphics.drawString(font, string, -font.width(string) / 2, -font.lineHeight - 1, 16777215);
                string = Component.translatable("gui.blatapi.menu.optifine.1").getString();
                guiGraphics.drawString(font, string, -font.width(string) / 2, 0, 16777215);
                string = Component.translatable("gui.blatapi.menu.optifine.2").getString();
                guiGraphics.drawString(font, string, -font.width(string) / 2, font.lineHeight + 1, 16777215);
                poseStack.popPose();
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        }
        if (FluffyFurClient.piracyPresent) {
            float ticks = fluffy_fur$ticks + partialTick * 0.1f;
            float size = 2f + (float) Math.sin(Math.toRadians(ticks));
            poseStack.pushPose();
            poseStack.translate(self.width / 2f, self.height / 4f, 0);
            poseStack.scale(size, size, size);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) (Math.sin(Math.toRadians(ticks * 1.3f)) * 30f)));
            String string = Component.translatable("gui.blatapi.menu.piracy").getString();
            guiGraphics.drawString(font, string, -font.width(string) / 2, -font.lineHeight - 1, 16777215);
            poseStack.popPose();
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    public void fluffy_fur$render(CallbackInfo ci) {
        fluffy_fur$ticks++;
    }
}
