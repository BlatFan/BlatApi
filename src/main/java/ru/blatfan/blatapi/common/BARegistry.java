package ru.blatfan.blatapi.common;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.recipe.IAnvilRecipe;
import ru.blatfan.blatapi.common.recipe.IAnvilRepairRecipe;
import ru.blatfan.blatapi.common.recipe.AnvilRecipe;
import ru.blatfan.blatapi.common.recipe.AnvilRepairRecipe;
import ru.blatfan.blatapi.common.registry.BlatRegister;

public class BARegistry {
    public static final BlatRegister REG = new BlatRegister(BlatApi.MOD_ID);
    
    public static final RegistryObject<RecipeType<IAnvilRecipe>> ANVIL = REG.recipe_type("anvil");
    public static final RegistryObject<RecipeType<IAnvilRepairRecipe>> ANVIL_REPAIR = REG.recipe_type("anvil_repair");
    
    public static final RegistryObject<RecipeSerializer<IAnvilRecipe>> ANVIL_S = REG.recipe_serializer("anvil", AnvilRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<IAnvilRepairRecipe>> ANVIL_REPAIR_S = REG.recipe_serializer("anvil_repair", AnvilRepairRecipe.Serializer::new);
    
    public static void register(IEventBus event){
        REG.register(event);
    }
}