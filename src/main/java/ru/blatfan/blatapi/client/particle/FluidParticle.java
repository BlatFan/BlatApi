package ru.blatfan.blatapi.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluids;
import ru.blatfan.blatapi.client.particle.options.FluidParticleOptions;
import ru.blatfan.blatapi.client.render.FluidRenderMap;

public class FluidParticle extends GenericParticle {
    public FluidParticle(ClientLevel level, FluidParticleOptions options, double x, double y, double z, double vx, double vy, double vz) {
        super(level, options, null, x, y, z, vx, vy, vz);
        TextureAtlasSprite sprite;
        if (!options.fluidStack.isEmpty())
            sprite = FluidRenderMap.getFluidTexture(options.fluidStack, options.flowing ? FluidRenderMap.FluidFlow.FLOWING : FluidRenderMap.FluidFlow.STILL);
        else sprite = FluidRenderMap.getFluidTexture(Fluids.WATER);
        if(sprite!=null) this.setSprite(sprite);
    }
}