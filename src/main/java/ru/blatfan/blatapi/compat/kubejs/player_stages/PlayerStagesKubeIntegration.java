package ru.blatfan.blatapi.compat.kubejs.player_stages;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.common.player_stages.PlayerStageEvent;
import ru.blatfan.blatapi.compat.kubejs.BAKubeJS;

public class PlayerStagesKubeIntegration {
    @SubscribeEvent
    public static void onCreate(PlayerStageEvent.Create event) {
        if (BAKubeJS.CREATE.hasListeners())
            BAKubeJS.CREATE.post(new PlayerStageEventJS(event));
    }
    
    @SubscribeEvent
    public static void onAdd(PlayerStageEvent.Add<?> event) {
        if (BAKubeJS.ADD.hasListeners())
            BAKubeJS.ADD.post(new PlayerStageEventJS(event));
    }
    
    @SubscribeEvent
    public static void onSet(PlayerStageEvent.Set<?> event) {
        if (BAKubeJS.SET.hasListeners())
            BAKubeJS.SET.post(new PlayerStageEventJS(event));
    }
    
    @SubscribeEvent
    public static void onRemove(PlayerStageEvent.Remove event) {
        if (BAKubeJS.REMOVE.hasListeners())
            BAKubeJS.REMOVE.post(new PlayerStageEventJS(event));
    }
}