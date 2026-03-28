package ru.blatfan.blatapi.utils.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.ItemArgument;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class CommandBuilder {
    private final ArgumentBuilder<CommandSourceStack, ?> builder;
    
    private CommandBuilder(ArgumentBuilder<CommandSourceStack, ?> builder) {
        this.builder = builder;
    }
    
    public static CommandBuilder literal(String name) {
        return new CommandBuilder(Commands.literal(name));
    }
    
    public static <T> CommandBuilder argument(String name, ArgumentType<T> type) {
        return new CommandBuilder(Commands.argument(name, type));
    }
    
    public static CommandBuilder word(String name) { return argument(name, StringArgumentType.word()); }
    public static CommandBuilder string(String name) { return argument(name, StringArgumentType.string()); }
    public static CommandBuilder greedyString(String name) { return argument(name, StringArgumentType.greedyString()); }
    
    public static CommandBuilder integer(String name) { return argument(name, IntegerArgumentType.integer()); }
    public static CommandBuilder integer(String name, int min, int max) { return argument(name, IntegerArgumentType.integer(min, max)); }
    
    public static CommandBuilder floatArg(String name) { return argument(name, FloatArgumentType.floatArg()); }
    public static CommandBuilder floatArg(String name, float min, float max) { return argument(name, FloatArgumentType.floatArg(min, max)); }
    
    public static CommandBuilder longArg(String name) { return argument(name, LongArgumentType.longArg()); }
    public static CommandBuilder longArg(String name, long min, long max) { return argument(name, LongArgumentType.longArg(min, max)); }
    
    public static CommandBuilder doubleArg(String name) { return argument(name, DoubleArgumentType.doubleArg()); }
    public static CommandBuilder doubleArg(String name, double min, double max) { return argument(name, DoubleArgumentType.doubleArg(min, max)); }
    
    public static CommandBuilder bool(String name) { return argument(name, BoolArgumentType.bool()); }
    
    public static CommandBuilder player(String name) { return argument(name, EntityArgument.player()); }
    public static CommandBuilder players(String name) { return argument(name, EntityArgument.players()); }
    public static CommandBuilder entity(String name) { return argument(name, EntityArgument.entity()); }
    public static CommandBuilder entities(String name) { return argument(name, EntityArgument.entities()); }
    
    public static CommandBuilder blockPos(String name) { return argument(name, BlockPosArgument.blockPos()); }
    public static CommandBuilder vec3(String name) { return argument(name, Vec3Argument.vec3()); }
    public static CommandBuilder vec2(String name) { return argument(name, Vec2Argument.vec2()); }
    
    public static CommandBuilder resourceLocation(String name) { return argument(name, ResourceLocationArgument.id()); }
    public static CommandBuilder component(String name) { return argument(name, ComponentArgument.textComponent()); }
    public static CommandBuilder compoundTag(String name) { return argument(name, CompoundTagArgument.compoundTag()); }
    public static CommandBuilder dimension(String name) { return argument(name, DimensionArgument.dimension()); }
    public static CommandBuilder uuid(String name) { return argument(name, UuidArgument.uuid()); }
    
    public static CommandBuilder item(String name, CommandBuildContext ctx) { return argument(name, ItemArgument.item(ctx)); }
    public static CommandBuilder block(String name, CommandBuildContext ctx) { return argument(name, BlockStateArgument.block(ctx)); }
    
    public CommandBuilder requires(Predicate<CommandSourceStack> requirement) {
        this.builder.requires(requirement);
        return this;
    }
    
    public CommandBuilder requiresOp(int level) {
        return requires(source -> source.hasPermission(level));
    }
    public CommandBuilder requiresOp(OpLevels level) {
        return requiresOp(level.level);
    }
    
    public CommandBuilder executes(Command<CommandSourceStack> command) {
        this.builder.executes(command);
        return this;
    }
    
    public CommandBuilder then(CommandBuilder child) {
        this.builder.then(child.build());
        return this;
    }
    
    public CommandBuilder suggests(BASuggestionProvider<CommandSourceStack> provider) {
        if (this.builder instanceof RequiredArgumentBuilder) {
            ((RequiredArgumentBuilder<CommandSourceStack, ?>) this.builder).suggests(provider);
        }
        return this;
    }
    
    public ArgumentBuilder<CommandSourceStack, ?> build() {
        return this.builder;
    }
    
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (this.builder instanceof LiteralArgumentBuilder) {
            dispatcher.register((LiteralArgumentBuilder<CommandSourceStack>) this.builder);
        } else {
            throw new IllegalStateException("Only literal commands can be registered in the dispatcher root!");
        }
    }
    
    @FunctionalInterface
    public interface BASuggestionProvider<T> extends SuggestionProvider<T> {
        SuggestionsBuilder getSuggestionsBuilder(CommandContext<T> context, SuggestionsBuilder builder) throws CommandSyntaxException;
        
        @Override
        default CompletableFuture<Suggestions> getSuggestions(CommandContext<T> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException{
            return getSuggestionsBuilder(commandContext, suggestionsBuilder).buildFuture();
        }
    }
    
    public enum OpLevels {
        PLAYER(Commands.LEVEL_ALL),
        MODERATOR(Commands.LEVEL_MODERATORS),
        GAMEMASTER(Commands.LEVEL_GAMEMASTERS),
        ADMIN(Commands.LEVEL_ADMINS),
        OWNER(Commands.LEVEL_OWNERS);
        
        private final int level;
        
        OpLevels(int level) {
            this.level = level;
        }
    }
}