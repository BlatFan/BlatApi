package ru.blatfan.blatapi.fluffy_fur.common.capability;

import ru.blatfan.blatapi.fluffy_fur.client.model.playerskin.PlayerSkinData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public interface IPlayerSkin {
    Capability<IPlayerSkin> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    void setSkinData(PlayerSkinData data);
    PlayerSkinData getSkinData();

    void setSkin(String id);
    String getSkin();

    void setSkinEffect(String id);
    String getSkinEffect();

    void setSkinCape(String id);
    String getSkinCape();
}
