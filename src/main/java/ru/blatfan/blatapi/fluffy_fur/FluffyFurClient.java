package ru.blatfan.blatapi.fluffy_fur;

import ru.blatfan.blatapi.fluffy_fur.client.event.FluffyFurClientEvents;
import ru.blatfan.blatapi.fluffy_fur.client.splash.SplashHandler;
import ru.blatfan.blatapi.fluffy_fur.client.gui.screen.FluffyFurMod;
import ru.blatfan.blatapi.fluffy_fur.client.gui.screen.FluffyFurModsHandler;
import ru.blatfan.blatapi.fluffy_fur.client.gui.screen.FluffyFurPanorama;
import ru.blatfan.blatapi.fluffy_fur.integration.client.ShadersIntegration;
import ru.blatfan.blatapi.fluffy_fur.registry.common.item.FluffyFurCreativeTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.awt.*;

public class FluffyFurClient {

    public static boolean optifinePresent = false;
    public static boolean piracyPresent = false;

    public static class ClientOnly {
        public static void clientInit() {
            IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
            IEventBus forgeBus = MinecraftForge.EVENT_BUS;
            forgeBus.register(new FluffyFurClientEvents());

            eventBus.addListener(FluffyFurCreativeTabs::addCreativeTabContent);
        }
    }

    public static void clientSetup(final FMLClientSetupEvent event) {
        try {
            Class.forName("net.optifine.Config");
            optifinePresent = true;
        } catch (ClassNotFoundException e) {
            optifinePresent = false;
        }
        piracyPresent = ModList.get().isLoaded("tlskincape");

        setupMenu();
        setupSplashes();

        ShadersIntegration.init();
    }

    public static FluffyFurMod MOD_INSTANCE;
    public static FluffyFurPanorama VANILLA_PANORAMA;

    public static void setupMenu() {
        MOD_INSTANCE = new FluffyFurMod(FluffyFur.MOD_ID, "BlatApi", "0.1.3").setDev("BlatFan").setItem(new ItemStack(Items.AMETHYST_SHARD))
                .setNameColor(new Color(142, 95, 239)).setVersionColor(new Color(65, 36, 138))
                .setDescription(Component.translatable("mod_description.blatapi"))
                .addGithubLink("https://github.com/BlatFan/BlatApi")
                .addCurseForgeLink("https://www.curseforge.com/minecraft/mc-mods/blatapi")
                .addModrinthLink("https://modrinth.com/mod/blatapi")
                .addDiscordLink("https://discord.gg/eHJChH9mqH")
        ;
        VANILLA_PANORAMA = new FluffyFurPanorama("minecraft:vanilla", Component.translatable("panorama.minecraft.vanilla")).setItem(new ItemStack(Items.GRASS_BLOCK));
        
        registerMod(MOD_INSTANCE);
        registerPanorama(VANILLA_PANORAMA);
    }

    public static void registerMod(FluffyFurMod mod) {
        FluffyFurModsHandler.registerMod(mod);
    }

    public static void registerPanorama(FluffyFurPanorama panorama) {
        FluffyFurModsHandler.registerPanorama(panorama);
    }

    public static void setupSplashes() {
        SplashHandler.addSplash("Привет, Россия!");
    }
}
