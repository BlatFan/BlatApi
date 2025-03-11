package ru.blatfan.blatapi.fluffy_fur.client.gui.components;

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
    
    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency) {
        Vector3 pos = bezier(
            new Vector3(-5, 30, 0),
            new Vector3(5, 25, 0),
            new Vector3(5, 25, 0),
            new Vector3(-5, 30, 0),
            timeUnite()*5
        );
        this.renderLogo(guiGraphics, (int) Math.round(screenWidth+pos.x), transparency, (int) Math.round(pos.y));
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency, int height) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : transparency);
        int i = screenWidth / 2 - 128;
        guiGraphics.blit(logo, i, height, 0.0F, 0.0F, 256, 64, 256, 64);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
