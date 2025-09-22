package ru.blatfan.blatapi.common.reward;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;

@AllArgsConstructor
@Getter
public class AttributeReward extends Reward {
    private final boolean visible;
    private final ResourceLocation attributeLoc;
    private final float value;
    private final AttributeModifier.Operation operation;
    @Override
    public void apply(Player player) {
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(attributeLoc);
        if (attribute==null) {
            BlatApi.LOGGER.warn("Unknow attribute {}", attributeLoc);
            return;
        }
        if(player.getAttributes().hasAttribute(attribute)){
            player.getAttribute(attribute).addPermanentModifier(
                new AttributeModifier("Skill", value, operation)
            );
        }
    }
    
    public static AttributeReward fromJson(JsonObject json){
        ResourceLocation lc = ResourceLocation.tryParse(json.get("attribute").getAsString());
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        float v = json.get("value").getAsFloat();
        AttributeModifier.Operation op = AttributeModifier.Operation.valueOf(json.get("operation").getAsString().toUpperCase());
        return new AttributeReward(b, lc, v, op);
    }
    
    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("attribute", attributeLoc.toString());
        tag.putBoolean("visible", visible);
        tag.putFloat("value", value);
        tag.putString("op", operation.name());
        return tag;
    }
    
    public static Reward fromTag(CompoundTag tag) {
        return new AttributeReward(
            tag.getBoolean("visible"),
            ResourceLocation.tryParse(tag.getString("attribute")),
            tag.getFloat("value"),
            AttributeModifier.Operation.valueOf(tag.getString("op"))
        );
    }
    
    @Override
    public Component text(Player player) {
        return Component.translatable(BuiltInRegistries.ATTRIBUTE.get(getAttributeLoc()).getDescriptionId())
            .append(": ").withStyle(ChatFormatting.GRAY).append(Component.literal(
                    (getValue()>0 ? "+":"")+
                        (getOperation()== AttributeModifier.Operation.ADDITION ? getValue() : (getValue()*100)+"%"))
                .withStyle(getValue()>0 ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}