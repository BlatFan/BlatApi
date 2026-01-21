/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package ru.blatfan.blatapi.client.guide_book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.guide_book.GuideBookItem;

import java.util.Map;

public class BookModel extends BakedModelWrapper<BakedModel> {
    private final ItemOverrides itemHandler;
    
    private BookModel(BakedModel original) {
        super(original);
        this.itemHandler = new ItemOverrides() {
            @Override
            public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack,
                                      @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
                GuideBookData book = GuideBookItem.getBook(stack);
                if (book != null) {
                    ModelResourceLocation modelPath = new ModelResourceLocation(book.getModel(), "inventory");
                    return Minecraft.getInstance().getModelManager().getModel(modelPath);
                }
                return model;
            }
        };
    }
    
    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        return this.itemHandler;
    }
    
    public static void replace(Map<ResourceLocation, BakedModel> models, ModelBakery bakery) {
        ModelResourceLocation key = new ModelResourceLocation(BARegistry.Items.GUIDE_BOOK.getId(), "inventory");
        models.computeIfPresent(key, (k, oldModel) -> new BookModel(oldModel));
    }
}