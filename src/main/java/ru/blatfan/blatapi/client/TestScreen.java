package ru.blatfan.blatapi.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import ru.blatfan.blatapi.client.gui.widget.EnergyBar;
import ru.blatfan.blatapi.client.gui.widget.FluidBar;
import ru.blatfan.blatapi.client.gui.widget.TexturedProgress;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.cap.FloatEnergyStorage;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.GuiUtil;
import software.bernie.example.registry.ItemRegistry;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

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
        float time = tick/60f;
        if(time>=1) invert=true;
        if(time<=0) invert=false;
        time = Math.max(0, Math.min(1, time));
        energyStorage.setEnergy((int) (energyStorage.getMaxEnergyStored()*time));
        fluidTank.setFluid(new FluidStack(Fluids.WATER, (int) (fluidTank.getCapacity()*time)));
        fluidTank2.setFluid(new FluidStack(Fluids.LAVA, (int) (fluidTank2.getCapacity()*time)));
        fluidTank3.setFluid(new FluidStack(ForgeMod.MILK.get(), (int) (fluidTank3.getCapacity()*time)));
        
        gui.pose().pushPose();
        gui.pose().translate(width/2f+48, height/2f+16, 0);
        GuiUtil.renderWavyFluid(gui.pose(), fluidTank.getFluid(), 1, 1, false, 1, 1, 1);
        gui.pose().popPose();
        
        progress.draw(gui, time);
        progress.renderHoveredToolTip(gui, mX, mY, (int) (time*60));
        progress2.draw(gui, time);
        progress2.renderHoveredToolTip(gui, mX, mY, (int) (time*60));
        
        GuiUtil.renderFlatPlayerHead(gui, 0, 0, 16, GuideClient.player);
        GuiUtil.render3DPlayerHeadMouse(gui, width/2, 128, 3, mX, mY, GuideClient.player);
        GuiUtil.render3DPlayerHead(gui, 256, 128, 3, 0, 0, GuideClient.player);
        
        float yRot = ClientTicks.ticks;
        List<Item> armors = Arrays.asList(
            Items.DIAMOND_HELMET,
            ItemRegistry.WOLF_ARMOR_HELMET.get(),
            ItemRegistry.GECKO_ARMOR_HELMET.get(),
            Items.DIAMOND_CHESTPLATE,
            ItemRegistry.WOLF_ARMOR_CHESTPLATE.get(),
            ItemRegistry.GECKO_ARMOR_CHESTPLATE.get(),
            Items.DIAMOND_LEGGINGS,
            ItemRegistry.WOLF_ARMOR_LEGGINGS.get(),
            ItemRegistry.GECKO_ARMOR_LEGGINGS.get(),
            Items.DIAMOND_BOOTS,
            ItemRegistry.WOLF_ARMOR_BOOTS.get(),
            ItemRegistry.GECKO_ARMOR_BOOTS.get()
        );
        gui.fill(0, 16, 24, 40, Color.RED.getRGB());
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_HELMET.getDefaultInstance(), 0, 16, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        gui.fill(0, 40, 24, 64, Color.BLUE.getRGB());
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_CHESTPLATE.getDefaultInstance(), 0, 40, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        gui.fill(0, 64, 24, 88, Color.YELLOW.getRGB());
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_LEGGINGS.getDefaultInstance(), 0, 64, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        gui.fill(0, 88, 24, 112, Color.GREEN.getRGB());
        GuiUtil.renderArmorInGui(gui, Items.DIAMOND_BOOTS.getDefaultInstance(), 0, 88, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        
        armorTicks+=partialTick;
        time = Math.max(0, Math.min(1, armorTicks%200/200));
        int index = (int) ((armors.size())*time);
        
        gui.fill(0, 128, 64, 128+64, Color.WHITE.getRGB());
        GuiUtil.renderArmorInGui(gui, armors.get(index).getDefaultInstance(), 0, 128, 16, 64, 64, 64, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        
        gui.fill(65, 128, 65+24, 128+24, Color.WHITE.getRGB());
        GuiUtil.renderArmorInGui(gui, armors.get(index).getDefaultInstance(), 65, 128, 16, 24, 24, 24, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
        
        gui.fill(65+25, 128, 65+26+16, 128+16, Color.WHITE.getRGB());
        GuiUtil.renderArmorInGui(gui, armors.get(index).getDefaultInstance(), 65+22, 128, 16, 16, 16, 16, 0, yRot, 0, GuiUtil.FULL_BRIGHT);
    }
    private float armorTicks = 0;
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}