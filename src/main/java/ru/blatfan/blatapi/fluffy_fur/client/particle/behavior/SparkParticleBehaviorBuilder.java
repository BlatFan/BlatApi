package ru.blatfan.blatapi.fluffy_fur.client.particle.behavior;

import ru.blatfan.blatapi.fluffy_fur.client.particle.data.ColorParticleData;
import ru.blatfan.blatapi.fluffy_fur.client.particle.data.GenericParticleData;
import ru.blatfan.blatapi.fluffy_fur.client.particle.data.SpinParticleData;
import ru.blatfan.blatapi.fluffy_fur.client.particle.options.GenericParticleOptions;
import net.minecraft.world.phys.Vec3;

public class SparkParticleBehaviorBuilder extends ParticleBehaviorBuilder {

    public ColorParticleData colorData = GenericParticleOptions.DEFAULT_COLOR;
    public GenericParticleData transparencyData = GenericParticleOptions.DEFAULT_GENERIC;
    public GenericParticleData scaleData =  GenericParticleData.create(1).build();
    public boolean secondColor = false;
    public Vec3 startPos = null;
    public Vec3 endPos = null;

    protected SparkParticleBehaviorBuilder(float xOffset, float yOffset, float zOffset) {
        super(xOffset, yOffset, zOffset);
    }

    public SparkParticleBehaviorBuilder setXSpinData(SpinParticleData xSpinData) {
        this.xSpinData = xSpinData;
        return this;
    }

    public SparkParticleBehaviorBuilder setYSpinData(SpinParticleData ySpinData) {
        this.ySpinData = ySpinData;
        return this;
    }

    public SparkParticleBehaviorBuilder setZSpinData(SpinParticleData zSpinData) {
        this.zSpinData = zSpinData;
        return this;
    }

    public SparkParticleBehaviorBuilder enableSided() {
        return setSided(true);
    }

    public SparkParticleBehaviorBuilder disableSided() {
        return setSided(false);
    }

    public SparkParticleBehaviorBuilder setSided(boolean side) {
        return setFirstSide(side).setSecondSide(side);
    }

    public SparkParticleBehaviorBuilder enableFirstSide() {
        return setFirstSide(true);
    }

    public SparkParticleBehaviorBuilder disableFirstSide() {
        return setFirstSide(false);
    }

    public SparkParticleBehaviorBuilder setFirstSide(boolean side) {
        this.firstSide = side;
        return this;
    }

    public SparkParticleBehaviorBuilder enableSecondSide() {
        return setSecondSide(true);
    }

    public SparkParticleBehaviorBuilder disableSecondSide() {
        return setSecondSide(false);
    }

    public SparkParticleBehaviorBuilder setSecondSide(boolean side) {
        this.secondSide = side;
        return this;
    }

    public SparkParticleBehaviorBuilder setSide(boolean firstSide, boolean secondSide) {
        return setFirstSide(firstSide).setSecondSide(secondSide);
    }

    public SparkParticleBehaviorBuilder enableCamera() {
        return setCamera(true);
    }

    public SparkParticleBehaviorBuilder disableCamera() {
        return setCamera(false);
    }

    public SparkParticleBehaviorBuilder setCamera(boolean camera) {
        this.camera = camera;
        return this;
    }

    public SparkParticleBehaviorBuilder setCameraRotation(boolean xRotCam, boolean yRotCam) {
        this.xRotCam = xRotCam;
        this.yRotCam = yRotCam;
        return this;
    }

    public SparkParticleBehaviorBuilder setColorData(ColorParticleData colorData) {
        this.colorData = colorData;
        return this;
    }

    public SparkParticleBehaviorBuilder setTransparencyData(GenericParticleData transparencyData) {
        this.transparencyData = transparencyData;
        return this;
    }

    public SparkParticleBehaviorBuilder setScaleData(GenericParticleData transparencyData) {
        this.scaleData = transparencyData;
        return this;
    }

    public SparkParticleBehaviorBuilder enableSecondColor() {
        return setSecondColor(true);
    }

    public SparkParticleBehaviorBuilder disableSecondColor() {
        return setSecondColor(false);
    }

    public SparkParticleBehaviorBuilder setSecondColor(boolean secondColor) {
        this.secondColor = secondColor;
        return this;
    }

    public SparkParticleBehaviorBuilder setStartPosition(Vec3 position) {
        this.startPos = position;
        return this;
    }

    public SparkParticleBehaviorBuilder setEndPosition(Vec3 position) {
        this.endPos = position;
        return this;
    }

    public ParticleBehavior build() {
        return new SparkParticleBehavior(colorData, transparencyData, scaleData, secondColor, startPos, endPos, xSpinData, ySpinData, zSpinData, xOffset, yOffset, zOffset, firstSide, secondSide, camera, xRotCam, yRotCam);
    }
}
