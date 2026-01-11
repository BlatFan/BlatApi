package ru.blatfan.blatapi.client.guide_book.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.events.EntryClickedEvent;
import ru.blatfan.blatapi.common.guide_book.GuideBookCategory;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.guide_book.GuideBookEntry;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.common.task.BookEntryTask;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.MathUtils;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;

public class ResearchScreen extends Screen {
    private float scale = 1, targetScale = 1;
    private double offset = 0,  offsetX = 0, offsetY = 0,
        targetOffset = 0, targetOffsetX = 0, targetOffsetY = 0;
    private GuideBookCategory category = null;
    private final int bgSize = 1024;
    
    private GuideBookGui guideBookGui = new GuideBookGui();
    private final GuideBookData data;
    public ResearchScreen(GuideBookData data) {
        super(Component.empty());
        GuideClient.guideBookData=data;
        this.data = data;
    }
    
    private float screenTick = 0;
    @Override
    public void render(GuiGraphics gui, int mX, int mY, float partialTick) {
        screenTick+=partialTick;
        scale = MathUtils.lerp(scale, targetScale, 0.1f);
        offset = MathUtils.lerp(offset, targetOffset, 0.1f);
        offsetX = MathUtils.lerp(offsetX, targetOffsetX, 0.1f);
        offsetY = MathUtils.lerp(offsetY, targetOffsetY, 0.1f);
        PoseStack pose = gui.pose();
        
        gui.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        gui.blitNineSlicedSized(data.getTexture(), 16, 16, width-48, height-32, 16, 48, 48, 0, 0, 256, 256);
        
        if(category==null && !categories().isEmpty()) category = categories().get(0);
        else if(category==null) return;
        
        renderBackground(gui);
        renderCategories(gui);
        
        gui.pose().pushPose();
        gui.pose().translate(width / 2f + offsetX, height / 2f + offsetY, 50);
        gui.pose().scale(scale, scale, 1);
        
        RenderSystem.enableBlend();
        gui.enableScissor(24, 24, width-40, height-24);
        
        renderConnections(gui);
        renderEntries(gui);
        
        gui.disableScissor();
        RenderSystem.disableBlend();
        
        pose.popPose();
        renderTooltips(gui, mX, mY);
        
        if(guideBookGui.getEntry()!=null){
            pose.pushPose();
            pose.translate(0, 0, 250);
            guideBookGui.render(gui, mX, mY, partialTick);
            pose.popPose();
            if(guideBookGui.isClose()) guideBookGui.setEntry(null);
        }
        
        pose.pushPose();
        pose.translate(mX+4, mY, 500);
        pose.mulPose(Axis.ZP.rotationDegrees(45));
        gui.blit(data.getTexture(), 0, 0, 160, 64, 58, 9);
        pose.popPose();
    }
    
    public void renderBackground(GuiGraphics gui) {
        RenderSystem.enableBlend();
        gui.enableScissor(24, 24, width-40, height-24);
        for(GuideBookCategory.Background bg : category.backgrounds()) renderParallaxBG(bg, gui);
        gui.disableScissor();
        RenderSystem.disableBlend();
    }
    
    private void renderParallaxBG(GuideBookCategory.Background background, GuiGraphics gui){
        PoseStack poseStack = gui.pose();
        poseStack.pushPose();
        poseStack.translate(width / 2f + offsetX*background.speed(), height / 2f + offsetY*background.speed(), background.sort());
        poseStack.scale(scale*(1-background.speed()), scale*(1-background.speed()), 1);
        gui.blit(background.texture(), (int)(-bgSize*1.5f), (int)(-bgSize*1.5f), 0, 0, 0,
            bgSize*3, bgSize*3, bgSize*3, bgSize*3);
        poseStack.popPose();
    }
    
