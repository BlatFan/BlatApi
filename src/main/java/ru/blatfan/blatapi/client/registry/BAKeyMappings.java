package ru.blatfan.blatapi.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import ru.blatfan.blatapi.BlatApi;

public class BAKeyMappings {
    public static final KeyMapping SKIN_MENU = new KeyMapping("key."+BlatApi.MOD_ID+".skin_menu",
        KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, BlatApi.MOD_NAME);
}
