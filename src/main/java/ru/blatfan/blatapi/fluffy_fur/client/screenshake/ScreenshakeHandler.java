package ru.blatfan.blatapi.fluffy_fur.client.screenshake;

import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.client.event.ClientTickHandler;
import ru.blatfan.blatapi.fluffy_fur.common.raycast.RayCast;
import ru.blatfan.blatapi.fluffy_fur.common.raycast.RayCastContext;
import ru.blatfan.blatapi.fluffy_fur.common.raycast.RayHitResult;
import ru.blatfan.blatapi.fluffy_fur.config.FluffyFurClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ComputeFovModifierEvent;

import java.util.ArrayList;

public class ScreenshakeHandler {
    public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();
    private static final RandomSource RANDOM = RandomSource.create();
    public static float intensityRotation;
    public static float intensityPosition;
    public static float intensityFov;
    public static float intensityFovNormalize;
    public static Vec3 intensityVector = Vec3.ZERO;
    public static Vec3 intensityVectorOld = Vec3.ZERO;

    public static void cameraTick(Camera camera) {
        if (intensityRotation > 0) {
            float yawOffset = randomizeOffset(intensityRotation);
            float pitchOffset = randomizeOffset(intensityRotation);
            camera.setRotation(camera.getYRot() + yawOffset, camera.getXRot() + pitchOffset);
        }
        boolean cameraUpdate = false;
        Vec3 pos = camera.getPosition();
        Vec3 cameraPos = pos;
        if (intensityPosition > 0) {
            double angleA = RANDOM.nextDouble() * Math.PI * 2;
            double angleB = RANDOM.nextDouble() * Math.PI * 2;
            float x = (float) (Math.cos(angleA) * Math.cos(angleB)) * randomizeOffset(intensityPosition);
            float y = (float) (Math.sin(angleA) * Math.cos(angleB)) * randomizeOffset(intensityPosition);
            float z = (float) Math.sin(angleB) * randomizeOffset(intensityPosition);
            Vec3 posOffset = new Vec3(pos.x() + x, pos.y() + y, pos.z() + z);
            pos = new Vec3(posOffset.x(), posOffset.y(), posOffset.z());
            cameraUpdate = true;
        }
        if (!intensityVector.equals(Vec3.ZERO) && !intensityVectorOld.equals(Vec3.ZERO)) {
            float partialTicks = ClientTickHandler.partialTicks;
            double lx = Mth.lerp(partialTicks, intensityVectorOld.x(), intensityVector.x());
            double ly = Mth.lerp(partialTicks, intensityVectorOld.y(), intensityVector.y());
            double lz = Mth.lerp(partialTicks, intensityVectorOld.z(), intensityVector.z());
            Vec3 posOffset = new Vec3(pos.x() + lx, pos.y() + ly, pos.z() + lz);
            pos = new Vec3(posOffset.x(), posOffset.y(), posOffset.z());
            cameraUpdate = true;
        }
        if (cameraUpdate) {
            Level level = FluffyFur.proxy.getLevel();
            if (level != null) {
                RayHitResult hitResult = RayCast.getHit(level, new RayCastContext(cameraPos, pos).setBlockShape(RayCastContext.Block.VISUAL));
                double distance = Math.sqrt(Math.pow(cameraPos.x() - hitResult.getPos().x(), 2) + Math.pow(cameraPos.y() - hitResult.getPos().y(), 2) + Math.pow(cameraPos.z() - hitResult.getPos().z(), 2));
                distance = distance - 0.1f;
                if (distance < 0) distance = 0;
                double dX = cameraPos.x() - hitResult.getPos().x();
                double dY = cameraPos.y() - hitResult.getPos().y();
                double dZ = cameraPos.z() - hitResult.getPos().z();
                double yaw = Math.atan2(dZ, dX);
                double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
                double x = Math.sin(pitch) * Math.cos(yaw) * distance;
                double y = Math.cos(pitch) * distance;
                double z = Math.sin(pitch) * Math.sin(yaw) * distance;
                pos = new Vec3(cameraPos.x() + x, cameraPos.y() + y, cameraPos.z() + z);
            }
            camera.setPosition(pos.x(), pos.y(), pos.z());
        }
    }

    public static void fovTick(ComputeFovModifierEvent event) {
        float fovModifier = event.getFovModifier();
        if (fovModifier != event.getNewFovModifier()) fovModifier = event.getNewFovModifier();
        boolean update = false;
        if (intensityFov >= 0) {
            float offset = randomizeOffset(intensityFov);
            fovModifier = fovModifier + offset;
            update = true;
        }
        if (intensityFovNormalize != 0) {
            fovModifier = fovModifier + intensityFovNormalize;
            update = true;
        }
        if (update) event.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, fovModifier));
    }

    public static void clientTick(Camera camera) {
        double intensity = FluffyFurClientConfig.SCREENSHAKE_INTENSITY.get();
        double rotationNormalize = 0;
        double positionNormalize = 0;
        double fovNormalize = 0;
        double fovNorm = 0;
        double rotation = 0;
        double position = 0;
        double fov = 0;
        Vec3 vector = Vec3.ZERO;
        for (ScreenshakeInstance instance : INSTANCES) {
            double update = instance.updateIntensity(camera);
            if (instance.isRotation) {
                if (instance.isNormalize) {
                    rotationNormalize = rotationNormalize + update;
                } else {
                    if (rotation < update) rotation = update;
                }
            }
            if (instance.isPosition) {
                if (instance.isNormalize) {
                    positionNormalize = positionNormalize + update;
                } else {
                    if (position < update) position = update;
                }
            }
            if (instance.isFov) {
                if (instance.isNormalize) {
                    if (instance.isFovNormalize) {
                        if (fovNormalize < update) fovNormalize = fovNormalize + update;
                    } else {
                        fovNormalize = fovNormalize + update;
                    }
                } else {
                    if (instance.isFovNormalize) {
                        fovNorm = fovNorm + update;
                    } else {
                        fov = fov + update;
                    }
                }
            }
            if (instance.isVector) {
                if (instance.isNormalize) {
                    Vec3 newVec = instance.vector.scale(update * intensity);
                    double dX = newVec.x() - vector.x();
                    double dY = newVec.y() - vector.y();
                    double dZ = newVec.z() - vector.z();
                    vector = new Vec3(dX, dY, dZ);
                } else {
                    vector = vector.add(instance.vector.scale(update * intensity));
                }
            }
        }
        rotationNormalize = Math.min(rotationNormalize, intensity);
        positionNormalize = Math.min(positionNormalize, intensity);
        fovNorm = Math.min(fovNorm, intensity);
        rotation = rotation * intensity;
        position = position * intensity;
        fov = fov * intensity;

        intensityRotation = (float) Math.max(Math.pow(rotationNormalize, 3), rotation);
        intensityPosition = (float) Math.max(Math.pow(positionNormalize / 2, 3), position);
        intensityFov = (float) Math.max(fovNorm, fov);
        intensityFovNormalize = (float) fovNormalize;
        intensityVectorOld = intensityVector;
        intensityVector = vector;
        INSTANCES.removeIf(i -> i.progress >= i.duration);
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }

    public static float randomizeOffset(float offset) {
        return Mth.nextFloat(RANDOM, -offset * 2, offset * 2);
    }
}
