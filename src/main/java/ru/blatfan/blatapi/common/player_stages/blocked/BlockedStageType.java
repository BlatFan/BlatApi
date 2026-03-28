package ru.blatfan.blatapi.common.player_stages.blocked;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class BlockedStageType<T extends BlockedStage.BlockedStageValue> {
    private static final Map<ResourceLocation, BlockedStageType<?>> REGISTRY = new HashMap<>();
    
    public static <T extends BlockedStage.BlockedStageValue> BlockedStageType<T> register(ResourceLocation id, Function<JsonElement, T> parser) {
        if (REGISTRY.containsKey(id))
            throw new IllegalStateException("BlockedStageType already registered: " + id);
        BlockedStageType<T> type = new BlockedStageType<>(id, parser);
        REGISTRY.put(id, type);
        return type;
    }
    
    public static void init() {
        BlockedStageType.register(BlatApi.loc("item"), BlockedStage.ItemStageValue::parseItem);
        BlockedStageType.register(BlatApi.loc("loot"), BlockedStage.ItemStageValue::parseLoot);
        BlockedStageType.register(BlatApi.loc("tag"), BlockedStage.TagStageValue::parse);
        BlockedStageType.register(BlatApi.loc("mod"), BlockedStage.ModStageValue::parse);
        BlockedStageType.register(BlatApi.loc("dimension"), BlockedStage.DimensionStageValue::parse);
        BlockedStageType.register(BlatApi.loc("block"), BlockedStage.BlockStageValue::parse);
    }
    
    
    @SuppressWarnings("unchecked")
    public static <T extends BlockedStage.BlockedStageValue> BlockedStageType<T> byId(ResourceLocation id) {
        BlockedStageType<?> type = REGISTRY.get(id);
        if (type == null)
            throw new IllegalArgumentException("Unknown BlockedStageType id: " + id);
        return (BlockedStageType<T>) type;
    }
    
    public static Map<ResourceLocation, BlockedStageType<?>> allTypes() {
        return Map.copyOf(REGISTRY);
    }
    
    private final ResourceLocation id;
    private final Function<JsonElement, T> parser;
    
    private BlockedStageType(ResourceLocation id, Function<JsonElement, T> parser) {
        this.id = id;
        this.parser = parser;
    }
    
    public ResourceLocation id() {
        return id;
    }
    
    public T parse(JsonElement element) {
        return parser.apply(element);
    }
}