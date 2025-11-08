package ru.blatfan.blatapi.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import ru.blatfan.blatapi.client.gui.widget.EnergyBar;
import ru.blatfan.blatapi.client.gui.widget.FluidBar;
import ru.blatfan.blatapi.client.gui.widget.TexturedProgress;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.cap.FloatEnergyStorage;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.GuiUtil;

import java.awt.*;

public class TestScreen extends Screen {
    public TestScreen() {
        super(Component.empty());
    }
    
    private final FloatEnergyStorage energyStorage = new FloatEnergyStorage(1000);
    private final FluidTank fluidTank = new FluidTank(1000);
    private final FluidTank fluidTank2 = new FluidTank(5000);
    private final FluidTank fluidTank3 = new FluidTank(5000);
    
    private TexturedProgress progress;
    private TexturedProgress progress2;
    
    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new EnergyBar(width/2-48, height/2-16, 16, energyStorage));
        
        addRenderableWidget(new FluidBar(width/2+48, height/2-16, 16, fluidTank, 0));
        addRenderableWidget(new FluidBar(width/2+16, height/2-16, 32, fluidTank2, 0));
        addRenderableWidget(new FluidBar(width/2+96, height/2-16, 32, fluidTank3, 0, Color.MAGENTA));
        
        progress = TexturedProgress.createHorizontal(width/2-16, height/2-64);
        progress2 = TexturedProgress.createVertical(width/2+16, height/2-64);
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
        fluidTank3.setFluid(new FluidStack(ForgeMod.MILK.get(), (int) (fluidTank3.getCapacity()*time)));
        
        progress.draw(gui, time);
        progress.renderHoveredToolTip(gui, mX, mY, (int) (time*60));
        progress2.draw(gui, time);
        progress2.renderHoveredToolTip(gui, mX, mY, (int) (time*60));
        
        GuiUtil.renderFlatPlayerHead(gui, 0, 0, 16, GuideClient.player);
        GuiUtil.render3DPlayerHeadMouse(gui, width/2, 128, 3, mX, mY, GuideClient.player);
        GuiUtil.render3DPlayerHead(gui, 256, 128, 3, 0, 0, GuideClient.player);
        
        float yRot = ClientTicks.ticks;
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_HELMET.getDefaultInstance(), 0, 16, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_CHESTPLATE.getDefaultInstance(), 0, 32, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_LEGGINGS.getDefaultInstance(), 0, 48, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_BOOTS.getDefaultInstance(), 0, 64, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}