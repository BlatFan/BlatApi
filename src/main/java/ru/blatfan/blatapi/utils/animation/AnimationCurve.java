package ru.blatfan.blatapi.utils.animation;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.blatfan.blatapi.utils.collection.Vector3;

public class AnimationCurve {
    @OnlyIn(Dist.CLIENT)
    public static float timeUnite(){
        return Minecraft.getInstance().getPartialTick();
    }
    
    public static Vector3 bezier(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3, double timeUnite) {
        double u = 1 - timeUnite;
        double uu = u * u;
        double uuu = uu * u;
        double tt = timeUnite * timeUnite;
        double ttt = tt * timeUnite;
        
        return p0.multiply(uuu)
            .add(p1.multiply(3 * uu * timeUnite))
            .add(p2.multiply(3 * u * tt))
            .add(p3.multiply(ttt));
    }
    
    public static Vector3 catmull(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3, double timeUnite) {
        double t2 = timeUnite * timeUnite;
        double t3 = t2 * timeUnite;
        
        return p1.multiply(2)
            .add(p2.subtract(p0)).multiply(timeUnite)
            .add(p0.multiply(2).subtract(p1.multiply(5))
                .add(p2.multiply(4)).subtract(p3).multiply(new Vector3(t2, t2, t2)))
            .add(p1.multiply(3).subtract(p0).subtract(p2.multiply(3)).add(p3).multiply(t3))
            .multiply(0.5);
    }
}