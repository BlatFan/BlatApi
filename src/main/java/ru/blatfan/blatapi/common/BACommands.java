package ru.blatfan.blatapi.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.blatapi.client.render.MultiblockPreviewRenderer;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class BACommands {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        //PlayerStagesCommands.onRegisterCommands(event);
        MultiblockCommands.onRegisterCommands(event);
    }
    
    public static class MultiblockCommands {
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            
            dispatcher.register(Commands.literal("multiblock")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("reset")
                    .executes(MultiblockCommands::reset)
                ).then(Commands.literal("build")
                    .executes(MultiblockCommands::build)
                ).then(Commands.literal("list")
                    .executes(MultiblockCommands::list))
            );
        }
        
        private static int reset(CommandContext<CommandSourceStack> context) {
            MultiblockPreviewRenderer.setMultiblock(null, Component.empty(), false);
            return 1;
        }
        
        private static int list(CommandContext<CommandSourceStack> context) {
            List<String> list = new ArrayList<>();
            GuideManager.multiblocks().keySet().forEach(id -> list.add(id.toString()));
            
            context.getSource().sendSuccess(()->
                    Component.translatable("commands.blatapi.player_stages.available_multiblocks"),
                false
            );
            
            if (list.isEmpty())
                context.getSource().sendSuccess(()->Component.literal("  No multiblocks available"), false);
            else for (String m : list)
                context.getSource().sendSuccess(()->Component.literal("  " + m), false);
            return list.size();
        }
        
        private static int build(CommandContext<CommandSourceStack> context) {
            return MultiblockPreviewRenderer.buildMultiblock(context.getSource().getLevel());
        }
    }
    // TODO PlayerStagesCommands
    /*
    public static class PlayerStagesCommands {
        private static final DynamicCommandExceptionType INVALID_STAGE = new DynamicCommandExceptionType(
            stage -> Component.translatable("argument.blatapi.player_stages.invalid_stage", stage)
        );
        private static final SuggestionProvider<CommandSourceStack> STAGES =
            (context, builder) -> {
                String input = builder.getRemaining().toLowerCase();
                Collection<String> allStages = PlayerStages.allStages;
                for (String stage : allStages)
                    if (stage.toLowerCase().contains(input))
                        builder.suggest(stage);
                return builder.buildFuture();
            };
        
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            
            dispatcher.register(Commands.literal("player_stages")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("get")
                    .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.argument("stage", StringArgumentType.word()).suggests(STAGES)
                            .executes(PlayerStagesCommands::getStage)))
                )
                .then(Commands.literal("set")
                    .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.argument("stage", StringArgumentType.word()).suggests(STAGES)
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(PlayerStagesCommands::setStage))))
                )
                .then(Commands.literal("all")
                    .then(Commands.argument("player", EntityArgument.players())
                        .executes(PlayerStagesCommands::listAllStages)))
                .then(Commands.literal("list")
                    .executes(PlayerStagesCommands::listAvailableStages))
            );
        }
        
        private static int getStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "player");
            String stage = StringArgumentType.getString(context, "stage");
            
            if (!isValidStage(stage))
                throw INVALID_STAGE.create(stage);
            
            for (ServerPlayer player : players) {
                boolean hasStage = PlayerStages.get(player, stage);
                context.getSource().sendSuccess(()->
                        Component.translatable("commands.blatapi.player_stages.get",
                            player.getDisplayName(), stage, hasStage),
                    false
                );
            }
            
            return players.size();
        }
        
        private static int setStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "player");
            String stage = StringArgumentType.getString(context, "stage");
            boolean value = BoolArgumentType.getBool(context, "value");
            int count = 0;
            
            for (ServerPlayer player : players) {
                if (value) {
                    PlayerStages.add(player, stage);
                } else {
                    PlayerStages.remove(player, stage);
                }
                
                context.getSource().sendSuccess(()->
                        Component.translatable("commands.blatapi.player_stages.set",
                            player.getDisplayName(), stage, value),
                    false
                );
                count++;
            }
            
            return count;
        }
        
        private static int listAllStages(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "player");
            
            for (ServerPlayer player : players) {
                Map<String, Boolean> stages = PlayerStages.get(player).getAll();
                
                if (stages.isEmpty()) {
                    context.getSource().sendSuccess(()->
                            Component.translatable("commands.blatapi.player_stages.no_stages",
                                player.getDisplayName()),
                        false
                    );
                } else {
                    context.getSource().sendSuccess(()->
                            Component.translatable("commands.blatapi.player_stages.list_header",
                                player.getDisplayName(), stages.size()),
                        false
                    );
                    
                    for (Map.Entry<String, Boolean> entry : stages.entrySet()) {
                        context.getSource().sendSuccess(()->
                                Component.literal("  " + entry.getKey() + ": " + entry.getValue()),
                            false
                        );
                    }
                }
            }
            
            return players.size();
        }
        
        private static int listAvailableStages(CommandContext<CommandSourceStack> context) {
            Collection<String> allStages = PlayerStages.allStages;
            
            context.getSource().sendSuccess(()->
                    Component.translatable("commands.blatapi.player_stages.available_stages"),
                false
            );
            
            if (allStages.isEmpty()) {
                context.getSource().sendSuccess(()->Component.literal("  No stages available"), false);
            } else {
                for (String stage : allStages) {
                    context.getSource().sendSuccess(()->Component.literal("  " + stage), false);
                }
            }
            
            return allStages.size();
        }
        private static boolean isValidStage(String stage) {
            return PlayerStages.allStages.contains(stage);
        }
    }*/
}