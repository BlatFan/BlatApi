package ru.blatfan.blatapi.utils;

import java.util.ArrayList;

public class MathUtils {

    public static float PI = 3.14159265358979323846f;

    public static float PI_HALF = 3.14159265358979323846f/2f;

    public static float PI2 = PI*2;

    public static float abs(float value){ return Math.abs(value);}

    public static float rad(float angle) {
        return angle * PI / 180;
    }

    public static float deg(float angle) {
        return angle * 180/PI;
    }

    public static float sqrt(float value){
        return (float)Math.sqrt(value);
    }

    public static float cos(float value){
        return (float)Math.cos(value);
    }

    public static float sin(float value){
        return (float)Math.sin(value);
    }

    public static float acos(float value){
        return (float)Math.acos(value);
    }

    public static float atan2(float y,float x){
        return (float)Math.atan2(y,x);
    }

    public static float asin(float a){
        return (float)Math.asin(a);
    }

    public static float lerp(float a, float b, float t){
        return (1 - t) * a + t * b;
    }


    public static float lerpRot(float a, float b, float t) {
        float wrap = MathUtils.rad(wrapDegrees(MathUtils.deg(b)-MathUtils.deg(a)));
        return a +t*wrap;
    }

    public static float floatSafe(float f){
        if (Float.isNaN(f)){
            return 0;
        }
        return f;
    }

    public static float wrapDegrees(float angle) {
        float f = angle % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }
        if (f < -180.0F) {
            f += 360.0F;
        }
        return f;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean equalsApprox(float a, float b){
        float diff = Math.abs(b - a);
        float tolerance = 0.1f/100 * b;
        return diff < tolerance;
    }

    public static float catmullrom(float previous, float current, float next, float nextNext,float f) {
        return 0.5F * (2.0F * current + (next - previous) * f + (2.0F * previous - 5.0F * current + 4.0F * next - nextNext) * f * f + (3.0F * current - previous - 3.0F * next + nextNext) * f * f * f);
    }

    public static float progressOfTime(float ticks,float value){
        return ((float) (ticks % value)) / value;
    }

    public static float arrange(float oldMin, float oldMax, float newMin, float newMax, float value){
        float oldRange = (oldMax - oldMin);
        float newValue;
        if (oldRange == 0) {
            newValue = newMin;
        }
        else {
            float newRange = (newMax - newMin);
            newValue = (((value - oldMin) * newRange) / oldRange) + newMin;
        }
        return newValue;
    }
}

