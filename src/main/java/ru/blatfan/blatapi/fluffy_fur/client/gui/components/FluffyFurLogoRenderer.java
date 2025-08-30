package ru.blatfan.blatapi.fluffy_fur.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.blatfan.blatapi.common.core.Vector3;

import static ru.blatfan.blatapi.utils.animation.AnimationCurve.*;

@OnlyIn(Dist.CLIENT)
public class FluffyFurLogoRenderer extends LogoRenderer {
    public ResourceLocation logo;

    public FluffyFurLogoRenderer(ResourceLocation logo, boolean keepLogoThroughFade) {
        super(keepLogoThroughFade);
        this.logo = logo;
    }
    private float ticks = 0;
    private boolean invert = false;
    
    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency) {
        ticks+=invert? -timeUnite() : timeUnite();
        float time = ticks/40f%20f;
        if(time>=1) invert=true;
        if(time<=0) invert=false;
        Vector3 pos = bezier(
            new Vector3(-6, 32, 0),
            new Vector3(6, 27, 0),
            new Vector3(6, 27, 0),
            new Vector3(-6, 32, 0),
            time
        );
        renderSmoothLogo(guiGraphics, screenWidth+pos.x, transparency, pos.y);
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency, int height) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : transparency);
        int i = screenWidth / 2 - 128;
        guiGraphics.blit(logo, i, height, 0.0F, 0.0F, 256, 64, 256, 64);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public void renderSmoothLogo(GuiGraphics guiGraphics, double screenWidth, float transparency, double height) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : transparency);
        double i = screenWidth / 2 - 128;
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(i, height, 0);
        guiGraphics.blit(logo, 0, 0, 0.0F, 0.0F, 256, 64, 256, 64);
        pose.popPose();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
