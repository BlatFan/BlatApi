package ru.blatfan.blatapi.common.reward;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AttributeReward extends Reward {
    private final boolean visible;
    private final ResourceLocation attributeLoc;
    private final float value;
    private final AttributeModifier.Operation operation;
    
    private final UUID uuid;
    private final String name;
    
    @Override
    public void apply(Player player) {
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(attributeLoc);
        if (attribute==null) {
            BlatApi.LOGGER.warn("Unknown attribute {}", attributeLoc);
            return;
        }
        if(player.getAttributes().hasAttribute(attribute))
            player.getAttribute(attribute).addPermanentModifier(
                new AttributeModifier(uuid, name, value, operation)
            );
    }
    
    public static AttributeReward fromJson(JsonObject json){
        ResourceLocation lc = ResourceLocation.tryParse(json.get("attribute").getAsString());
        boolean b = !json.has("visible") || json.get("visible").getAsBoolean();
        float v = json.get("value").getAsFloat();
        AttributeModifier.Operation op = AttributeModifier.Operation.valueOf(json.get("operation").getAsString().toUpperCase());
        UUID uuid = json.has("uuid") ? UUID.fromString(json.get("uuid").getAsString()) : Mth.createInsecureUUID(RandomSource.createNewThreadLocalInstance());
        String name = json.has("name") ? json.get("name").getAsString() : "attribute_reward";
        return new AttributeReward(b, lc, v, op, uuid, name);
    }
    
    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("attribute", attributeLoc.toString());
        tag.putBoolean("visible", visible);
        tag.putFloat("value", value);
        tag.putString("op", operation.name());
        tag.putUUID("uuid", uuid);
        tag.putString("name", name);
        return tag;
    }
    
    public static Reward fromTag(CompoundTag tag) {
        return new AttributeReward(
            tag.getBoolean("visible"),
            ResourceLocation.tryParse(tag.getString("attribute")),
            tag.getFloat("value"),
            AttributeModifier.Operation.valueOf(tag.getString("op")),
            tag.getUUID("uuid"),
            tag.getString("name")
        );
    }
    
    @Override
    public Text text(Player player) {
        return Text.create(BuiltInRegistries.ATTRIBUTE.get(getAttributeLoc()).getDescriptionId())
            .add(": ").withStyle(ChatFormatting.GRAY).add(Component.literal(
                    (getValue()>0 ? "+":"")+
                        (getOperation()== AttributeModifier.Operation.ADDITION ? getValue() : (getValue()*100)+"%"))
                .withStyle(getValue()>0 ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}