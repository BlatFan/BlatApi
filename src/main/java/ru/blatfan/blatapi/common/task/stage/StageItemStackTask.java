package ru.blatfan.blatapi.common.task.stage;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.ItemHelper;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.collection.Text;

@AllArgsConstructor
@Getter
public class StageItemStackTask extends Task {
    private boolean visible;
    private ResourceLocation stage;
    private ItemStack value;
    
    @Override
    public boolean get(Player player) {
        if (!PlayerStages.has(player, stage)) return false;
        ItemStack st = PlayerStages.getItemStack(player, stage);
        return ItemHelper.areStacksEqual(st, value) && st.getCount() >= this.value.getCount();
    }
    
    @Override
    public ResourceLocation getType() {
        return BlatApi.loc("stage_item_stack");
    }
    
    @Override
    public Text text(Player player) {
        return Text.create("task.blatapi.stage_item_stack", stage, value.getHoverName(), value.getCount())
            .withStyle(get(player) ? ChatFormatting.GREEN : ChatFormatting.RED);
    }
    
    public static class Serializer implements ITaskSerializer {
        public static final Serializer INSTANCE = new Serializer();
        protected Serializer() {}
        
        @Override
        public Task fromJson(JsonObject json) {
            boolean visible = !json.has("visible") || json.get("visible").getAsBoolean();
            ResourceLocation stage = ResourceLocation.parse(json.get("stage").getAsString());
            ItemStack value = RecipeUtil.itemStackFromJson(json.getAsJsonObject("value"));
            return new StageItemStackTask(visible, stage, value);
        }
        
        @Override
        public Task fromNBT(CompoundTag tag) {
            boolean visible = !tag.contains("visible") || tag.getBoolean("visible");
            ResourceLocation stage = ResourceLocation.parse(tag.getString("stage"));
            ItemStack value = ItemStack.of(tag.getCompound("value"));
            return new StageItemStackTask(visible, stage, value);
        }
        
        @Override
        public CompoundTag toNBT(Task task) {
            CompoundTag tag = new CompoundTag();
            if (task instanceof StageItemStackTask t) {
                tag.putBoolean("visible", t.visible);
                tag.putString("stage", t.stage.toString());
                tag.put("value", t.value.save(new CompoundTag()));
            }
            return tag;
        }
    }
}
