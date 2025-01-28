package ru.blatfan.blatapi;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.blatfan.blatapi.anvilapi.AnvilAPI;
import ru.blatfan.blatapi.biome_replacer.BiomeRaplacerModule;
import ru.blatfan.blatapi.events.BlatBlockEvents;
import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.init.BARecipeSerializers;
import ru.blatfan.blatapi.init.BARecipeTypes;
import ru.blatfan.blatapi.utils.packet.PacketRegistry;

@Mod(BlatApi.MODID)
public class BlatApi {
    public static final String MODID = "blatapi";
    public static final Logger LOGGER = LoggerFactory.getLogger("BlatApi");
    
    public BlatApi() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        BARecipeTypes.TYPES.register(bus);
        BARecipeSerializers.SERIALIZERS.register(bus);
        
        AnvilAPI.load(bus);
        
        new FluffyFur(bus);
        new BiomeRaplacerModule(bus);
        new BlatBlockEvents();
        bus.addListener(this::setup);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        PacketRegistry.setup();
        InterModComms.getMessages(MODID).forEach(x -> {
            LOGGER.info("registration from " + x.senderModId() + " | " + x.messageSupplier().get());
        });
    }
    
    public static ResourceLocation loc(String s) {
        return new ResourceLocation(MODID, s);
    }
}