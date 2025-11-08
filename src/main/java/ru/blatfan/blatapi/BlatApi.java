package ru.blatfan.blatapi;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.biome_replacer.BiomeRaplacerModule;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.multiblock.MultiBlockData;
import ru.blatfan.blatapi.common.player_stages.PlayerStagesEvents;
import ru.blatfan.blatapi.common.recipe.IngredientWithCountSerializer;
import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.config.FluffyFurClientConfig;
import ru.blatfan.blatapi.utils.DisabledRecipes;
import ru.blatfan.blatapi.utils.InitialSpawnItems;
import ru.blatfan.blatapi.utils.RecipeHelper;

@Mod(BlatApi.MOD_ID)
public class BlatApi {
    public static final String MOD_ID = "blatapi";
    public static final String MOD_NAME = "BlatApi";
    public static final String MOD_VERSION = "0.3.1";
    public static final Logger LOGGER = LoggerFactory.getLogger("BlatAPI");
    public static String CUSTOM_WINDOW_TITLE = "";
    
    public BlatApi() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeMod.enableMilkFluid();
        
        BARegistry.register(bus);
        MinecraftForge.EVENT_BUS.addListener(this::reloadable);
        
        new FluffyFur(bus);
        new BiomeRaplacerModule(bus);
        
        MinecraftForge.EVENT_BUS.register(PlayerStagesEvents.class);
        bus.addListener(this::setup);
        bus.addListener(this::configLoad);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()-> TestHooks::setup);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new GuideClient());
            return new Object();
        });
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
    
        bus.addListener(GuideClient::onRegisterGuiOverlays);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new DisabledRecipes());
        MinecraftForge.EVENT_BUS.register(new RecipeHelper());
        MinecraftForge.EVENT_BUS.addListener(InitialSpawnItems.INSTANCE::onPlayerLoggedIn);
        
        GuideManager.init();
        MultiBlockData.init();
        BARegistry.CreativeTabs.addSubTabs();
        
        event.enqueueWork(() ->
            CraftingHelper.register(IngredientWithCountSerializer.ID, IngredientWithCountSerializer.INSTANCE)
        );
    }
    
    private void configLoad(ModConfigEvent event){
        CUSTOM_WINDOW_TITLE= FluffyFurClientConfig.CUSTOM_WINDOW_TITLE.get();
    }
    
    private void reloadable(AddReloadListenerEvent event){
        event.addListener(new GuideManager());
    }
    
    public static ResourceLocation loc(String s) {
        return new ResourceLocation(MOD_ID, s);
    }
}