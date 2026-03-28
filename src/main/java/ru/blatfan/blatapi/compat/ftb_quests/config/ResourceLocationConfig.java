package ru.blatfan.blatapi.compat.ftb_quests.config;

import dev.ftb.mods.ftblibrary.config.ConfigFromString;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ResourceLocationConfig extends ConfigFromString<ResourceLocation> {
    @Override
    public boolean parse(@Nullable Consumer<ResourceLocation> consumer, String s) {
        try {
            if (s == null || s.trim().isEmpty())
                return false;
            ResourceLocation loc = ResourceLocation.parse(s);
            return okValue(consumer, loc);
        } catch (Exception e) {
            return false;
        }
    }
}
