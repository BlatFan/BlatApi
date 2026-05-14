package ru.blatfan.blatapi.utils;

import com.google.gson.*;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.*;

@UtilityClass
public final class NBTToJson {
    public static JsonElement toJson(Tag nbt) {
        //noinspection ChainOfInstanceofChecks
        if (nbt instanceof CompoundTag) {
            return toJsonObject((CompoundTag) nbt);
        } else if (nbt instanceof CollectionTag) {
            return toJsonArray((CollectionTag<?>) nbt);
        } else if (nbt instanceof NumericTag) {
            return new JsonPrimitive(((NumericTag) nbt).getAsNumber());
        } else if (nbt instanceof StringTag) {
            return new JsonPrimitive(nbt.getAsString());
        }
        return JsonNull.INSTANCE;
    }
    
    public static JsonObject toJsonObject(CompoundTag nbt) {
        JsonObject json = new JsonObject();
        for (String key : nbt.getAllKeys()) {
            Tag element = nbt.get(key);
            if (element != null)
                json.add(key, toJson(element));
        }
        return json;
    }
    
    public static JsonArray toJsonArray(CollectionTag<?> nbt) {
        JsonArray json = new JsonArray();
        for (Tag element : nbt)
            json.add(toJson(element));
        return json;
    }
}