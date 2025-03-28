package ru.blatfan.blatapi.fluffy_fur.registry.client;

import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.client.model.armor.EmptyArmorModel;
import ru.blatfan.blatapi.fluffy_fur.client.model.book.CustomBookModel;
import ru.blatfan.blatapi.fluffy_fur.client.model.item.BowItemOverrides;
import ru.blatfan.blatapi.fluffy_fur.client.model.item.CustomItemOverrides;
import ru.blatfan.blatapi.fluffy_fur.client.model.item.CustomModel;
import ru.blatfan.blatapi.fluffy_fur.client.model.item.CustomRenderModel;
import ru.blatfan.blatapi.fluffy_fur.client.model.playerskin.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Map;

public class FluffyFurModels {
    public static final ModelLayerLocation EMPTY_ARMOR_LAYER = addLayer("empty_armor");

    public static final ModelLayerLocation BOOK_LAYER = addLayer("book");

    public static EmptyArmorModel EMPTY_ARMOR = null;

    public static CustomBookModel BOOK = null;

    @Mod.EventBusSubscriber(modid = FluffyFur.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents {
        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(EMPTY_ARMOR_LAYER, EmptyArmorModel::createBodyLayer);

            event.registerLayerDefinition(BOOK_LAYER, CustomBookModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers event) {
            EMPTY_ARMOR = new EmptyArmorModel(event.getEntityModels().bakeLayer(EMPTY_ARMOR_LAYER));

            BOOK = new CustomBookModel(event.getEntityModels().bakeLayer(BOOK_LAYER));
        }
    }

    public static ModelLayerLocation addLayer(String layer) {
        return addLayer(FluffyFur.MOD_ID, layer);
    }

    public static ModelLayerLocation addLayer(String modId, String layer) {
        return new ModelLayerLocation(new ResourceLocation(modId, layer), "main");
    }

    public static ModelResourceLocation addCustomModel(String modId, String model) {
        return new ModelResourceLocation(modId, model, "");
    }

    public static void addCustomRenderItemModel(Map<ResourceLocation, BakedModel> map, ResourceLocation item) {
        BakedModel model = map.get(new ModelResourceLocation(item, "inventory"));
        CustomModel customModel = new CustomRenderModel(model, new CustomItemOverrides());
        map.replace(new ModelResourceLocation(item, "inventory"), customModel);
    }

    public static void addBowItemModel(Map<ResourceLocation, BakedModel> map, ResourceLocation item, BowItemOverrides itemOverrides) {
        BakedModel model = map.get(new ModelResourceLocation(item, "inventory"));
        CustomModel customModel = new CustomModel(model, itemOverrides);

        for (int i = 0; i < 3; i++) {
            BakedModel pullModel = map.get(new ModelResourceLocation(new ResourceLocation(item.toString() + "_pulling_" + String.valueOf(i)), "inventory"));
            itemOverrides.models.add(pullModel);
        }

        map.replace(new ModelResourceLocation(item, "inventory"), customModel);
    }

    public static void addBowItemModel(Map<ResourceLocation, BakedModel> map, ResourceLocation item) {
        addBowItemModel(map, item, new BowItemOverrides());
    }

    public static ArrayList<ModelResourceLocation> getBowModels(String modId, String item) {
        ArrayList<ModelResourceLocation> models = new ArrayList<>();
        models.add(new ModelResourceLocation(new ResourceLocation(modId, item), "inventory"));
        models.add(new ModelResourceLocation(new ResourceLocation(modId, item + "_pulling_0"), "inventory"));
        models.add(new ModelResourceLocation(new ResourceLocation(modId, item + "_pulling_1"), "inventory"));
        models.add(new ModelResourceLocation(new ResourceLocation(modId, item + "_pulling_2"), "inventory"));
        return models;
    }

    public static void addBowItemModel(ModelEvent.RegisterAdditional event, String modId, String item) {
        for (ModelResourceLocation model : getBowModels(modId, item)) {
            event.register(model);
        }
    }
}
