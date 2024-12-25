package ru.blatfan.blatapi.fluffy_fur.client.particle.behavior;

import com.mojang.blaze3d.vertex.PoseStack;
import ru.blatfan.blatapi.fluffy_fur.client.particle.GenericParticle;
import net.minecraft.client.renderer.MultiBufferSource;

public interface ICustomBehaviorParticleRender {
    void render(GenericParticle particle, PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
