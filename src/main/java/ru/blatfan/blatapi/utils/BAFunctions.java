package ru.blatfan.blatapi.utils;

import java.util.function.Function;

public class BAFunctions {
    public static Function<Float, Float> FULL_WIDTH_FUNCTION = (f) -> 1f;
    public static Function<Float, Float> LINEAR_IN_WIDTH_FUNCTION = (f) -> f;
    public static Function<Float, Float> LINEAR_OUT_WIDTH_FUNCTION = (f) -> 1f - f;
    public static Function<Float, Float> LINEAR_IN_ROUND_WIDTH_FUNCTION = (f) -> f == 1 ? 0 : f;
    public static Function<Float, Float> LINEAR_OUT_ROUND_WIDTH_FUNCTION = (f) -> f == 0 ? 0 : 1f - f;
    public static Function<Float, Float> LINEAR_IN_SEMI_ROUND_WIDTH_FUNCTION = (f) -> f == 1 ? 0.5f : f;
    public static Function<Float, Float> LINEAR_OUT_SEMI_ROUND_WIDTH_FUNCTION = (f) -> f == 0 ? 0.5f : 1f - f;
}
