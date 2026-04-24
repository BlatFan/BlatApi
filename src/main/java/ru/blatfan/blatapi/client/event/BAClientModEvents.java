package ru.blatfan.blatapi.client.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.model.armor.EmptyArmorModel;
import ru.blatfan.blatapi.client.particle.type.*;
import ru.blatfan.blatapi.client.playerskin.PlayerSkinHandler;
import ru.blatfan.blatapi.client.registry.*;
import ru.blatfan.blatapi.client.shader.postprocess.GlowPostProcess;
import ru.blatfan.blatapi.client.shader.postprocess.PostProcessHandler;
import ru.blatfan.blatapi.common.BARegistry;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = BlatApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BAClientModEvents {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BARegistry.Particles.EMERALD.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.WISP.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.TINY_WISP.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.SPARKLE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.STAR.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.TINY_STAR.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.FIRE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.SQUARE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.DOT.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.CIRCLE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.TINY_CIRCLE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.HEART.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.SKULL.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.SMOKE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.TRAIL.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.PANCAKE.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.DEATH.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.EARTH.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.SUN.get(), GenericParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.ITEM.get(), ItemParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.BLOCK.get(), BlockParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.FLUID.get(), FluidParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.SPRITE.get(), SpriteParticleType.Factory::new);
        event.registerSpriteSet(BARegistry.Particles.CHERRY_LEAVES.get(), LeavesParticleType.Factory::new);
    }
    
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BAKeyMappings.SKIN_MENU);
    }
    
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BAModels.EMPTY_ARMOR_LAYER, EmptyArmorModel::createBodyLayer);
    }
    
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        BAModels.EMPTY_ARMOR = new EmptyArmorModel(event.getEntityModels().bakeLayer(BAModels.EMPTY_ARMOR_LAYER));
    }
    
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BlockEntityRenderers.register(BARegistry.BlockEntities.SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(BARegistry.BlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);
    }
    
    @SubscribeEvent
    public static void registerShaders(FMLClientSetupEvent event) {
        PostProcessHandler.addInstance(GlowPostProcess.INSTANCE);
        
        PlayerSkinHandler.register(BAPlayerSkins.BLATFAN_SKIN);
        PlayerSkinHandler.register(BAPlayerSkins.EMERALD_EFFECT);
        PlayerSkinHandler.register(BAPlayerSkins.BROKENBLOCK_EFFECT);
        PlayerSkinHandler.register(BAPlayerSkins.BLATFAN_CAPE);
        PlayerSkinHandler.register(BAPlayerSkins.BROKENBLOCK_CAPE);
        
        BARenderTypes.addAdditiveParticleRenderType(BARenderTypes.ADDITIVE_PARTICLE);
        BARenderTypes.addAdditiveParticleRenderType(BARenderTypes.ADDITIVE_BLOCK_PARTICLE);
        BARenderTypes.addAdditiveRenderType(BARenderTypes.ADDITIVE_PARTICLE_TEXTURE);
        BARenderTypes.addAdditiveRenderType(BARenderTypes.ADDITIVE_TEXTURE);
        BARenderTypes.addAdditiveRenderType(BARenderTypes.ADDITIVE);
        BARenderTypes.addTranslucentParticleRenderType(BARenderTypes.TRANSLUCENT_PARTICLE);
        BARenderTypes.addTranslucentParticleRenderType(BARenderTypes.TRANSLUCENT_BLOCK_PARTICLE);
        BARenderTypes.addTranslucentRenderType(BARenderTypes.TRANSLUCENT_PARTICLE_TEXTURE);
        BARenderTypes.addTranslucentRenderType(BARenderTypes.TRANSLUCENT_TEXTURE);
        BARenderTypes.addTranslucentRenderType(BARenderTypes.TRANSLUCENT);
    }
    
    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), BlatApi.loc("additive_texture"), DefaultVertexFormat.POSITION_TEX_COLOR), shader -> BAShaders.ADDITIVE_TEXTURE = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), BlatApi.loc("additive"), DefaultVertexFormat.POSITION_COLOR), shader -> BAShaders.ADDITIVE = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), BlatApi.loc("translucent_texture"), DefaultVertexFormat.PARTICLE), shader -> BAShaders.TRANSLUCENT_TEXTURE = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), BlatApi.loc("translucent"), DefaultVertexFormat.PARTICLE), shader -> BAShaders.TRANSLUCENT = shader);
    }
}
