package ru.blatfan.blatapi.common.guide_book;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.Text;

import java.awt.*;

@RequiredArgsConstructor@Getter
public class GuideBookData{
    private final ResourceLocation texture;
    private final Component title;
    private final Component author;
    private final Color titleColor;
    private final ItemStack pedestal;
    private final Component tooltip;
    
    private final ResourceLocation model;
    
    public static GuideBookData json(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        ResourceLocation texture = ResourceLocation.tryParse(json.get("texture").getAsString());
        Component title = Text.create(json.get("title").getAsString()).asComponent();
        Component author = Text.create(json.get("author").getAsString());
        Color titleColor = Color.decode(json.get("title_color").getAsString());
        ItemStack pedestal = json.has("pedestal") ? RecipeUtil.itemStackFromJson(json.get("pedestal")) : ItemStack.EMPTY;
        Component tooltip = json.has("tooltip") ? Text.create(json.get("tooltip").getAsString()) : Component.empty();
        ResourceLocation model = json.has("model") ? ResourceLocation.tryParse(json.get("model").getAsString()) : BlatApi.loc("guide_book");
        return new GuideBookData(texture, title, author, titleColor, pedestal, tooltip, model);
    }
}