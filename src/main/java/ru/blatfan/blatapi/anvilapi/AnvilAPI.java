package ru.blatfan.blatapi.anvilapi;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import ru.blatfan.blatapi.utils.DisabledRecipes;
import ru.blatfan.blatapi.utils.RecipeHelper;

public final class AnvilAPI {
    public AnvilAPI(IEventBus bus) {
        bus.addListener(AnvilAPI::commonSetup);
    }
    
    public static void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new RecipeHelper());
        MinecraftForge.EVENT_BUS.register(new DisabledRecipes());
    }
}
