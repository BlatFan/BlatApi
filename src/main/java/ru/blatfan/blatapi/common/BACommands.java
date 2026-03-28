package ru.blatfan.blatapi.common;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.blatapi.client.render.MultiblockPreviewRenderer;
import ru.blatfan.blatapi.common.multiblock.Multiblock;
import ru.blatfan.blatapi.common.player_stages.PlayerStages;
import ru.blatfan.blatapi.utils.collection.Text;
import ru.blatfan.blatapi.utils.command.CommandBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber
public class BACommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        PlayerStagesCommands.onRegisterCommands(event);
        MultiblockCommands.onRegisterCommands(event);
    }
    
    private static class MultiblockCommands {
        public static final CommandBuilder.BASuggestionProvider<CommandSourceStack> MULTIBLOCK_SUGGESTION = (context, builder) -> {
            GuideManager.multiblocks().keySet().forEach(rl -> builder.suggest(rl.toString()));
            return builder;
        };
        
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandBuilder.literal("ba_multiblock")
                .requiresOp(CommandBuilder.OpLevels.ADMIN)
                .then(CommandBuilder.literal("reset").executes(MultiblockCommands::reset))
                .then(CommandBuilder.literal("build").executes(MultiblockCommands::build))
                .then(CommandBuilder.literal("show")
                    .then(CommandBuilder.resourceLocation("multiblock_id")
                        .suggests(MULTIBLOCK_SUGGESTION)
                        .executes(MultiblockCommands::show)
                    )
                )
                .then(CommandBuilder.literal("list").executes(MultiblockCommands::list))
                .register(event.getDispatcher());
        }
        
        private static int reset(CommandContext<CommandSourceStack> context) {
            MultiblockPreviewRenderer.setMultiblock(null, Component.empty(), false);
            return 1;
        }
        
        private static int show(CommandContext<CommandSourceStack> context) {
            ResourceLocation id = ResourceLocationArgument.getId(context, "multiblock_id");
            Multiblock multiblock = GuideManager.getMultiblock(id);
            if(multiblock==null) return 0;
            MultiblockPreviewRenderer.setMultiblock(multiblock, Component.empty(), false);
            return 1;
        }
        
        private static int build(CommandContext<CommandSourceStack> context) {
            return MultiblockPreviewRenderer.buildMultiblock(context.getSource().getLevel());
        }
        
        private static int list(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            Set<ResourceLocation> ids = GuideManager.multiblocks().keySet();
            
            source.sendSuccess(() -> Text.create("commands.blatapi.player_stages.available_multiblocks"), false);
            
            if (ids.isEmpty())
                source.sendSuccess(() -> Component.literal("  No multiblocks available"), false);
            else for (var id : ids)
                source.sendSuccess(() -> Component.literal("  " + id.toString()), false);
            
            return ids.size();
        }
    }
    
    public static class PlayerStagesCommands {
        public static final CommandBuilder.BASuggestionProvider<CommandSourceStack> STAGES_SUGGESTION = (context, builder) -> {
            PlayerStages.allStages.forEach(rl -> builder.suggest(rl.toString()));
            return builder;
        };
        
        static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandBuildContext buildContext = event.getBuildContext();
            
            CommandBuilder.literal("player_stages")
                .requiresOp(Commands.LEVEL_GAMEMASTERS)
                .then(CommandBuilder.literal("list")
                    .executes(PlayerStagesCommands::listAvailableStages)
                )
                .then(CommandBuilder.literal("all")
                    .then(CommandBuilder.players("targets")
                        .executes(PlayerStagesCommands::listAllStages)
                    )
                )
                .then(CommandBuilder.literal("get")
                    .then(CommandBuilder.players("targets")
                        .then(CommandBuilder.word("stage").suggests(STAGES_SUGGESTION)
                            .executes(PlayerStagesCommands::getStage)
                        )
                    )
                )
                .then(CommandBuilder.literal("remove")
                    .then(CommandBuilder.players("targets")
                        .then(CommandBuilder.word("stage").suggests(STAGES_SUGGESTION)
                            .executes(PlayerStagesCommands::removeStage)
                        )
                    )
                )
                .then(CommandBuilder.literal("set")
                    .then(CommandBuilder.players("targets")
                        .then(CommandBuilder.resourceLocation("stage").suggests(STAGES_SUGGESTION)
                            .then(CommandBuilder.literal("int")
                                .then(CommandBuilder.integer("value")
                                    .executes(ctx -> executeSet(ctx, "int", (p, s) -> PlayerStages.setInt(p, s, ctx.getArgument("value", Integer.class))))))
                            .then(CommandBuilder.literal("long")
                                .then(CommandBuilder.longArg("value")
                                    .executes(ctx -> executeSet(ctx, "long", (p, s) -> PlayerStages.setLong(p, s, ctx.getArgument("value", Long.class))))))
                            .then(CommandBuilder.literal("double")
                                .then(CommandBuilder.doubleArg("value")
                                    .executes(ctx -> executeSet(ctx, "double", (p, s) -> PlayerStages.setDouble(p, s, ctx.getArgument("value", Double.class))))))
                            .then(CommandBuilder.literal("float")
                                .then(CommandBuilder.floatArg("value")
                                    .executes(ctx -> executeSet(ctx, "float", (p, s) -> PlayerStages.setFloat(p, s, ctx.getArgument("value", Float.class))))))
                            
                            .then(CommandBuilder.literal("bool")
                                .then(CommandBuilder.bool("value")
                                    .executes(ctx -> executeSet(ctx, "bool", (p, s) -> PlayerStages.setBool(p, s, ctx.getArgument("value", Boolean.class))))))
                            
                            .then(CommandBuilder.literal("string")
                                .then(CommandBuilder.greedyString("value")
                                    .executes(ctx -> executeSet(ctx, "string", (p, s) -> PlayerStages.setString(p, s, ctx.getArgument("value", String.class))))))
                            
                            .then(CommandBuilder.literal("item")
                                .then(CommandBuilder.item("value", buildContext)
                                    .executes(ctx -> executeSet(ctx, "item_stack", (p, s) -> {
                                        ItemStack stack = ctx.getArgument("value", ItemInput.class).createItemStack(1, false);
                                        PlayerStages.setItemStack(p, s, stack);
                                    }))))
                        )
                    )
                )
                .register(event.getDispatcher());
        }
        
        private static int listAvailableStages(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            Collection<ResourceLocation> allStages = PlayerStages.allStages;
            
            source.sendSuccess(() -> Text.create("commands.blatapi.player_stages.list_header", allStages.size()), false);
            
            if (allStages.isEmpty()) {
                source.sendSuccess(() -> Text.create("commands.blatapi.player_stages.list_empty"), false);
            } else {
                for (ResourceLocation stage : allStages) {
                    source.sendSuccess(() -> Component.literal("§7- §f" + stage.toString()), false);
                }
            }
            return allStages.size();
        }
        
        private static int listAllStages(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
            
            for (ServerPlayer player : players) {
                Map<ResourceLocation, PlayerStages.Value<?>> stages = PlayerStages.get(player).getAll();
                
                if (stages.isEmpty()) {
                    context.getSource().sendSuccess(() ->
                        Text.create("commands.blatapi.player_stages.player_empty", player.getScoreboardName()), false);
                } else {
                    context.getSource().sendSuccess(() ->
                        Text.create("commands.blatapi.player_stages.player_header", player.getScoreboardName()), false);
                    for (Map.Entry<ResourceLocation, PlayerStages.Value<?>> entry : stages.entrySet()) {
                        ResourceLocation key = entry.getKey();
                        String type = entry.getValue().getType().getPath();
                        Object value = entry.getValue().getValue();
                        context.getSource().sendSuccess(() ->
                            Text.create("commands.blatapi.player_stages.player_entry", key, type, value), false);
                    }
                }
            }
            return players.size();
        }
        
        
        private static int getStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
            ResourceLocation stage = ResourceLocationArgument.getId(context, "stage");
            
            for (ServerPlayer player : players) {
                if (PlayerStages.has(player, stage)) {
                    PlayerStages.Value<?> valueObj = PlayerStages.get(player).getAll().get(stage);
                    context.getSource().sendSuccess(() ->
                        Text.create("commands.blatapi.player_stages.get_success",
                            player.getScoreboardName(), stage, valueObj.getType().getPath(), valueObj.getValue().toString()), false);
                } else {
                    context.getSource().sendFailure(
                        Text.create("commands.blatapi.player_stages.get_fail", player.getScoreboardName(), stage));
                }
            }
            return players.size();
        }
        
        private static int removeStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
            ResourceLocation stage = ResourceLocationArgument.getId(context, "stage");
            
            for (ServerPlayer player : players) {
                PlayerStages.remove(player, stage);
                context.getSource().sendSuccess(() ->
                    Text.create("commands.blatapi.player_stages.remove", stage, player.getScoreboardName()), false);
            }
            return players.size();
        }
        
        private static int executeSet(CommandContext<CommandSourceStack> context, String typeName, StageSetter setter) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
            ResourceLocation stage = ResourceLocationArgument.getId(context, "stage");
            
            for (ServerPlayer player : players) {
                setter.apply(player, stage);
                context.getSource().sendSuccess(() ->
                    Component.literal(String.format("§aStage established '%s' §8[%s]§a for %s", stage, typeName, player.getScoreboardName())), false);
            }
            return players.size();
        }
        
        @FunctionalInterface
        private interface StageSetter {
            void apply(ServerPlayer player, ResourceLocation stage) throws CommandSyntaxException;
        }
    }
    
}