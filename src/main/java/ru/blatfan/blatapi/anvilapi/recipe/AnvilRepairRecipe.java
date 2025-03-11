package ru.blatfan.blatapi.anvilapi.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.blatfan.blatapi.anvilapi.api.recipe.IAnvilRepairRecipe;
import lombok.Getter;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.utils.ItemHelper;

import java.util.List;
import java.util.function.Consumer;

public class AnvilRepairRecipe implements IAnvilRepairRecipe {
    private final ResourceLocation serializerName;

    private final List<ICondition> conditions = Lists.newArrayList();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    private final ResourceLocation id;
    private final Item baseItem;
    private final Ingredient repairItem;

    public AnvilRepairRecipe(ResourceLocation id, Item baseItem, Ingredient repairItem) {
        this.serializerName = new ResourceLocation("anvil_repair");

        this.id = id;
        this.baseItem = baseItem;
        this.repairItem = repairItem;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public Item getBaseItem() {
        return this.baseItem;
    }

    @Override
    public Ingredient getRepairItem() {
        return this.repairItem;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(this.baseItem);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        return this.getResultItem(access).copy();
    }

    @Override
    public boolean matches(Container container, Level level) {
        return container.getItem(0).is(this.baseItem) && this.repairItem.test(container.getItem(1));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeType<IAnvilRepairRecipe> getType() {
        return BARegistry.ANVIL_REPAIR.get();
    }

    @Override
    public RecipeSerializer<IAnvilRepairRecipe> getSerializer() {
        return BARegistry.ANVIL_REPAIR_S.get();
    }

    public AnvilRepairRecipe addCriterion(String name, CriterionTriggerInstance criterion) {
        this.advancementBuilder.addCriterion(name, criterion);

        return this;
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        if (this.advancementBuilder.getCriteria().isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + id);

        this.advancementBuilder.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", new RecipeUnlockedTrigger.TriggerInstance(ContextAwarePredicate.ANY, id))
                .rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(id));
    }

    public static class Serializer implements RecipeSerializer<IAnvilRepairRecipe> {
        @Override
        public IAnvilRepairRecipe fromJson(ResourceLocation id, JsonObject json) {
            var baseItem = GsonHelper.getAsItem(json, "baseItem");
            Ingredient repairItem;

            var repairItemJson = json.get("repairItem");

            if (repairItemJson.isJsonPrimitive())
                repairItem = Ingredient.of(GsonHelper.convertToItem(repairItemJson, "repairItem"));
            else
                repairItem = Ingredient.fromJson(json.get("repairItem"), false);

            return new AnvilRepairRecipe(id, baseItem, repairItem);
        }

        @Nullable
        @Override
        public IAnvilRepairRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            var baseItem = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
            var repairItem = Ingredient.fromNetwork(buffer);

            return new AnvilRepairRecipe(id, baseItem, repairItem);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, IAnvilRepairRecipe recipe) {
            buffer.writeResourceLocation(ForgeRegistries.ITEMS.getKey(recipe.getBaseItem()));
            recipe.getRepairItem().toNetwork(buffer);
        }
    }

    @Getter
    private class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ResourceLocation advancementId;

        Result(ResourceLocation id) {
            this.id = id;
            this.advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
        }

        @Override
        public JsonObject serializeRecipe() {
            var json = new JsonObject();

            json.addProperty("type", serializerName.toString());

            if (!conditions.isEmpty()) {
                var conditionsArray = new JsonArray();

                for (var condition : conditions)
                    conditionsArray.add(CraftingHelper.serialize(condition));

                json.add("conditions", conditionsArray);
            }

            this.serializeRecipeData(json);

            return json;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("baseItem", ItemHelper.serialize(baseItem));
            json.add("repairItem", repairItem.toJson());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancementBuilder.serializeToJson();
        }
    }
}
