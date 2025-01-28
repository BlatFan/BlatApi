package ru.blatfan.blatapi.utils;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import ru.blatfan.blatapi.BlatApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class DisabledRecipes implements ResourceManagerReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FMLPaths.CONFIGDIR.get().toAbsolutePath();

    private static final List<Pair<Ingredient, Ingredient>> REPAIR = Lists.newArrayList();
    private static final List<Pair<Pair<Enchantment, Integer>, Ingredient>> ENCHANTMENTS = Lists.newArrayList();
    private static final List<Ingredient> REPAIR_ITEMS = Lists.newArrayList();

    private static final List<Pair<Ingredient, Ingredient>> REPAIR_INTERNAL = Lists.newArrayList();
    private static final List<Pair<Pair<Enchantment, Integer>, Ingredient>> ENCHANTMENTS_INTERNAL = Lists.newArrayList();
    private static final List<Ingredient> REPAIR_ITEMS_INTERNAL = Lists.newArrayList();

    public DisabledRecipes() {
        try {
            Files.createDirectory(Paths.get(PATH.toString(), "blatfan/"+BlatApi.MODID));
        } catch (FileAlreadyExistsException e) {
            BlatApi.LOGGER.debug("{} config directory already exists.", "blatfan/"+BlatApi.MODID);
        } catch (IOException e) {
            BlatApi.LOGGER.error("Failed to create {} config directory.", "blatfan/"+BlatApi.MODID);
        }
    }

    public static void disableRepairRecipe(Ingredient baseItem, Ingredient repairItem) {
        REPAIR_INTERNAL.add(Pair.of(baseItem, repairItem));
    }

    public static void disableEnchantmentRecipe(Ingredient baseItem, Enchantment enchantment) {
        disableEnchantmentRecipe(baseItem, enchantment, -1);
    }

    public static void disableEnchantmentRecipe(Ingredient baseItem, Enchantment enchantment, int enchantmentLevel) {
        ENCHANTMENTS_INTERNAL.add(Pair.of(Pair.of(enchantment, enchantmentLevel), baseItem));
    }

    public static void disableRepairItem(Ingredient repairItem) {
        REPAIR_ITEMS_INTERNAL.add(repairItem);
    }

    public static boolean isRepairDisabled(ItemStack baseItem, @Nullable ItemStack repairItem) {
        return REPAIR.stream().anyMatch(entry -> entry.getFirst().test(baseItem) && (entry.getSecond().isEmpty() || entry.getSecond().test(repairItem)));
    }

    public static boolean isEnchantmentDisabled(@Nullable ItemStack baseItem, Enchantment enchantment, int enchantmentLevel) {
        return ENCHANTMENTS.stream().anyMatch(entry -> entry.getFirst().getFirst() == enchantment && (entry.getFirst().getSecond() == -1 || entry.getFirst().getSecond() == enchantmentLevel) && (entry.getSecond().isEmpty() || entry.getSecond().test(baseItem)));
    }

    public static boolean isRepairItemDisabled(ItemStack repairItem) {
        return REPAIR_ITEMS.stream().anyMatch(item -> item.test(repairItem));
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        REPAIR.clear();
        ENCHANTMENTS.clear();
        REPAIR_ITEMS.clear();

        REPAIR.addAll(REPAIR_INTERNAL);
        ENCHANTMENTS.addAll(ENCHANTMENTS_INTERNAL);
        REPAIR_ITEMS.addAll(REPAIR_ITEMS_INTERNAL);

        try {
            var file = new File(Paths.get(PATH.toString(), "blatfan/"+BlatApi.MODID).toString(), "disabled_vanilla_recipes.json");

            if (!file.exists()) {
                file.createNewFile();

                var writer = new FileWriter(file);

                var template = new JsonObject();
                template.addProperty("enable", true);

                template.add("repair", new JsonArray());
                template.add("repairItems", new JsonArray());
                template.add("enchantments", new JsonArray());

                GSON.toJson(template, writer);
                writer.close();

                return;
            }

            var json = JsonParser.parseReader(new InputStreamReader(new FileInputStream(file))).getAsJsonObject();

            if (!GsonHelper.getAsBoolean(json, "enable", true))
                return;

            for (var repairJson : json.get("repair").getAsJsonArray()) {
                Ingredient baseItem;
                var repairItem = Ingredient.EMPTY;

                if (repairJson.isJsonObject()) {
                    var baseItemJson = repairJson.getAsJsonObject().get("baseItem");

                    if (baseItemJson.isJsonPrimitive())
                        baseItem = Ingredient.of(new ItemStack(GsonHelper.convertToItem(baseItemJson, "baseItem")));
                    else
                        baseItem = CraftingHelper.getIngredient(baseItemJson, false);

                    if (repairJson.getAsJsonObject().has("repairItem")) {
                        var repairItemJson = repairJson.getAsJsonObject().get("repairItem");

                        if (repairItemJson.isJsonPrimitive())
                            repairItem = Ingredient.of(new ItemStack(GsonHelper.convertToItem(repairItemJson, "repairItem")));
                        else
                            repairItem = CraftingHelper.getIngredient(repairJson.getAsJsonObject().get("repairItem"), true);
                    }
                } else
                    baseItem = Ingredient.of(new ItemStack(GsonHelper.convertToItem(repairJson, "baseItem")));

                REPAIR.add(Pair.of(baseItem, repairItem));
            }

            for (var enchantmentJson : json.get("enchantments").getAsJsonArray()) {
                var baseItem = Ingredient.EMPTY;
                Enchantment enchantment;
                var enchantmentLevel = -1;

                if (enchantmentJson.isJsonObject()) {
                    if (enchantmentJson.getAsJsonObject().has("baseItem")) {
                        var baseItemJson = enchantmentJson.getAsJsonObject().get("baseItem");

                        if (baseItemJson.isJsonPrimitive())
                            baseItem = Ingredient.of(new ItemStack(GsonHelper.convertToItem(baseItemJson, "baseItem")));
                        else
                            baseItem = CraftingHelper.getIngredient(baseItemJson, false);
                    }

                    enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(GsonHelper.getAsString(enchantmentJson.getAsJsonObject(), "enchantment")));
                    enchantmentLevel = GsonHelper.getAsInt(enchantmentJson.getAsJsonObject(), "level", -1);
                } else
                    enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantmentJson.getAsString()));

                ENCHANTMENTS.add(Pair.of(Pair.of(enchantment, enchantmentLevel), baseItem));
            }

            for (var repairItemJson : json.get("repairItems").getAsJsonArray()) {
                Ingredient repairItem;

                if (repairItemJson.isJsonPrimitive())
                    repairItem = Ingredient.of(new ItemStack(GsonHelper.convertToItem(repairItemJson, "repairItem")));
                else
                    repairItem = CraftingHelper.getIngredient(repairItemJson, false);

                REPAIR_ITEMS.add(repairItem);
            }
        } catch (IOException e) {
            BlatApi.LOGGER.error(e.getMessage());
        }
    }

    @SubscribeEvent
    public void onAddReloadListener(final AddReloadListenerEvent event) {
        event.addListener(this);
    }
}
