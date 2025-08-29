package ru.blatfan.blatapi;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.biome_replacer.BiomeRaplacerModule;
import ru.blatfan.blatapi.common.player_stages.PlayerStagesEvents;
import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.utils.DisabledRecipes;
import ru.blatfan.blatapi.utils.RecipeHelper;

@Mod(BlatApi.MOD_ID)
public class BlatApi {
    public static final String MOD_ID = "blatapi";
    public static final String MOD_NAME = "BlatApi";
    public static final String MOD_VERSION = "0.2.7";
    public static final Logger LOGGER = LoggerFactory.getLogger("BlatAPI");
    
    public BlatApi() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        BARegistry.register(bus);
        
        new FluffyFur(bus);
        new BiomeRaplacerModule(bus);
        
        MinecraftForge.EVENT_BUS.register(PlayerStagesEvents.class);
        bus.addListener(this::setup);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        InterModComms.getMessages(MOD_ID).forEach(x ->
            LOGGER.info("registration from {} | {}", x.senderModId(), x.messageSupplier().get()));
        MinecraftForge.EVENT_BUS.register(RecipeHelper.class);
        MinecraftForge.EVENT_BUS.register(DisabledRecipes.class);
    }
    
    public static ResourceLocation loc(String s) {
        return new ResourceLocation(MOD_ID, s);
    }
}