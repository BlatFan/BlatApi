package ru.blatfan.blatapi;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ru.blatfan.blatapi.client.event.BAClientEvents;
import ru.blatfan.blatapi.client.gui.screen.BAModsHandler;
import ru.blatfan.blatapi.client.gui.screen.BAPanorama;
import ru.blatfan.blatapi.client.gui.screen.BlatMod;
import ru.blatfan.blatapi.client.guide_book.BookModel;
import ru.blatfan.blatapi.client.render.LevelRenderHandler;
import ru.blatfan.blatapi.client.render.MultiblockPreviewRenderer;
import ru.blatfan.blatapi.client.shader.postprocess.PostProcessHandler;
import ru.blatfan.blatapi.client.splash.SplashHandler;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.compat.ShadersIntegration;
import ru.blatfan.blatapi.compat.jei.JeiIntegration;
import ru.blatfan.blatapi.utils.ClientTicks;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;

public class BlatApiClient {

    public static boolean optifinePresent = false;
    public static boolean piracyPresent = false;

    public static class ClientOnly {
        public static void clientInit(IEventBus eventBus) {
            IEventBus forgeBus = MinecraftForge.EVENT_BUS;
            forgeBus.register(new BAClientEvents());
            
            forgeBus.addListener(EventPriority.LOWEST, LevelRenderHandler::onLevelRender);
            forgeBus.addListener(EventPriority.LOWEST, PostProcessHandler::onLevelRender);
            
            eventBus.addListener(BARegistry.CreativeTabs::addCreativeTabContent);
        }
    }
    
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        BookModel.replace(event.getModels(), event.getModelBakery());
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
        
        if(ModList.get().isLoaded("jei")) MinecraftForge.EVENT_BUS.addListener((ScreenEvent.Opening e) -> JeiIntegration.updateHiddenItems());
        
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
            if (e.phase == TickEvent.Phase.END)
                MultiblockPreviewRenderer.onClientTick();
        });
        
        //Render multiblock preview
        MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent e) -> {
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) //After translucent causes block entities to error out on render in preview
                MultiblockPreviewRenderer.onRenderLevelLastEvent(e.getPoseStack());
        });
    }

    public static BlatMod MOD_INSTANCE;
    public static BAPanorama VANILLA_PANORAMA;
    public static BAPanorama BA_PANORAMA;

    public static void setupMenu() {
        MOD_INSTANCE = new BlatMod(BlatApi.MOD_ID, BlatApi.MOD_NAME, BlatApi.MOD_VERSION).setDev("BlatFan").setItem(new ItemStack(Items.AMETHYST_SHARD))
                .setNameColor(new Color(142, 95, 239))
                .setVersionColor(new Color(65, 36, 138))
                .setDescription(Text.create("mod_description.blatapi"))
            .addBFLinks("BlatApi");
        VANILLA_PANORAMA = new BAPanorama("minecraft:vanilla", Text.create("panorama.minecraft.vanilla").withColor(Color.GREEN))
            .setItem(new ItemStack(Items.GRASS_BLOCK));
        BA_PANORAMA = new BAPanorama("blatapi:cherry", Text.create("panorama.blatapi.cherry").withColor(Color.PINK))
            .setMod(MOD_INSTANCE)
            .setLogo(BlatApi.guiLoc("title/title"))
            .setTexture(BlatApi.guiLoc("title/cherry"))
            .setFlat(true)
            .setItem(new ItemStack(Items.CHERRY_LEAVES));
        
        registerMod(MOD_INSTANCE);
        registerPanorama(VANILLA_PANORAMA);
        registerPanorama(BA_PANORAMA);
    }

    public static void registerMod(BlatMod mod) {
        BAModsHandler.registerMod(mod);
    }

    public static void registerPanorama(BAPanorama panorama) {
        BAModsHandler.registerPanorama(panorama);
    }

    public static void setupSplashes() {
        SplashHandler.addSplash("Привет, Россия!");
        SplashHandler.addSplash("BlatFan!");
    }
}
