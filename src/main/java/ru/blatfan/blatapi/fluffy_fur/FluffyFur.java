package ru.blatfan.blatapi.fluffy_fur;

import ru.blatfan.blatapi.fluffy_fur.common.capability.IPlayerSkin;
import ru.blatfan.blatapi.fluffy_fur.common.event.FluffyFurEvents;
import ru.blatfan.blatapi.fluffy_fur.common.itemskin.ItemSkin;
import ru.blatfan.blatapi.fluffy_fur.common.itemskin.ItemSkinHandler;
import ru.blatfan.blatapi.fluffy_fur.common.network.FluffyFurPacketHandler;
import ru.blatfan.blatapi.fluffy_fur.common.proxy.ClientProxy;
import ru.blatfan.blatapi.fluffy_fur.common.proxy.ISidedProxy;
import ru.blatfan.blatapi.fluffy_fur.common.proxy.ServerProxy;
import ru.blatfan.blatapi.fluffy_fur.config.FluffyFurClientConfig;
import ru.blatfan.blatapi.fluffy_fur.config.FluffyFurConfig;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurParticles;
import ru.blatfan.blatapi.fluffy_fur.registry.common.FluffyFurLootModifier;
import ru.blatfan.blatapi.fluffy_fur.registry.common.block.FluffyFurBlockEntities;
import ru.blatfan.blatapi.fluffy_fur.registry.common.block.FluffyFurBlocks;
import ru.blatfan.blatapi.fluffy_fur.registry.common.item.FluffyFurItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.blatfan.blatapi.BlatApi;

public class FluffyFur {
    public static final String MOD_ID = BlatApi.MODID;

    public static final ISidedProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogManager.getLogger();
    
    public FluffyFur(IEventBus eventBus) {
        FluffyFurItems.register(eventBus);
        FluffyFurBlocks.register(eventBus);
        FluffyFurBlockEntities.register(eventBus);
        FluffyFurParticles.register(eventBus);
        FluffyFurLootModifier.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FluffyFurClientConfig.SPEC, "blatfan/blatapi-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FluffyFurConfig.SPEC, "blatfan/blatapi-common.toml");

        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            FluffyFurClient.ClientOnly.clientInit();
            return new Object();
        });

        eventBus.addListener(this::setup);
        eventBus.addListener(FluffyFurClient::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new FluffyFurEvents());
    }

    private void setup(final FMLCommonSetupEvent event) {
        FluffyFurBlocks.setFireBlock();
        FluffyFurPacketHandler.init();
        for (ItemSkin skin : ItemSkinHandler.getSkins()) {
            skin.setupSkinEntries();
        }
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerCaps(RegisterCapabilitiesEvent event) {
            event.register(IPlayerSkin.class);
        }
    }
}