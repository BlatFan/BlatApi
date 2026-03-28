package ru.blatfan.blatapi.client.registry;

import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.playerskin.*;

public class BAPlayerSkins {
    public static PlayerSkin BLATFAN_SKIN = new PlayerSkin(BlatApi.MOD_ID + ":blatfan")
        .setSkinTexture(PlayerSkin.getSkinLocation(BlatApi.MOD_ID, "blatfan"));
    public static PlayerSkinEffect EMERALD_EFFECT = new BlatFanSkinEffect(BlatApi.MOD_ID + ":blatfan");
    public static PlayerSkinEffect BROKENBLOCK_EFFECT = new BrokenBlockSkinEffect(BlatApi.MOD_ID + ":brokenblock");
    public static PlayerSkinCape BLATFAN_CAPE = new PlayerSkinCape(BlatApi.MOD_ID + ":blatfan")
        .setTexture(PlayerSkinCape.getCapeLocation(BlatApi.MOD_ID, "blatfan"));
    public static PlayerSkinCape BROKENBLOCK_CAPE = new PlayerSkinCape(BlatApi.MOD_ID + ":brokenblock")
        .setTexture(PlayerSkinCape.getCapeLocation(BlatApi.MOD_ID, "brokenblock"));
}
