package ru.blatfan.blatapi.common.player_stages;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Map;

@Mod.EventBusSubscriber
public class PlayerStagesCommands {
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
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(Commands.literal("player_stages")
            .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(Commands.literal("get")
                .then(Commands.argument("player", EntityArgument.players())
                    .then(Commands.argument("stage", StringArgumentType.string()).suggests(STAGES)
                        .executes(PlayerStagesCommands::getStage)))
            )
            .then(Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.players())
                    .then(Commands.argument("stage", StringArgumentType.string()).suggests(STAGES)
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
}