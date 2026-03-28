package ru.blatfan.blatapi.common.item;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.model.item.CustomModel;
import ru.blatfan.blatapi.client.model.item.ItemSkinItemOverrides;
import ru.blatfan.blatapi.client.model.item.ItemSkinModels;
import ru.blatfan.blatapi.client.render.item.LargeItemRenderer;

import java.util.Map;

public class BAItemSkins {
    @Mod.EventBusSubscriber(modid = BlatApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents {
        @SubscribeEvent
        public static void modelRegistrySkins(ModelEvent.RegisterAdditional event) {
            for (String skin : ItemSkinModels.getSkins()) {
                event.register(ItemSkinModels.getModelLocationSkin(skin));
            }
        }

        @SubscribeEvent
        public static void modelBakeSkins(ModelEvent.ModifyBakingResult event) {
            Map<ResourceLocation, BakedModel> map = event.getModels();

            for (String skin : ItemSkinModels.getSkins()) {
                BakedModel model = map.get(ItemSkinModels.getModelLocationSkin(skin));
                ItemSkinModels.addModelSkins(skin, model);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addSkinModel(Map<ResourceLocation, BakedModel> map, ResourceLocation id) {
        BakedModel model = map.get(new ModelResourceLocation(id, "inventory"));
        CustomModel newModel = new CustomModel(model, new ItemSkinItemOverrides());
        map.replace(new ModelResourceLocation(id, "inventory"), newModel);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addLargeModel(Map<ResourceLocation, BakedModel> map, String modId, String skin) {
        LargeItemRenderer.bakeModel(map, modId, "skin/"+skin);
        ItemSkinModels.addModelSkins(modId+":"+skin, map.get(ItemSkinModels.getModelLocationSkin(modId+":"+skin)));
    }
}
