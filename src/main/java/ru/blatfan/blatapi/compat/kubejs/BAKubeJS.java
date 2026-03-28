package ru.blatfan.blatapi.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.MinecraftForge;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStage;
import ru.blatfan.blatapi.common.player_stages.blocked.BlockedStageType;
import ru.blatfan.blatapi.common.reward.AttributeReward;
import ru.blatfan.blatapi.common.reward.CloseMenuReward;
import ru.blatfan.blatapi.common.reward.CommandReward;
import ru.blatfan.blatapi.common.reward.Reward;
import ru.blatfan.blatapi.common.task.*;
import ru.blatfan.blatapi.common.task.stage.*;
import ru.blatfan.blatapi.compat.kubejs.blocked_stages.BlockedStagesEventJS;
import ru.blatfan.blatapi.compat.kubejs.player_stages.PlayerStageEventJS;
import ru.blatfan.blatapi.compat.kubejs.player_stages.PlayerStagesKubeIntegration;
import ru.blatfan.blatapi.compat.kubejs.util.TaskBuilder;
import ru.blatfan.blatapi.utils.RecipeHelper;

import java.util.Map;

public class BAKubeJS extends KubeJSPlugin {
    public static final EventGroup STAGE_EVENTS = EventGroup.of("PlayerStageEvents");
    public static final EventHandler CREATE = STAGE_EVENTS.server("on_create", () -> PlayerStageEventJS.class).hasResult();
    public static final EventHandler ADD = STAGE_EVENTS.server("on_add", () -> PlayerStageEventJS.class).hasResult();
    public static final EventHandler SET = STAGE_EVENTS.server("on_set", () -> PlayerStageEventJS.class).hasResult();
    public static final EventHandler REMOVE = STAGE_EVENTS.server("on_remove", () -> PlayerStageEventJS.class).hasResult();
    
    public static final EventGroup BLOCKED_STAGE_EVENTS = EventGroup.of("BlockedStageEvents");
    public static final EventHandler REGISTER = BLOCKED_STAGE_EVENTS.server("register", () -> BlockedStagesEventJS.class);
    
    @Override
    public void injectRuntimeRecipes(RecipesEventJS event, RecipeManager manager, Map<ResourceLocation, Recipe<?>> recipesByName) {
        RecipeHelper.fireRecipeManagerLoadedEventKubeJSEdition(manager, recipesByName);
    }
    
    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(PlayerStagesKubeIntegration.class);
    }
    
    public static void postBSRegister() {
        if(REGISTER.hasListeners())
            REGISTER.post(new BlockedStagesEventJS());
    }
    
    @Override
    public void registerEvents() {
        STAGE_EVENTS.register();
        BLOCKED_STAGE_EVENTS.register();
    }
    
    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("PlayerStage", PlayerStages.class);
        event.add("TaskBuilder", TaskBuilder.class);
    }
    
    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        if(!type.isStartup()){
            filter.allow(Task.class);
            filter.allow(HasItemTask.class);
            filter.allow(ItemTask.class);
            filter.allow(PositionTask.class);
            filter.allow(BookEntryTask.class);
            filter.allow(ViewBookEntryTask.class);
            filter.allow(AdvancementTask.class);
            filter.allow(NotTask.class);
            filter.allow(CraftTask.class);
            filter.allow(KillTask.class);
            filter.allow(EatTask.class);
            filter.allow(StageBoolTask.class);
            filter.allow(StageDoubleTask.class);
            filter.allow(StageFloatTask.class);
            filter.allow(StageIntTask.class);
            filter.allow(StageItemStackTask.class);
            filter.allow(StageLongTask.class);
            filter.allow(StageStringTask.class);
            
            filter.allow(Reward.class);
            filter.allow(AttributeReward.class);
            filter.allow(CommandReward.class);
            filter.allow(CloseMenuReward.class);
            
            filter.allow(BlockedStage.class);
            filter.allow(BlockedStageType.class);
        }
    }
}