package ru.blatfan.blatapi.client;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RenderOverrideManager {
    private static final Map<ResourceLocation, ResourceLocation> BLOCK_REPLACEMENTS = new HashMap<>();
    
    public static void registerReplacement(ResourceLocation blockedId, ResourceLocation replacementId) {
        BLOCK_REPLACEMENTS.put(blockedId, replacementId);
    }
    
    public static void clearReplacements() {
        BLOCK_REPLACEMENTS.clear();
    }
    
    public static boolean isReplacementActive(ResourceLocation blockId) {
        return BLOCK_REPLACEMENTS.containsKey(blockId);
    }
    
    public static ResourceLocation getReplacement(ResourceLocation blockId) {
        return BLOCK_REPLACEMENTS.get(blockId);
    }
}