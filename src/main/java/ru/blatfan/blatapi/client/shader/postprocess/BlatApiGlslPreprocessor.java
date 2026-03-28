package ru.blatfan.blatapi.client.shader.postprocess;

import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.BlatApi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BlatApiGlslPreprocessor extends GlslPreprocessor {

    public static final BlatApiGlslPreprocessor PREPROCESSOR = new BlatApiGlslPreprocessor();

    @Nullable
    @Override
    public String applyImport(boolean useFullPath, String directory) {
        ResourceLocation resourcelocation = ResourceLocation.tryParse(directory);
        ResourceLocation resourcelocation1 = ResourceLocation.fromNamespaceAndPath(resourcelocation.getNamespace(), "shaders/include/" + resourcelocation.getPath());
        try {
            Resource resource1 = Minecraft.getInstance().getResourceManager().getResource(resourcelocation1).get();
            return IOUtils.toString(resource1.open(), StandardCharsets.UTF_8);
        } catch (IOException ioexception) {
            BlatApi.LOGGER.error("Could not open GLSL import {}: {}", directory, ioexception.getMessage());
            return "#error " + ioexception.getMessage();
        }
    }
}
