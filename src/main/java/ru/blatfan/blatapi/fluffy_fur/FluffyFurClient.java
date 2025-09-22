package ru.blatfan.blatapi.fluffy_fur;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.render.MultiblockPreviewRenderer;
import ru.blatfan.blatapi.common.guide_book.GuideBookItem;
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
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.Text;

import java.awt.*;

import static ru.blatfan.blatapi.BlatApi.loc;

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
        
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
            if (e.phase == TickEvent.Phase.END) {
                ClientTicks.endClientTick(Minecraft.getInstance());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.RenderTickEvent e) -> {
            if (e.phase == TickEvent.Phase.START) {
                ClientTicks.renderTickStart(e.renderTickTime);
            } else {
                ClientTicks.renderTickEnd();
            }
        });
        
        //let multiblock preview renderer handle right clicks for anchoring
        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock e) -> {
            InteractionResult result = MultiblockPreviewRenderer.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
            if (result.consumesAction()) {
                e.setCanceled(true);
                e.setCancellationResult(result);
            }
        });
        
        //Tick multiblock preview
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
            if (e.phase == TickEvent.Phase.END) MultiblockPreviewRenderer.onClientTick();
        });
        
        //Render multiblock preview
        MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent e) -> {
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) { //After translucent causes block entities to error out on render in preview
                MultiblockPreviewRenderer.onRenderLevelLastEvent(e.getPoseStack());
            }
        });
    }

    public static FluffyFurMod MOD_INSTANCE;
    public static FluffyFurPanorama VANILLA_PANORAMA;

    public static void setupMenu() {
        MOD_INSTANCE = new FluffyFurMod(FluffyFur.MOD_ID, BlatApi.MOD_NAME, BlatApi.MOD_VERSION).setDev("BlatFan").setItem(new ItemStack(Items.AMETHYST_SHARD))
                .setNameColor(new Color(142, 95, 239))
                .setVersionColor(new Color(65, 36, 138))
                .setDescription(Component.translatable("mod_description.blatapi"))
                .addGithubLink("https://github.com/BlatFan/BlatApi")
                .addCurseForgeLink("https://www.curseforge.com/minecraft/mc-mods/blatapi")
                .addModrinthLink("https://modrinth.com/mod/blatapi")
                .addDiscordLink("https://discord.gg/eHJChH9mqH")
        ;
        VANILLA_PANORAMA = new FluffyFurPanorama("minecraft:vanilla", Text.create("panorama.minecraft.vanilla")).setItem(new ItemStack(Items.GRASS_BLOCK));
        
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
        SplashHandler.addSplash("BlatFan!");
    }
}
