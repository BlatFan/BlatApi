    package ru.blatfan.blatapi;
    
    import net.minecraft.resources.ResourceLocation;
    import net.minecraftforge.api.distmarker.Dist;
    import net.minecraftforge.common.ForgeMod;
    import net.minecraftforge.common.MinecraftForge;
    import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
    import net.minecraftforge.common.crafting.CraftingHelper;
    import net.minecraftforge.event.AddReloadListenerEvent;
    import net.minecraftforge.eventbus.api.IEventBus;
    import net.minecraftforge.fml.DistExecutor;
    import net.minecraftforge.fml.IExtensionPoint;
    import net.minecraftforge.fml.ModList;
    import net.minecraftforge.fml.common.Mod;
    import net.minecraftforge.fml.config.ModConfig;
    import net.minecraftforge.fml.event.config.ModConfigEvent;
    import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
    import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
    import net.minecraftforge.gametest.ForgeGameTestHooks;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import ru.blatfan.blatapi.client.guide_book.GuideClient;
    import ru.blatfan.blatapi.common.BARegistry;
    import ru.blatfan.blatapi.common.GuideManager;
    import ru.blatfan.blatapi.common.capability.IPlayerSkin;
    import ru.blatfan.blatapi.common.itemskin.ItemSkin;
    import ru.blatfan.blatapi.common.itemskin.ItemSkinHandler;
    import ru.blatfan.blatapi.common.multiblock.MultiBlockData;
    import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;
    import ru.blatfan.blatapi.common.player_stages.PlayerStages;
    import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStageType;
    import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStagesManager;
    import ru.blatfan.blatapi.common.proxy.ClientProxy;
    import ru.blatfan.blatapi.common.proxy.ISidedProxy;
    import ru.blatfan.blatapi.common.proxy.ServerProxy;
    import ru.blatfan.blatapi.common.recipe.IngredientWithCountSerializer;
    import ru.blatfan.blatapi.common.reward.Reward;
    import ru.blatfan.blatapi.common.task.Task;
    import ru.blatfan.blatapi.compat.ftb_quests.FTBQuestsBA;
    import ru.blatfan.blatapi.compat.ftb_teams.IFTBTeam;
    import ru.blatfan.blatapi.config.BlatApiClientConfig;
    import ru.blatfan.blatapi.config.BlatApiConfig;
    import ru.blatfan.blatapi.utils.*;
    
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

// TODO PlayerStages for block render

@Mod(BlatApi.MOD_ID)
public class BlatApi {
    public static final String MOD_ID = "blatapi";
    public static final String MOD_NAME = "BlatApi";
    public static final String MOD_VERSION = "0.4.1";
    public static final Logger LOGGER = LoggerFactory.getLogger("BlatAPI");
    public static String CUSTOM_WINDOW_TITLE = "";
    public static final ISidedProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static List<String> mcreatorModsList = new ArrayList<>();
    public static final boolean DEV = ForgeGameTestHooks.isGametestEnabled();
    
    public BlatApi(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ForgeMod.enableMilkFluid();
        context.registerConfig(ModConfig.Type.CLIENT, BlatApiClientConfig.SPEC, "blatfan/blatapi/client.toml");
        context.registerConfig(ModConfig.Type.COMMON, BlatApiConfig.SPEC, "blatfan/blatapi/common.toml");
        
        BARegistry.register(bus);
        
        bus.addListener(this::setup);
        bus.addListener(BlatApiClient::clientSetup);
        bus.addListener(this::configLoad);
        bus.addListener(BlatApiClient::onModifyBakingResult);
        bus.addListener((RegisterCapabilitiesEvent event) -> {
            event.register(PlayerStages.class);
            event.register(IPlayerSkin.class);
        });
        bus.addListener(GuideClient::onRegisterGuiOverlays);
        
        MinecraftForge.EVENT_BUS.addListener(this::reloadable);
        
        if(DEV) DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()-> () -> TestHooks.setup(bus));
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            BlatApiClient.ClientOnly.clientInit(bus);
            MinecraftForge.EVENT_BUS.register(GuideClient.class);
            return new Object();
        });
        context.registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
    
        if(ModList.get().isLoaded("ftbquests")) FTBQuestsBA.init();
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new DisabledRecipes());
        MinecraftForge.EVENT_BUS.register(new RecipeHelper());
        MinecraftForge.EVENT_BUS.addListener(InitialSpawnItems.INSTANCE::onPlayerLoggedIn);
        Task.init();
        Reward.init();
        BlockedStageType.init();
        PlayerStages.Value.init();
        BlatApiPacketHandler.init();
        RegistryUtils.setFireBlock();
        GuideManager.init();
        MultiBlockData.init();
        BARegistry.CreativeTabs.addSubTabs();
        
        if(ModList.get().isLoaded("ftbteams")) TeamHelper.TEAMS.put("ftbteams", new IFTBTeam());
        
        
        event.enqueueWork(() ->
            CraftingHelper.register(IngredientWithCountSerializer.ID, IngredientWithCountSerializer.INSTANCE)
        );
        
        for (ItemSkin skin : ItemSkinHandler.getSkins())
            skin.setupSkinEntries();
        for (Package pack: Arrays.stream(Package.getPackages()).toList()) {
            String string = pack.getName();
            int dots = 0;
            for (char c : string.toCharArray())
                if (c == '.') dots++;
            if (dots == 2) {
                if (pack.getName().startsWith("net.mcreator")) {
                    int i = string.indexOf(".");
                    string = string.substring(i + 1, string.length() - 1);
                    i = string.indexOf(".");
                    string = string.substring(i + 1);
                    mcreatorModsList.add(string);
                } else if (pack.getName().contains("procedures")) {
                    int i = string.indexOf(".");
                    string = string.substring(i + 1, string.length() - 1);
                    i = string.indexOf(".");
                    string = string.substring(0, i);
                    mcreatorModsList.add(string);
                }
            }
        }
    }
    
    private void configLoad(ModConfigEvent event){
        CUSTOM_WINDOW_TITLE=BlatApiClientConfig.CUSTOM_WINDOW_TITLE.get();
    }
    
    private void reloadable(AddReloadListenerEvent event){
        event.addListener(new GuideManager());
        event.addListener(new BlockedStagesManager());
    }
    
    public static ResourceLocation loc(String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }
    public static ResourceLocation locParse(String s) {
        ResourceLocation parse = ResourceLocation.parse(s);
        if(parse.getNamespace().equals("minecraft"))
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
        return parse;
    }
    public static ResourceLocation guiLoc(String s) {
        return loc("textures/gui/"+s+".png");
    }
}