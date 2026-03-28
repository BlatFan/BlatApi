package ru.blatfan.blatapi.common.player_stages.blocked;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.collection.Couple;
import ru.blatfan.blatapi.utils.collection.ItemStackList;

import java.util.List;

@Getter
public class BlockedStage<T extends BlockedStage.BlockedStageValue> {
    private final List<Task> tasks;
    private final boolean visible;
    private final T value;
    
    public BlockedStage(List<Task> tasks, boolean visible, T value) {
        this.tasks = tasks;
        this.visible = visible;
        this.value = value;
    }
    
    public interface BlockedStageValue {
        ResourceLocation getTypeId();
    }
    
    @Getter
    public static class ItemStageValue implements BlockedStageValue {
        private final String logicalType;
        private final ItemStackList items;
        
        public ItemStageValue(String logicalType, ItemStackList items) {
            this.logicalType = logicalType;
            this.items = items;
        }
        
        @Override
        public ResourceLocation getTypeId() {
            return BlatApi.locParse(logicalType);
        }
        
        public static ItemStageValue parseItem(JsonElement element) {
            return new ItemStageValue("item", RecipeUtil.itemStacksListFromJson(element));
        }
        
        public static ItemStageValue parseLoot(JsonElement element) {
            return new ItemStageValue("loot", RecipeUtil.itemStacksListFromJson(element));
        }
    }
    
    @Getter
    public static class TagStageValue implements BlockedStageValue {
        private final List<Ingredient> tags;
        
        public TagStageValue(List<Ingredient> tags) {
            this.tags = tags;
        }
        
        @Override
        public ResourceLocation getTypeId() {
            return BlatApi.loc("tag");
        }
        
        public static TagStageValue parse(JsonElement element) {
            return new TagStageValue(RecipeUtil.getIngFromJson(element));
        }
    }
    
    @Getter
    public static class ModStageValue implements BlockedStageValue {
        private final String modId;
        
        public ModStageValue(String modId) {
            this.modId = modId;
        }
        
        @Override
        public ResourceLocation getTypeId() {
            return BlatApi.loc("mod");
        }
        
        public static ModStageValue parse(JsonElement element) {
            return new ModStageValue(element.getAsString());
        }
    }
    
    @Getter
    public static class DimensionStageValue implements BlockedStageValue {
        private final ResourceLocation dimension;
        
        public DimensionStageValue(ResourceLocation dimension) {
            this.dimension = dimension;
        }
        
        @Override
        public ResourceLocation getTypeId() {
            return BlatApi.loc("dimension");
        }
        
        public static DimensionStageValue parse(JsonElement element) {
            return new DimensionStageValue(ResourceLocation.parse(element.getAsString()));
        }
    }
    
    @Getter
    public static class BlockStageValue implements BlockedStageValue {
        private final Couple<ResourceLocation, ResourceLocation> blockPair;
        
        public BlockStageValue(Couple<ResourceLocation, ResourceLocation> pair) {
            this.blockPair = pair;
        }
        
        @Override
        public ResourceLocation getTypeId() {
            return BlatApi.loc("block");
        }
        
        public static BlockStageValue parse(JsonElement element) {
            JsonObject json = element.getAsJsonObject();
            ResourceLocation v1 = ResourceLocation.parse(json.get("block").getAsString());
            ResourceLocation v2 = json.has("replacement")
                ? ResourceLocation.parse(json.get("replacement").getAsString())
                : BlatApi.loc("empty");
            return new BlockStageValue(new Couple<>(v1, v2));
        }
    }
    
    public static <T extends BlockedStageValue> BlockedStage<T> fromJson(JsonObject json) {
        ResourceLocation typeId = ResourceLocation.parse(json.get("type").getAsString());
        BlockedStageType<T> type = BlockedStageType.byId(typeId);
        
        List<Task> tasks = Task.fromJson(json.getAsJsonArray("tasks"));
        boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
        T value = type.parse(json.get("value"));
        
        return new BlockedStage<>(tasks, visible, value);
    }
}
