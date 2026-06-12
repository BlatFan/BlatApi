package ru.blatfan.blatapi.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.BlatApiClient;
import ru.blatfan.blatapi.client.gui.components.BALogoRenderer;
import ru.blatfan.blatapi.client.render.FlatCubeMap;
import ru.blatfan.blatapi.config.BlatApiClientConfig;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatapi.utils.collection.Text;
import ru.blatfan.blatapi.utils.gui_utils.GuiTextUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BAMenuScreen extends Screen {
    public Screen lastScreen;
    public CubeMap CUBE_MAP = new CubeMap(BlatApi.loc("textures/gui/title/background/panorama"));
    public PanoramaRenderer panorama = new PanoramaRenderer(CUBE_MAP);
    public BALogoRenderer logoRenderer;
    public long fadeInStart;
    
    private static final ResourceLocation LOGO = BlatApi.guiLoc("title/title");
    public static ResourceLocation BACKGROUND = BlatApi.guiLoc("menu_background");
    public static int ticks = 0;
    
    public static List<BlatMod> mods = new ArrayList<>();
    public static List<BAPanorama> panoramas = new ArrayList<>();
    
    public static int descriptionScroll = 0;
    public static int panoramasScroll = 0;
    public static int modsScroll = 0;
    
    public static int selectedPanorama = 0;
    public static int selectedMod = 0;
    
    public BAMenuScreen(Screen lastScreen) {
        super(Component.empty());
        this.lastScreen = lastScreen;
        this.logoRenderer = new BALogoRenderer(LOGO, false);
        if (lastScreen instanceof TitleScreen titleScreen)
            copyPanorama(titleScreen);
        this.fadeInStart = Util.getMillis();
        
        mods = BAModsHandler.getSortedMods();
        panoramas = BAModsHandler.getSortedPanoramas();
        
        BAPanorama currentPanorama = BAModsHandler.getPanorama(BlatApiClientConfig.PANORAMA.get());
        if (currentPanorama != null)
            if (panoramas.contains(currentPanorama))
                selectedPanorama = panoramas.indexOf(currentPanorama);
        descriptionScroll = 0;
    }
    
    public void copyPanorama(TitleScreen titleScreen) {
        CUBE_MAP = TitleScreen.CUBE_MAP;
        panorama = new PanoramaRenderer(CUBE_MAP);
        panorama.spin = titleScreen.panorama.spin;
        panorama.bob = titleScreen.panorama.bob;
    }
    
    public void setLocalPanorama(BAPanorama panorama) {
        float spin = this.panorama.spin;
        float bob = this.panorama.bob;
        ResourceLocation base = ResourceLocation.tryParse("textures/gui/title/background/panorama");
        if (panorama.getTexture() != null)
            base = panorama.getTexture();
        CUBE_MAP = panorama.flat ? new FlatCubeMap(base) : new CubeMap(base);
        this.panorama = new PanoramaRenderer(CUBE_MAP);
        this.panorama.spin = spin;
        this.panorama.bob = bob;
    }
    
    @Override
    public void init() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (button) -> {
            this.minecraft.setScreen(this.lastScreen);
            if (lastScreen instanceof TitleScreen titleScreen) {
                TitleScreen.CUBE_MAP = CUBE_MAP;
                titleScreen.panorama = panorama;
            }
        }).bounds(this.width / 2 - 80, this.height / 4 + 152, 160, 20).build());
    }
    
    @Override
    public void tick() {
        ticks++;
    }
    
    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        float f = (float) (Util.getMillis() - this.fadeInStart) / 250.0F;
        this.panorama.render(partialTicks, 1f);
        if (lastScreen instanceof TitleScreen titleScreen)
            titleScreen.logoRenderer.renderLogo(gui, this.width, 1f - f);
        this.logoRenderer.renderLogo(gui, this.width, f);
        
        drawDescription(gui, font, this.width / 2 - 80, this.height / 4 + 48, mouseX, mouseY, partialTicks);
        drawPanoramaList(gui, font, this.width / 2 - 204, this.height / 4 + 48, mouseX, mouseY, partialTicks);
        drawModList(gui, font, this.width / 2 + 84, this.height / 4 + 48, mouseX, mouseY, partialTicks);
        
        super.render(gui, mouseX, mouseY, partialTicks);
    }
    
    public void drawDescription(GuiGraphics gui, Font font, int x, int y, int mouseX, int mouseY, float partialTicks) {
        gui.blit(BACKGROUND, x, y, 0, 0, 160, 100, 256, 256);
        
        if (mods.isEmpty() || selectedMod >= mods.size()) return;
        BlatMod mod = mods.get(selectedMod);
        
        Component component = Component.literal(mod.getName()).withStyle(Style.EMPTY.withColor(ColorHelper.getColor(mod.getNameColor())))
            .append(" ").append(Component.literal("v" + mod.getVersion()).withStyle(Style.EMPTY.withColor(ColorHelper.getColor(mod.getVersionColor()))));
        
        drawBlackBackground(gui, x + 80, y - 12, font.width(component) + 8, mouseX, mouseY, partialTicks);
        gui.drawCenteredString(font, component, x + 80, y - 11, 16777215);
        
        List<Component> lines = getDescription(mod);
        int links = mod.getLinks().size();
        int l = lines.size() - links;
        
        gui.enableScissor(x + 5, y + 5, x + 145, y + 95);
        for (int i = 0; i < 9; i++) {
            int index = descriptionScroll + i;
            if (index < 0 || index >= lines.size()) break;
            
            Component line = lines.get(index);
            MutableComponent mutableLine = line.copy();
            
            if (index >= l &&
                mouseX >= x + 5 && mouseY >= y + 5 + (i * (font.lineHeight + 1))
                && mouseX <= x + 5 + font.width(line) && mouseY < y + 5 + (i * (font.lineHeight + 1) + font.lineHeight)) {
                mutableLine.withStyle(ChatFormatting.UNDERLINE);
            }
            gui.drawString(font, mutableLine, x + 5, y + 5 + (i * (font.lineHeight + 1)), 16777215);
        }
        gui.disableScissor();
        
        int s = lines.size() - 9;
        if (s > 0) {
            float slider = (float) descriptionScroll / s;
            drawSlider(gui, x + 147, y + 4, slider, mouseX, mouseY, partialTicks);
        }
    }
    
    public void drawPanoramaList(GuiGraphics gui, Font font, int x, int y, int mouseX, int mouseY, float partialTicks) {
        gui.blit(BACKGROUND, x, y, 0, 100, 120, 100, 256, 256);
        int s = panoramas.size() - 5;
        if (s > 0) {
            float slider = (float) panoramasScroll / s;
            drawSlider(gui, x + 107, y + 4, slider, mouseX, mouseY, partialTicks);
        }
        
        Component component = Text.create("gui.blatapi.menu.panoramas");
        drawBlackBackground(gui, x + 60, y - 12, font.width(component) + 8, mouseX, mouseY, partialTicks);
        gui.drawCenteredString(font, component, x + 60, y - 11, 16777215);
        
        gui.enableScissor(x, y + 2, x + 105, y + 98);
        for (int i = 0; i < 5; i++) {
            int index = panoramasScroll + i;
            if (index < 0 || index >= panoramas.size()) break;
            
            BAPanorama panorama = panoramas.get(index);
            MutableComponent name = Component.empty().append(panorama.getName());
            if (selectedPanorama == index) {
                name.withStyle(ChatFormatting.UNDERLINE);
            }
            gui.renderItem(panorama.getItemStack(), x + 2, y + 2 + (i * 20));
            gui.drawString(font, name, x + 20, y + 6 + (i * 20), 16777215);
        }
        gui.disableScissor();
        
        for (int i = 0; i < 5; i++) {
            int index = panoramasScroll + i;
            if (index < 0 || index >= panoramas.size()) break;
            BAPanorama panorama = panoramas.get(index);
            
            if (mouseX >= x && mouseY >= y && mouseX <= x + 120 && mouseY < y + 100) {
                if (mouseX >= x + 2 && mouseY >= y + 2 + (i * 20) && mouseX <= x + 102 && mouseY < y + 22 + (i * 20)) {
                    List<Component> list = new ArrayList<>();
                    list.add(panorama.getName());
                    if (panorama.getMod() != null) {
                        list.add(Component.empty());
                        list.add(Text.create("gui.blatapi.menu.mod").space().add(Component.literal(panorama.getMod().getName()).withStyle(ChatFormatting.GRAY)));
                    }
                    if (panorama == BlatApiClient.VANILLA_PANORAMA) {
                        list.add(Component.empty());
                        list.add(Component.literal("Minecraft").withStyle(ChatFormatting.GRAY));
                    }
                    gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
                }
            }
        }
    }
    
    private boolean blatfan(String devs){
        for(String s : devs.split(" "))
            if(s.equals("BlatFan"))
                return true;
        return false;
    }
    
    public void drawModList(GuiGraphics gui, Font font, int x, int y, int mouseX, int mouseY, float partialTicks) {
        gui.blit(BACKGROUND, x, y, 0, 100, 120, 100, 256, 256);
        
        int s = mods.size() - 5;
        if (s > 0) {
            float slider = (float) modsScroll / s;
            drawSlider(gui, x + 107, y + 4, slider, mouseX, mouseY, partialTicks);
        }
        
        Component component = Text.create("gui.blatapi.menu.mods");
        drawBlackBackground(gui, x + 60, y - 12, font.width(component) + 8, mouseX, mouseY, partialTicks);
        gui.drawCenteredString(font, component, x + 60, y - 11, 16777215);
        
        if (!BlatApi.mcreatorModsList.isEmpty()) {
            Component mcreatorComp = Text.create("gui.blatapi.menu.mcreator_mods").space().add(BlatApi.mcreatorModsList.size());
            drawBlackBackground(gui, x + 60, y - 24, font.width(mcreatorComp) + 8, mouseX, mouseY, partialTicks);
            gui.drawCenteredString(font, mcreatorComp, x + 60, y - 23, 16777215);
            
            if (mouseX >= x + 60 - (font.width(mcreatorComp) / 2f) - 4 && mouseY >= y - 24 && mouseX <= x + 60 + (font.width(mcreatorComp) / 2f) + 4 && mouseY < y - 14) {
                List<Component> list = new ArrayList<>();
                for (String string : BlatApi.mcreatorModsList)
                    list.add(Component.literal(string));
                gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
            }
        }
        
        gui.enableScissor(x, y + 2, x + 105, y + 98);
        for (int i = 0; i < 5; i++) {
            int index = modsScroll + i;
            if (index < 0 || index >= mods.size()) break;
            BlatMod mod = mods.get(index);
            gui.renderItem(mod.getItemStack(), x + 2, y + 2 + (i * 20));
            MutableComponent name = Component.empty().append(mod.getName());
            if (blatfan(mod.getDev()))
                name = name.withStyle(Style.EMPTY.withColor(ColorHelper.getColor(new Color(65, 36, 138))));
            if (selectedMod == index)
                name.withStyle(ChatFormatting.UNDERLINE);
            gui.drawString(font, name, x + 20, y + 6 + (i * 20), 16777215);
        }
        gui.disableScissor();
        
        for (int i = 0; i < 5; i++) {
            int index = modsScroll + i;
            if (index < 0 || index >= mods.size()) break;
            BlatMod mod = mods.get(index);
            
            if (mouseX >= x && mouseY >= y && mouseX <= x + 120 && mouseY < y + 100) {
                if (mouseX >= x + 2 && mouseY >= y + 2 + (i * 20) && mouseX <= x + 102 && mouseY < y + 22 + (i * 20)) {
                    List<Component> list = new ArrayList<>();
                    list.add(Component.literal(mod.getName()));
                    list.add(Component.empty());
                    list.add(Text.create("gui.blatapi.menu.id").space().add(Component.literal(mod.getId()).withStyle(ChatFormatting.GRAY)));
                    list.add(Text.create("gui.blatapi.menu.version").space().add(Component.literal(mod.getVersion()).withStyle(ChatFormatting.GRAY)));
                    if (mod.getEdition() > 0)
                        list.add(Text.create("gui.blatapi.menu.edition").space().add(Component.literal(String.valueOf(mod.getEdition())).withStyle(ChatFormatting.GRAY)));
                    list.add(Text.create("gui.blatapi.menu.author").space().add(Component.literal(mod.getDev()).withStyle(ChatFormatting.GRAY)));
                    
                    gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
                }
            }
        }
    }
    
    public static void drawSlider(GuiGraphics gui, int x, int y, float progress, int mouseX, int mouseY, float partialTicks) {
        int i = (int) (progress * 80);
        gui.blit(BACKGROUND, x, y, 160, 0, 10, 92, 256, 256);
        gui.blit(BACKGROUND, x + 1, y + 1 + i, 170, 0, 8, 10, 256, 256);
    }
    
    public static void drawBlackBackground(GuiGraphics gui, int x, int y, int size, float alpha, int mouseX, int mouseY, float partialTicks) {
        if (size < 4) size = 4;
        if (size % 2 != 0) size++;
        
        RenderSystem.enableBlend();
        gui.setColor(1f, 1f, 1f, alpha);
        gui.blit(BACKGROUND, x - (size / 2), y, 178, 0, 1, 10, 256, 256);
        gui.blit(BACKGROUND, x - (size / 2) + 1, y, size - 2, 10, 179, 0, 1, 10, 256, 256);
        gui.blit(BACKGROUND, x + (size / 2) - 1, y, 178, 0, 1, 10, 256, 256);
        gui.setColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }
    
    public static void drawBlackBackground(GuiGraphics gui, int x, int y, int size, int mouseX, int mouseY, float partialTicks) {
        drawBlackBackground(gui, x, y , size, 0.5f, mouseX, mouseY, partialTicks);
    }
    
    public static List<Component> getDescription(BlatMod mod) {
        String text = mod.getDescription().getString();
        int w = 140;
        List<Component> lines = new ArrayList<>();
        GuiTextUtil.splitText(text, w).forEach(s -> lines.add(Text.create(s)));
        if (!mod.getLinks().isEmpty()) {
            lines.add(Component.empty());
            lines.add(Text.create("gui.blatapi.menu.links").withStyle(ChatFormatting.GRAY));
            for (BlatMod.Link link : mod.getLinks())
                lines.add(link.getComponent());
        }
        return lines;
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (descriptionMouseClicked(this.width / 2 - 80, this.height / 4 + 48, mouseX, mouseY, button)) return true;
        if (panoramaListMouseClicked(this.width / 2 - 204, this.height / 4 + 48, mouseX, mouseY, button)) return true;
        if (modListMouseClicked(this.width / 2 + 84, this.height / 4 + 48, mouseX, mouseY, button)) return true;
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean descriptionMouseClicked(int x, int y, double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 160 && mouseY < y + 100) {
            BlatMod mod = mods.get(selectedMod);
            List<Component> lines = getDescription(mod);
            int links = mod.getLinks().size();
            int l = lines.size() - links;
            for (int i = 0; i < 9; i++) {
                int index = descriptionScroll + i;
                if (index < 0 || index >= lines.size()) break;
                
                Component line = lines.get(index);
                if (index >= l) {
                    if (mouseX >= x + 5 && mouseY >= y + 5 + (i * (this.font.lineHeight + 1)) && mouseX <= x + 5 + this.font.width(line) && mouseY < y + 5 + (i * (this.font.lineHeight + 1) + this.font.lineHeight)) {
                        linkTo(mod.getLinks().get(index - l).getLink());
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.get(), 1.0f, 0.25f));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean panoramaListMouseClicked(int x, int y, double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 120 && mouseY < y + 100) {
            for (int i = 0; i < 5; i++) {
                int index = panoramasScroll + i;
                if (index < 0 || index >= panoramas.size()) break;
                if (mouseX >= x + 2 && mouseY >= y + 2 + (i * 20) && mouseX <= x + 102 && mouseY < y + 22 + (i * 20)) {
                    selectedPanorama = index;
                    BAModsHandler.setPanorama(panoramas.get(selectedPanorama));
                    setLocalPanorama(panoramas.get(selectedPanorama));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.get(), 1.0f, 0.25f));
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean modListMouseClicked(int x, int y, double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 120 && mouseY < y + 100) {
            for (int i = 0; i < 5; i++) {
                int index = modsScroll + i;
                if (index < 0 || index >= mods.size()) break;
                if (mouseX >= x + 2 && mouseY >= y + 2 + (i * 20) && mouseX <= x + 102 && mouseY < y + 22 + (i * 20)) {
                    selectedMod = index;
                    descriptionScroll = 0;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.get(), 1.0f, 0.25f));
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (descriptionMouseScrolled(this.width / 2 - 80, this.height / 4 + 48, mouseX, mouseY, delta)) return true;
        if (panoramaListMouseScrolled(this.width / 2 - 204, this.height / 4 + 48, mouseX, mouseY, delta)) return true;
        if (modListMouseScrolled(this.width / 2 + 84, this.height / 4 + 48, mouseX, mouseY, delta)) return true;
        
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    
    public boolean descriptionMouseScrolled(int x, int y, double mouseX, double mouseY, double delta) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 160 && mouseY < y + 100) {
            List<Component> lines = getDescription(mods.get(selectedMod));
            int add = (int) Math.signum(delta);
            if (descriptionScroll - add < 0) return false;
            if (descriptionScroll - add > lines.size() - 9) return false;
            
            descriptionScroll -= add;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.get(), 2.0f, 0.1f));
            return true;
        }
        return false;
    }
    
    public boolean panoramaListMouseScrolled(int x, int y, double mouseX, double mouseY, double delta) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 120 && mouseY < y + 100) {
            int add = (int) Math.signum(delta);
            if (panoramasScroll - add < 0) return false;
            if (panoramasScroll - add > panoramas.size() - 5) return false;
            
            panoramasScroll -= add;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.get(), 2.0f, 0.1f));
            return true;
        }
        return false;
    }
    
    public boolean modListMouseScrolled(int x, int y, double mouseX, double mouseY, double delta) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + 120 && mouseY < y + 100) {
            int add = (int) Math.signum(delta);
            if (modsScroll - add < 0) return false;
            if (modsScroll - add > mods.size() - 5) return false;
            
            modsScroll -= add;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.get(), 2.0f, 0.1f));
            return true;
        }
        return false;
    }
    
    public void linkTo(String url) {
        this.minecraft.setScreen(new ConfirmLinkScreen((confirm) -> {
            if (confirm) Util.getPlatform().openUri(url);
            this.minecraft.setScreen(this);
        }, url, true));
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}