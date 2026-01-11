package ru.blatfan.blatapi.client.guide_book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.screen.ResearchScreen;
import ru.blatfan.blatapi.client.render.MultiblockPreviewRenderer;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;

public class GuideClient {
    public static Minecraft mc = Minecraft.getInstance();
    public static ClientLevel level = Minecraft.getInstance().level;
    public static Player player = mc.player;
    public static Font font = mc.font;
    public static GuideBookData guideBookData = null;
    public static int pageWidth = 120;
    public static int pageHeight = 140;
    public static TooltipFlag tooltipFlag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL;
    
    public static void open(GuideBookData book) {
        try {
            mc.setScreen(new ResearchScreen(book));
        } catch (Exception e) {
            BlatApi.LOGGER.info(e.getMessage());
        }
    }
    
    @SubscribeEvent
    public void playerClientTick(TickEvent.PlayerTickEvent event){
        GuideClient.player = event.player;
        GuideClient.mc = Minecraft.getInstance();
        GuideClient.level = mc.level;
        GuideClient.font = mc.font;
        GuideClient.tooltipFlag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL;
    }
    
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.BOSS_EVENT_PROGRESS.id(), "multiblock_hud", (gui, guiGraphics, partialTick, screenWidth, screenHeight) ->
            MultiblockPreviewRenderer.onRenderHUD(guiGraphics, partialTick));
    }
}