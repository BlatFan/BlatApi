package ru.blatfan.blatapi.compat.jei.category;

import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.player.Player;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.recipe.IAnvilRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import ru.blatfan.blatapi.utils.ItemHelper;

import java.util.Arrays;
import java.util.List;

public final class AnvilRecipeCategory implements IRecipeCategory<IAnvilRecipe> {
    private static final ResourceLocation TEXTURE = BlatApi.loc("textures/gui/jei/anvil.png");
    public static final RecipeType<IAnvilRecipe> RECIPE_TYPE = RecipeType.create(BlatApi.MOD_ID, "anvil", IAnvilRecipe.class);

    private final IDrawable background;
    private final IDrawable shapeless;
    private final IDrawable icon;

    public AnvilRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 152, 79);
        this.shapeless = helper.createDrawable(TEXTURE, 153, 0, 18, 16);

        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.ANVIL));
    }

    @Override
    public RecipeType<IAnvilRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.minecraft.anvil");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(IAnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Player player = Minecraft.getInstance().player;

        graphics.drawString(font, Component.literal(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()).getHoverName().getString()), 46, 17, 0xFFFFFFFF);

        if (recipe.isShapeless())
            this.shapeless.draw(graphics, 135, 62);

        if (!recipe.getReturns().stream().allMatch(ItemStack::isEmpty)) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 400);

            if (!recipe.getReturn(0).isEmpty()) {
                if (mouseX > 9 && mouseX < 26 && mouseY > 39 && mouseY < 56) {
                    ItemStack item = recipe.getReturn(0);

                    graphics.renderItem(item, (int) mouseX + 12, (int) mouseY + 30);
                    graphics.renderItemDecorations(font, item, (int) mouseX + 12, (int) mouseY + 30);
                }
            }

            if (!recipe.getReturn(1).isEmpty()) {
                if (mouseX > 58 && mouseX < 75 && mouseY > 39 && mouseY < 56) {
                    ItemStack item = recipe.getReturn(1);

                    graphics.renderItem(item, (int) mouseX + 12, (int) mouseY + 30);
                    graphics.renderItemDecorations(font, item, (int) mouseX + 12, (int) mouseY + 30);
                }
            }

            graphics.pose().popPose();
        }

        if (recipe.getExperience() > 0)
            graphics.drawString(font, Component.translatable("container.repair.cost", recipe.getExperience() < 0 ? "err" : String.valueOf(recipe.getExperience())).getString(), 9, 65, (player == null || player.isCreative()) || (recipe.getExperience() < 40 && recipe.getExperience() <= player.experienceLevel) ? 0xFF80FF20 : 0xFFFF6060);
    }

    @Override
    public List<Component> getTooltipStrings(IAnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (recipe.isShapeless())
            if (mouseX > 135 && mouseX < this.shapeless.getWidth() + 135 && mouseY > 62 && mouseY < this.shapeless.getHeight() + 62)
                return List.of(Component.translatable("tooltip.blatapi.shapeless"));

        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IAnvilRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(recipe.isConsuming(0) ? RecipeIngredientRole.INPUT : RecipeIngredientRole.CATALYST, 10, 40).addItemStacks(Arrays.stream(recipe.getInput(0).getItems()).map(stack -> ItemHelper.withSize(stack, recipe.getInputCount(0), false)).peek(stack -> stack.setTag(recipe.getInputNbt(0))).toList()).addTooltipCallback(((slotView, tooltip) -> {
            tooltip.add(Component.translatable("tooltip.blatapi.consumes").append(": ").withStyle(ChatFormatting.GRAY).append(recipe.isConsuming(0) ? Component.literal("Yes").withStyle(ChatFormatting.RED) : Component.literal("No").withStyle(ChatFormatting.GREEN)));

            if (recipe.isUsingDurability(0))
                tooltip.add(Component.translatable("tooltip.blatapi.uses_durability").append(": ").withStyle(ChatFormatting.GRAY).append(Component.translatable("tooltip.blatapi.yes").withStyle(ChatFormatting.RED)));

            if (recipe.hasNbt(0))
                tooltip.add(Component.translatable("tooltip.blatapi.strict_nbt").append(": ").withStyle(ChatFormatting.GRAY).append(recipe.isNbtStrict(0) ? Component.translatable("tooltip.blatapi.yes").withStyle(ChatFormatting.RED) : Component.translatable("tooltip.blatapi.no").withStyle(ChatFormatting.GREEN)));

            if (!recipe.getReturn(0).isEmpty()) {
                tooltip.add(Component.translatable("tooltip.blatapi.returns").append(": ").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.empty());
                tooltip.add(Component.empty());
            }
        }));

        builder.addSlot(recipe.isConsuming(1) ? RecipeIngredientRole.INPUT : RecipeIngredientRole.CATALYST, 59, 40).addItemStacks(Arrays.stream(recipe.getInput(1).getItems()).map(stack -> ItemHelper.withSize(stack, recipe.getInputCount(1), false)).peek(stack -> stack.setTag(recipe.getInputNbt(1))).toList()).addTooltipCallback(((slotView, tooltip) -> {
            tooltip.add(Component.translatable("tooltip.blatapi.consumes").append(": ").withStyle(ChatFormatting.GRAY).append(recipe.isConsuming(1) ? Component.literal("Yes").withStyle(ChatFormatting.RED) : Component.literal("No").withStyle(ChatFormatting.GREEN)));

            if (recipe.isUsingDurability(1))
                tooltip.add(Component.translatable("tooltip.blatapi.uses_durability").append(": ").withStyle(ChatFormatting.GRAY).append(Component.translatable("tooltip.blatapi.yes").withStyle(ChatFormatting.RED)));

            if (recipe.hasNbt(1))
                tooltip.add(Component.translatable("tooltip.blatapi.strict_nbt").append(": ").withStyle(ChatFormatting.GRAY).append(recipe.isNbtStrict(1) ? Component.translatable("tooltip.blatapi.yes").withStyle(ChatFormatting.RED) : Component.translatable("tooltip.blatapi.no").withStyle(ChatFormatting.GREEN)));

            if (!recipe.getReturn(1).isEmpty()) {
                tooltip.add(Component.translatable("tooltip.blatapi.returns").append(": ").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.empty());
                tooltip.add(Component.empty());
            }
        }));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 40).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));

        if (!recipe.getReturn(0).isEmpty())
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(recipe.getReturn(0));

        if (!recipe.getReturn(1).isEmpty())
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(recipe.getReturn(1));
    }
}
