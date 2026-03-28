package ru.blatfan.blatapi.compat.ftb_quests.config;

import dev.ftb.mods.ftblibrary.config.NumberConfig;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FloatConfig extends NumberConfig<Float> {
    public FloatConfig(Float mn, Float mx) {
        super(mn, mx);
    }
    public FloatConfig() {
        super(Float.MIN_VALUE, Float.MAX_VALUE);
    }
    
    @Override
    public boolean parse(@Nullable Consumer<Float> consumer, String s) {
        if (!s.equals("-") && !s.equals("+") && !s.isEmpty()) {
            try {
                float v = Float.parseFloat(s);
                if (v >= this.min && v <= this.max)
                    return this.okValue(consumer, v);
            } catch (Exception ignored) {}
            return false;
        } else {
            return this.okValue(consumer, 0f);
        }
    }
}
