package ru.blatfan.blatapi.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.gui.widget.EnergyBar;
import ru.blatfan.blatapi.client.gui.widget.FluidBar;
import ru.blatfan.blatapi.client.gui.widget.TexturedProgress;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.cap.FloatEnergyStorage;
import ru.blatfan.blatapi.utils.GuiUtil;

public class TestScreen extends Screen {
    public TestScreen() {
        super(Component.empty());
    }
    
    private final FloatEnergyStorage energyStorage = new FloatEnergyStorage(1000);
    private final FluidTank fluidTank = new FluidTank(1000);
    private final FluidTank fluidTank2 = new FluidTank(5000);
    
    private TexturedProgress progress;
    private TexturedProgress progress2;
    
    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new EnergyBar(width/2-48, height/2-16, 16, energyStorage));
        addRenderableWidget(new EnergyBar(width/2-16, height/2-16, 32, energyStorage));
        addRenderableWidget(new EnergyBar(width/2-96, height/2-16, 48, energyStorage));
        
        addRenderableWidget(new FluidBar(width/2+48, height/2-16, 16, fluidTank, 0));
        addRenderableWidget(new FluidBar(width/2+16, height/2-16, 32, fluidTank2, 0));
        addRenderableWidget(new FluidBar(width/2+96, height/2-16, 48, fluidTank, 0));
        
        progress = new TexturedProgress(width/2-16, height/2-32);
        progress2 = new TexturedProgress(width/2+16, height/2-32, 16, 22, BlatApi.loc("textures/gui/progress_bar_vertical.png"));
        progress2.setTopDown(true);
    }
    
    private float tick = 0;
    private boolean invert = false;
    
    @Override
    public void render(GuiGraphics gui, int mX, int mY, float partialTick) {
        super.render(gui, mX, mY, partialTick);
        tick+=invert? -partialTick : partialTick;
        float time = tick/60f%20;
        if(time>=1) invert=true;
        if(time<=0) invert=false;
        time = Math.max(0, Math.min(1, time));
        energyStorage.setEnergy((int) (energyStorage.getMaxEnergyStored()*time));
        fluidTank.setFluid(new FluidStack(Fluids.WATER, (int) (fluidTank.getCapacity()*time)));
        fluidTank2.setFluid(new FluidStack(Fluids.LAVA, (int) (fluidTank2.getCapacity()*time)));
        
        progress.draw(gui, time);
        progress.renderHoveredToolTip(gui, mX, mY, (int) (time*60));
        progress2.draw(gui, time);
        progress2.renderHoveredToolTip(gui, mX, mY, (int) (time*60));
        
        GuiUtil.renderFlatPlayerHead(gui, 0, 0, 16, GuideClient.player);
        GuiUtil.render3DPlayerHeadMouse(gui, 128, 128, 3, mX, mY, GuideClient.player);
        GuiUtil.render3DPlayerHead(gui, 256, 128, 3, 0, 0, GuideClient.player);
        GuiUtil.render3DPlayerHead(gui, 256+128, 128, 3, 30, 30, GuideClient.player);
    }
}