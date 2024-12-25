package ru.blatfan.blatapi.fluffy_fur.client.model.item;

import net.minecraft.client.resources.model.BakedModel;

public class CustomRenderModel extends CustomModel {

    public CustomRenderModel(BakedModel baseModel, CustomItemOverrides itemOverrides) {
        super(baseModel, itemOverrides);
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }
}