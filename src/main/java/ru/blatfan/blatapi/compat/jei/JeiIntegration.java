package ru.blatfan.blatapi.compat.jei;

import net.minecraft.world.item.crafting.Recipe;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.recipe.IAnvilRepairRecipe;
import ru.blatfan.blatapi.compat.jei.category.AnvilRecipeCategory;
import ru.blatfan.blatapi.mixins.common.AccessorJEIRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.*;
import mezz.jei.library.plugins.vanilla.anvil.AnvilRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.utils.DisabledRecipes;
import ru.blatfan.blatapi.utils.RecipeHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@JeiPlugin
public final class JeiIntegration implements IModPlugin {
    public static final ResourceLocation UID = BlatApi.loc("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AnvilRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(AnvilRecipeCategory.RECIPE_TYPE, RecipeHelper.getRecipeManager().getAllRecipesFor(BARegistry.ANVIL.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.ANVIL), AnvilRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Blocks.CHIPPED_ANVIL), AnvilRecipeCategory.RECIPE_TYPE, RecipeTypes.ANVIL);
        registration.addRecipeCatalyst(new ItemStack(Blocks.DAMAGED_ANVIL), AnvilRecipeCategory.RECIPE_TYPE, RecipeTypes.ANVIL);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AnvilMenu.class, MenuType.ANVIL, AnvilRecipeCategory.RECIPE_TYPE, 0, 2, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AnvilScreen.class, 102, 48, 22, 15, AnvilRecipeCategory.RECIPE_TYPE, RecipeTypes.ANVIL);
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {
        var recipesToAdd = new ArrayList<IJeiAnvilRecipe>();
        var ingredientsToAdd = new ArrayList<ItemStack>();

        ((AccessorJEIRecipeManager) registration.getRecipeManager()).anvilapi$getInternal().getRecipesStream(RecipeTypes.ANVIL, registration.getJeiHelpers().getFocusFactory().getEmptyFocusGroup(), false).forEach(recipe -> {
            if (recipe.getRightInputs().stream().anyMatch(rightInput -> DisabledRecipes.isRepairItemDisabled(rightInput) || recipe.getLeftInputs().stream().anyMatch(leftInput -> DisabledRecipes.isRepairDisabled(leftInput, rightInput) || (rightInput.getItem() instanceof EnchantedBookItem && EnchantmentHelper.getEnchantments(rightInput).entrySet().stream().anyMatch(entry -> DisabledRecipes.isEnchantmentDisabled(leftInput, entry.getKey(), entry.getValue())))))) {
                registration.getRecipeManager().hideRecipes(RecipeTypes.ANVIL, Collections.singletonList(recipe));

                var leftInputs = new ArrayList<>(recipe.getLeftInputs());
                var rightInputs = new ArrayList<>(recipe.getRightInputs());

                ingredientsToAdd.addAll(leftInputs.stream().filter(stack -> registration.getIngredientVisibility().isIngredientVisible(VanillaTypes.ITEM_STACK, stack)).map(stack -> new ItemStack(stack.getItem())).toList());
                ingredientsToAdd.addAll(rightInputs.stream().filter(stack -> registration.getIngredientVisibility().isIngredientVisible(VanillaTypes.ITEM_STACK, stack)).map(stack -> new ItemStack(stack.getItem())).toList());

                var outputs = new ArrayList<>(recipe.getOutputs());

                if (leftInputs.stream().allMatch(input -> input.is(leftInputs.get(0).getItem()))) {
                    var leftInput = leftInputs.get(0);

                    if (rightInputs.stream().anyMatch(input -> !(input.getItem() instanceof EnchantedBookItem))) {
                        rightInputs.removeIf(DisabledRecipes::isRepairItemDisabled);

                        if (DisabledRecipes.isRepairDisabled(leftInput, null))
                            return;

                        rightInputs.removeIf(input -> DisabledRecipes.isRepairDisabled(leftInput, input));
                    } else {
                        var done = 0;

                        for (var index = 0; index - done < rightInputs.size(); index++) {
                            if (EnchantmentHelper.getEnchantments(rightInputs.get(index - done)).entrySet().stream().anyMatch(enchantment -> DisabledRecipes.isEnchantmentDisabled(leftInput, enchantment.getKey(), enchantment.getValue()))) {
                                if (rightInputs.size() == outputs.size())
                                    outputs.remove(index - done);

                                rightInputs.remove(index - done);

                                done++;
                            }
                        }
                    }

                    if (!rightInputs.isEmpty())
                        recipesToAdd.add(new AnvilRecipe(leftInputs, rightInputs, outputs, recipe.getUid()));
                } else {
                    var recipes = new ArrayList<IJeiAnvilRecipe>();

                    if (rightInputs.stream().anyMatch(input -> !(input.getItem() instanceof EnchantedBookItem))) {
                        leftInputs.removeIf(input -> DisabledRecipes.isRepairDisabled(input, null));

                        leftInputs.removeIf(leftInput -> {
                            var filter = false;

                            var newRightInputs = new ArrayList<>(rightInputs);

                            for (var rightInput : rightInputs) {
                                if (DisabledRecipes.isRepairDisabled(leftInput, rightInput)) {
                                    newRightInputs.remove(rightInput);

                                    filter = true;
                                }
                            }

                            if (!filter)
                                return false;

                            recipes.add(new AnvilRecipe(Collections.singletonList(leftInput), newRightInputs, outputs, recipe.getUid()));

                            return true;
                        });
                    } else {
                        leftInputs.removeIf(leftInput -> {
                            var newRightInputs = new ArrayList<>(rightInputs);
                            var newOutputs = new ArrayList<>(outputs);

                            var filter = false;
                            var done = 0;

                            for (var index = 0; index - done < newRightInputs.size(); index++) {
                                if (EnchantmentHelper.getEnchantments(newRightInputs.get(index - done)).entrySet().stream().anyMatch(enchantment -> DisabledRecipes.isEnchantmentDisabled(leftInput, enchantment.getKey(), enchantment.getValue()))) {
                                    if (newRightInputs.size() == newOutputs.size())
                                        newOutputs.remove(index - done);

                                    newRightInputs.remove(index - done);

                                    done++;
                                    filter = true;
                                }
                            }

                            if (!filter)
                                return false;

                            recipes.add(new AnvilRecipe(Collections.singletonList(leftInput), newRightInputs, newOutputs, recipe.getUid()));

                            return true;
                        });
                    }

                    if (!recipes.isEmpty())
                        for(IJeiAnvilRecipe recipe1 : recipes)
                            recipesToAdd.add(new AnvilRecipe(recipe1.getLeftInputs(), recipe1.getRightInputs(), recipe1.getOutputs(), recipe1.getUid()));

                    if (!leftInputs.isEmpty())
                        recipesToAdd.add(new AnvilRecipe(leftInputs, rightInputs, outputs, recipe.getUid()));
                }
            }
        });

        var recipes = new ArrayList<IJeiAnvilRecipe>();

        recipes.addAll(recipesToAdd);
        
        List<IJeiAnvilRecipe> rec = new ArrayList<>();
        for(Recipe<?> recipe1 : RecipeHelper.getRecipes(BARegistry.ANVIL_REPAIR.get())){
            IAnvilRepairRecipe recipe = (IAnvilRepairRecipe) recipe1;
            rec.add(new AnvilRecipe(
                Stream.of(recipe.getBaseItem().getDefaultInstance()).peek(input -> input.setDamageValue(input.getMaxDamage())).toList(),
                Arrays.asList(recipe.getRepairItem().getItems()),
                Stream.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())).peek(input -> input.setDamageValue(input.getMaxDamage() * 3 / 4)).toList(),
                recipe.getId()));
        }
        recipes.addAll(rec);

        if (!recipes.isEmpty())
            registration.getRecipeManager().addRecipes(RecipeTypes.ANVIL, recipes.stream().distinct().toList());

        if (!ingredientsToAdd.isEmpty())
            registration.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ingredientsToAdd);
    }
}
