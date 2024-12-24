package ru.blatfan.blatapi.fluffy_fur.registry.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.client.playerskin.*;

public class FluffyFurPlayerSkins {
    public static PlayerSkin BLATFAN_SKIN = new PlayerSkin(FluffyFur.MOD_ID + ":blatfan")
        .setSkinTexture(PlayerSkin.getSkinLocation(FluffyFur.MOD_ID, "blatfan"));
    public static PlayerSkinEffect EMERALD_EFFECT = new BlatFanSkinEffect(FluffyFur.MOD_ID + ":blatfan");
    public static PlayerSkinCape BLATFAN_CAPE = new PlayerSkinCape(FluffyFur.MOD_ID + ":blatfan")
        .setTexture(PlayerSkinCape.getCapeLocation(FluffyFur.MOD_ID, "blatfan"));

    @Mod.EventBusSubscriber(modid = FluffyFur.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents {
        @SubscribeEvent
        public static void registerPlayerSkins(FMLClientSetupEvent event) {
            PlayerSkinHandler.register(BLATFAN_SKIN);
            PlayerSkinHandler.register(EMERALD_EFFECT);
            PlayerSkinHandler.register(BLATFAN_CAPE);
        }
    }
}