    private void renderCategories(GuiGraphics gui) {
        gui.enableScissor(width-36, 16, width, height-24);
        for(int i = 0; i< categories().size(); i++){
            GuideBookCategory c = categories().get(i);
            int x = width-36;
            int y = (int) (16+offset)+i*30;
            gui.blitNineSlicedSized(data.getTexture(), x, y, 24, 24, 16, 48, 48, 208, 0, 256, 256);
            gui.renderFakeItem(c.icon(), x+4, y+4);
        }
        gui.disableScissor();
    }
    
    private List<GuideBookCategory> categories(){
        List<GuideBookCategory> l = new ArrayList<>();
        for(GuideBookCategory c : GuideManager.categories().values()) {
            ResourceLocation bookC = c.book();
            ResourceLocation book = GuideManager.getId(data);
            if(!bookC.equals(book)) continue;
            if(c.visible(GuideClient.player)) l.add(c);
        }
        return l;
    }
    
    private void renderEntries(GuiGraphics gui) {
        ResourceLocation texture = data.getTexture();
        for (GuideBookEntry node : entries()) {
            ResourceLocation id = GuideManager.getId(node);
            float a = 1;
            if(!node.completed(GuideClient.player))
                a = (float) (Math.sin(screenTick % 20 /20 * Math.PI)/4f+0.75f);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1, 1, 1, a);
            
            if(node.advance())
                gui.blit(texture, node.x() - 5, node.y() - 5, 74, PlayerStages.getBool(GuideClient.player, id.toString()) ? 26 : 0, 26, 26, 256, 256);
            else
                gui.blit(texture, node.x() - 5, node.y() - 5, 48, PlayerStages.getBool(GuideClient.player, id.toString()) ? 26 : 0, 26, 26, 256, 256);
            gui.renderFakeItem(node.icon(), node.x(), node.y());
            
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        }
    }
    
    private List<GuideBookEntry> entries(){
        List<GuideBookEntry> l = new ArrayList<>();
        for(GuideBookEntry e : GuideManager.entries().values()){
            ResourceLocation catE = e.category();
            ResourceLocation cat = GuideManager.getId(category);
            if(e.visible(GuideClient.player) && catE.equals(cat)) l.add(e);
        }
        return l;
    }
    private void renderConnections(GuiGraphics gui) {
        for (GuideBookEntry node : entries())
            for(Task con : node.view())
                if(con instanceof BookEntryTask connection && connection.getEntry()!=null)
                    renderConnection(gui, node, GuideManager.entries().get((connection.getEntry())));
    }
    
    private void renderConnection(GuiGraphics graphics, GuideBookEntry from, GuideBookEntry to) {
        ResourceLocation texture = data.getTexture();
        graphics.pose().pushPose();
        double connectionX = from.x()+8;
        double connectionY = from.y()+8;
        graphics.pose().translate(connectionX, connectionY, 0);
        float rotation = getAngleBetween(from, to);
        graphics.pose().mulPose(Axis.ZP.rotation(rotation));
        int length = (int) getDistanceBetween(from, to);
        boolean highlighted = to.visible(GuideClient.player);
        graphics.blitNineSlicedSized(texture, 0, -4, length, 8, 8, 99, 8, 100, highlighted ? 0 : 8, 256, 256);
        graphics.pose().popPose();
    }
    private float getAngleBetween(GuideBookEntry button1, GuideBookEntry button2) {
        float x1 = button1.x();
        float y1 = button1.y();
        float x2 = button2.x();
        float y2 = button2.y();
        return MathUtils.atan2(y2 - y1, x2 - x1);
    }
    private float getDistanceBetween(GuideBookEntry button1, GuideBookEntry button2) {
        float x1 = button1.x();
        float y1 = button1.y();
        float x2 = button2.x();
        float y2 = button2.y();
        return MathUtils.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    public void renderTooltips(GuiGraphics gui, int mouseX, int mouseY) {
        if(guideBookGui!=null) return;
        gui.pose().pushPose();
        gui.pose().translate(0, 0, 1000);
        List<Component> list = new ArrayList<>();
        for (GuideBookEntry node : entries()) {
            double x = ((node.x()-5)*targetScale)+((double) width / 2+targetOffsetX);
            double y = ((node.y()-5)*targetScale)+((double) height /2+targetOffsetY);
            double size = 26*targetScale;
            if(mouseX>= x && mouseX<= x+size &&
                mouseY>= y && mouseY<= y+size){
                list.add(Text.create(node.title()).withStyle(ChatFormatting.WHITE));
                list.add(Text.create(node.description()).withStyle(ChatFormatting.GRAY));
                if(GuideClient.tooltipFlag.isAdvanced())
                    list.add(Text.create(GuideManager.getId(node)).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        for(GuideBookCategory c : categories()){
            double x = width-36;
            double y = (int) (16+offset)+(categories().indexOf(c))*30;
            double size = 24;
            if(mouseX>= x && mouseX<= x+size &&
                mouseY>= y && mouseY<= y+size) {
                list.add(Text.create(c.title()).withColor(c.titleColor()));
                if(GuideClient.tooltipFlag.isAdvanced())
                    list.add(Text.create(GuideManager.getId(c)).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        if(!list.isEmpty())
            gui.renderComponentTooltip(getMinecraft().font, list, mouseX, mouseY);
        gui.pose().popPose();
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(guideBookGui!=null) return guideBookGui.mouseClicked(mouseX, mouseY, button);
        if (mouseX>16 && mouseX<width-36 &&
            mouseY>16 && mouseY<height-24 && button == 0) {
            for (GuideBookEntry node : entries()) {
                double x = ((node.x()-5)*targetScale)+((double) width / 2+targetOffsetX);
                double y = ((node.y()-5)*targetScale)+((double) height /2+targetOffsetY);
                double size = 26*targetScale;
                if(mouseX>= x && mouseX<= x+size &&
                    mouseY>= y && mouseY<= y+size) {
                    EntryClickedEvent event = new EntryClickedEvent(data, category, node);
                    if(!MinecraftForge.EVENT_BUS.post(event))
                        guideBookGui.setEntry(node);
                }
            }
        } else if(mouseX>width-36 && mouseX<width &&
            mouseY>16 && mouseY<height-24 && button == 0) {
            for(GuideBookCategory c : categories()){
                double x = width-40;
                double y = (int) (16+offset)+(categories().indexOf(c))*30;
                double size = 24;
                if(mouseX>= x && mouseX<= x+size &&
                    mouseY>= y && mouseY<= y+size) {
                    category = c;
                    targetScale = 1;
                    targetOffsetX = 0;
                    targetOffsetY = 0;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(guideBookGui!=null) return guideBookGui.mouseScrolled(mouseX, mouseY, delta);
        if(mouseX>16 && mouseX<width-36 &&
            mouseY>16 && mouseY<height-24){
            targetScale *= delta > 0 ? 1.1f : 0.9f;
            float minScale = 0.4f;
            float maxScale = 1.6f;
            targetScale = Math.max(minScale, Math.min(maxScale, targetScale));
            return true;
        } else if(mouseX>width-36 && mouseX<width &&
            mouseY>16 && mouseY<height-24) {
            targetOffset = Math.max(0, Math.min(Math.min(0,
                ((categories().size() - 1) * 30)-height-24), targetOffset - delta * 2));
            return true;
        }
        return false;
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(guideBookGui!=null) return guideBookGui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        if (mouseX>16 && mouseX<width-36 &&
            mouseY>16 && mouseY<height-24 && button == 0) {
            targetOffsetX = Math.max(-bgSize/2f, Math.min(bgSize/2f, targetOffsetX+deltaX));
            targetOffsetY = Math.max(-bgSize/2f, Math.min(bgSize/2f, targetOffsetY+deltaY));
            return true;
        } else if(mouseX>width-36 && mouseX<width &&
            mouseY>16 && mouseY<height-24 && button==0) {
            targetOffset = Math.max(0, Math.min(Math.min(0,
                ((categories().size() - 1) * 30)-height-24), targetOffset + deltaY));
            return true;
        }
        return false;
    }
    
    @Override
    public void onClose() {
        if(guideBookGui!=null) guideBookGui=null;
        else super.onClose();
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}