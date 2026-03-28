package ru.blatfan.blatapi.common.player_stages.blocked;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.RenderOverrideManager;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage.*;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.compat.kubejs.BAKubeJS;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockedStagesManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    
    private static final Map<ResourceLocation, BlockedStage<ItemStageValue>> ITEM_STAGES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BlockedStage<ItemStageValue>> LOOT_STAGES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BlockedStage<TagStageValue>> TAG_STAGES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BlockedStage<ModStageValue>> MOD_STAGES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BlockedStage<BlockStageValue>> BLOCK_STAGES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BlockedStage<DimensionStageValue>> DIMENSION_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<?>> OTHER_STAGES = new ConcurrentHashMap<>();
    
    public static final Map<ResourceLocation, BlockedStage<ItemStageValue>> KUBEJS_ITEM_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<ItemStageValue>> KUBEJS_LOOT_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<TagStageValue>> KUBEJS_TAG_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<ModStageValue>> KUBEJS_MOD_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<BlockStageValue>> KUBEJS_BLOCK_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<DimensionStageValue>> KUBEJS_DIMENSION_STAGES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, BlockedStage<?>> KUBEJS_OTHER_STAGES = new ConcurrentHashMap<>();
    
    public BlockedStagesManager() {
        super(GSON, "blocked_stages");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ITEM_STAGES.clear();
        LOOT_STAGES.clear();
        TAG_STAGES.clear();
        MOD_STAGES.clear();
        BLOCK_STAGES.clear();
        DIMENSION_STAGES.clear();
        
        map.forEach(BlockedStagesManager::addFromJson);
        if(ModList.get().isLoaded("kubejs")) BAKubeJS.postBSRegister();
        
        BlatApi.LOGGER.debug("--------- Blocked Stages -----------");
        BlatApi.LOGGER.debug("Loaded ITEM: {}", ITEM_STAGES.size());
        BlatApi.LOGGER.debug("Loaded LOOT: {}", LOOT_STAGES.size());
        BlatApi.LOGGER.debug("Loaded TAG: {}", TAG_STAGES.size());
        BlatApi.LOGGER.debug("Loaded MOD: {}", MOD_STAGES.size());
        BlatApi.LOGGER.debug("Loaded BLOCK: {}", BLOCK_STAGES.size());
        BlatApi.LOGGER.debug("Loaded DIMENSION: {}", DIMENSION_STAGES.size());
        BlatApi.LOGGER.debug("Loaded OTHER: {}", OTHER_STAGES.size());
        BlatApi.LOGGER.debug("Loaded from KubeJS: {}", getAllKubeJS().size());
        BlatApi.LOGGER.debug("------------------------------------");
    }
    
    private static Map<ResourceLocation, BlockedStage<?>> getAllKubeJS() {
        Map<ResourceLocation, BlockedStage<?>> all = new HashMap<>();
        all.putAll(KUBEJS_ITEM_STAGES);
        all.putAll(KUBEJS_LOOT_STAGES);
        all.putAll(KUBEJS_TAG_STAGES);
        all.putAll(KUBEJS_MOD_STAGES);
        all.putAll(KUBEJS_BLOCK_STAGES);
        all.putAll(KUBEJS_DIMENSION_STAGES);
        all.putAll(KUBEJS_OTHER_STAGES);
        return all;
    }
    
    public static void updateRenderOverrides(Player player) {
        RenderOverrideManager.clearReplacements();
        
        for (BlockedStage<BlockStageValue> stage : getBlockStages()) {
            if(Task.get(player, stage.getTasks())) continue;
            ResourceLocation blockedBlock = stage.getValue().getBlockPair().getKey();
            ResourceLocation replacement = stage.getValue().getBlockPair().getValue();
            if (replacement != null)
                RenderOverrideManager.registerReplacement(blockedBlock, replacement);
        }
    }
    
    private static void addFromJson(ResourceLocation id, JsonElement je) {
        BlockedStage<?> stage = BlockedStage.fromJson(je.getAsJsonObject());
        add(id, stage);
    }
    
    @SuppressWarnings("unchecked")
    public static void add(ResourceLocation id, BlockedStage<?> stage) {
        if (stage == null) {
            BlatApi.LOGGER.error("{} is empty", id);
            return;
        }
        BlatApi.LOGGER.debug("Added {}", id);
        
        var value = stage.getValue();
        ResourceLocation typeId = value.getTypeId();
        
        if (typeId.equals(BlatApi.loc("item"))) ITEM_STAGES.put(id, (BlockedStage<ItemStageValue>) stage);
        else if (typeId.equals(BlatApi.loc("loot"))) LOOT_STAGES.put(id, (BlockedStage<ItemStageValue>) stage);
        else if (typeId.equals(BlatApi.loc("tag"))) TAG_STAGES.put(id, (BlockedStage<TagStageValue>) stage);
        else if (typeId.equals(BlatApi.loc("mod"))) MOD_STAGES.put(id, (BlockedStage<ModStageValue>) stage);
        else if (typeId.equals(BlatApi.loc("block"))) BLOCK_STAGES.put(id, (BlockedStage<BlockStageValue>) stage);
        else if (typeId.equals(BlatApi.loc("dimension"))) DIMENSION_STAGES.put(id, (BlockedStage<DimensionStageValue>) stage);
        else OTHER_STAGES.put(id, stage);
    }
    
    @SafeVarargs
    private static <T extends BlockedStageValue> Map<ResourceLocation, BlockedStage<T>> get(Map<ResourceLocation, BlockedStage<T>>... data){
        Map<ResourceLocation, BlockedStage<T>> map = new HashMap<>();
        for (Map<ResourceLocation, BlockedStage<T>> datum : data)
            map.putAll(datum);
        return map;
    }
    
    @SafeVarargs
    private static Map<ResourceLocation, BlockedStage<?>> getOther(Map<ResourceLocation, BlockedStage<?>>... data) {
        Map<ResourceLocation, BlockedStage<?>> map = new HashMap<>();
        for (Map<ResourceLocation, BlockedStage<?>> datum : data)
            map.putAll(datum);
        return map;
    }
    
    public static Collection<BlockedStage<ItemStageValue>> getItemStages() {
        return get(ITEM_STAGES, KUBEJS_ITEM_STAGES).values();
    }
    
    public static Collection<BlockedStage<ItemStageValue>> getLootStages() {
        return get(LOOT_STAGES, KUBEJS_LOOT_STAGES).values();
    }
    
    public static Collection<BlockedStage<TagStageValue>> getTagStages() {
        return get(TAG_STAGES, KUBEJS_TAG_STAGES).values();
    }
    
    public static Collection<BlockedStage<ModStageValue>> getModStages() {
        return get(MOD_STAGES, KUBEJS_MOD_STAGES).values();
    }
    
    public static Collection<BlockedStage<BlockStageValue>> getBlockStages() {
        return get(BLOCK_STAGES, KUBEJS_BLOCK_STAGES).values();
    }
    
    public static Collection<BlockedStage<DimensionStageValue>> getDimensionStages() {
        return get(DIMENSION_STAGES, KUBEJS_DIMENSION_STAGES).values();
    }
    
    public static Collection<BlockedStage<?>> getOtherStages() {
        return getOther(OTHER_STAGES, KUBEJS_OTHER_STAGES).values();
    }
    
    public static Map<ResourceLocation, BlockedStage<?>> getAll() {
        Map<ResourceLocation, BlockedStage<?>> all = new HashMap<>();
        all.putAll(ITEM_STAGES);
        all.putAll(LOOT_STAGES);
        all.putAll(TAG_STAGES);
        all.putAll(MOD_STAGES);
        all.putAll(BLOCK_STAGES);
        all.putAll(DIMENSION_STAGES);
        all.putAll(OTHER_STAGES);
        all.putAll(getAllKubeJS());
        return all;
    }
}
