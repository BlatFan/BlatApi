package ru.blatfan.blatapi.creative_tab_filter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import ru.blatfan.blatapi.BlatApi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 */

public class Filter extends Button {
    public static final ResourceLocation ICONS = new ResourceLocation(BlatApi.MOD_ID, "textures/gui/filters.png");
    public static final ResourceLocation CREATIVE_TABS_LOCATION = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    public Supplier<ItemStack> icon;
    public final List<Item> items;
    public boolean enabled = true;

    protected Filter(Component tooltip, Supplier<ItemStack> icon, List<Item> items) {
        super(0, 0, 32, 26, tooltip, Button::onPress, DEFAULT_NARRATION);
        this.icon = icon;
        this.items = items;
    }

    @Override
    public void onPress() {
        enabled = !enabled;
    }

    @Override
    public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {
        super.render(p_282421_, p_93658_, p_93659_, p_93660_);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_93658_, int p_93659_, float p_93660_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CREATIVE_TABS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        int width = this.enabled ? 32 : 28;
        int textureX = 26;
        int textureY = this.enabled ? 32 : 0;
        this.drawRotatedTexture(guiGraphics.pose().last().pose(), this.getX(), this.getY(), textureX, textureY, width);

        guiGraphics.renderItem(icon.get(), this.getX() + 8, this.getY() + 4);
    }

    protected void drawRotatedTexture(Matrix4f pose, int x, int y, int textureX, int textureY, int width) {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, x, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        tesselator.end();
    }

    /**
     * Adding items to the filter.
     *
     * @param items items
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public Filter addItems(Item... items) {
        this.items.addAll(Arrays.asList(items));
        return this;
    }
}
