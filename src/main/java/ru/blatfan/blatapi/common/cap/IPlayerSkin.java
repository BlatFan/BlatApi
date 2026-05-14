package ru.blatfan.blatapi.common.cap;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import ru.blatfan.blatapi.client.model.playerskin.PlayerSkinData;

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
