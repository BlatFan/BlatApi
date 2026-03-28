package ru.blatfan.blatapi.client.registry;

import lombok.Getter;
import net.minecraft.client.renderer.ShaderInstance;

public class BAShaders {
    @Getter
    public static ShaderInstance ADDITIVE_TEXTURE, ADDITIVE, TRANSLUCENT_TEXTURE, TRANSLUCENT;
}