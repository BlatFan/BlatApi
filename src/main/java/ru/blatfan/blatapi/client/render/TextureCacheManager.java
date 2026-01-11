package ru.blatfan.blatapi.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.blatapi.BlatApi;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BlatApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TextureCacheManager {
    private static int tickCounter = 0;
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            
            if (tickCounter >= 6000) {
                TextureCache.cleanUp();
                tickCounter = 0;
            }
        }
    }
}