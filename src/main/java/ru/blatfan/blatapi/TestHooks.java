package ru.blatfan.blatapi;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import ru.blatfan.blatapi.client.TestScreen;

public class TestHooks {
    public static final KeyMapping TEST = new KeyMapping("Test",
        KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_H), BlatApi.MOD_NAME);
    
    public static void setup(){
        MinecraftForge.EVENT_BUS.register(CustomClientEventHandler.class);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(TestHooks::registerKeybindEvent);
    }
    
    public static void registerKeybindEvent(RegisterKeyMappingsEvent event){
        event.register(TEST);
    }
    
    public static class CustomClientEventHandler {
        @SubscribeEvent
        public static void onKeyPress(InputEvent event) {
            if(TestHooks.TEST.consumeClick()){
                Minecraft mc = Minecraft.getInstance();
                mc.setScreen(new TestScreen());
            }
        }
    }
}