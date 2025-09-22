package ru.blatfan.blatapi.mixins.client;

import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.client.guide_book.BookModel;

import java.util.Map;

@Mixin(ModelManager.class)
public abstract class MixinModelManager {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;getBakedTopLevelModels()Ljava/util/Map;"), method = "loadModels")
    public void replaceBookModel(ProfilerFiller pProfilerFiller, Map<ResourceLocation, AtlasSet.StitchResult> pAtlasPreparations, ModelBakery modelBakery, CallbackInfoReturnable<?> cir) {
        BookModel.replace(modelBakery.getBakedTopLevelModels(), modelBakery);
    }
}