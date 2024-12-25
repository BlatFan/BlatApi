package ru.blatfan.blatapi.init;

import ru.blatfan.blatapi.anvilapi.api.recipe.IAnvilRecipe;
import ru.blatfan.blatapi.anvilapi.api.recipe.IAnvilRepairRecipe;
import ru.blatfan.blatapi.anvilapi.recipe.AnvilRecipe;
import ru.blatfan.blatapi.anvilapi.recipe.AnvilRepairRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BARecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "minecraft");

    public static final RegistryObject<RecipeSerializer<IAnvilRecipe>> ANVIL = SERIALIZERS.register("anvil", AnvilRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<IAnvilRepairRecipe>> ANVIL_REPAIR = SERIALIZERS.register("anvil_repair", AnvilRepairRecipe.Serializer::new);
}
