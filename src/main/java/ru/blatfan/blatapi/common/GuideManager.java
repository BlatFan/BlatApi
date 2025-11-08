package ru.blatfan.blatapi.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.MinecraftForge;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.api.multiblock.Multiblock;
import ru.blatfan.blatapi.client.guide_book.recipe_renderers.*;
import ru.blatfan.blatapi.common.guide_book.*;
import ru.blatfan.blatapi.common.guide_book.pages.*;
import ru.blatfan.blatapi.api.event.GuideReload;
import ru.blatfan.blatapi.common.multiblock.*;
import ru.blatfan.blatapi.common.recipe.AnvilRecipe;
import ru.blatfan.blatapi.common.recipe.IngredientWithCount;
import ru.blatfan.blatapi.utils.RecipeHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuideManager extends SimpleJsonResourceReloadListener {
    private static final Map<ResourceLocation, GuideBookData> books = new HashMap<>();
    private static final Map<ResourceLocation, GuideBookCategory> categories = new HashMap<>();
    private static final Map<ResourceLocation, GuideBookEntry> entries = new HashMap<>();
    private static final Map<ResourceLocation, Multiblock> multiblocks = new HashMap<>();
    
    private static final Map<ResourceLocation, BookGuiExtension> bookExtensions = new HashMap<>();
    
    public static Map<ResourceLocation, BookGuiExtension> getBookExtensions() {
        return Collections.unmodifiableMap(bookExtensions);
    }
    public static void addBookExtension(ResourceLocation id, BookGuiExtension extension){
        if(bookExtensions.containsKey(id)){
            BlatApi.LOGGER.error("Book Gui Extension id {} duplicate", id);
            return;
        }
        bookExtensions.put(id, extension);
    }
    
    public static void register(ResourceLocation id, GuideBookData bookData){
        GuideReload.BookReload event = new GuideReload.BookReload(bookData);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isCanceled()) return;
        books.put(id, event.getBookData());
    }
    public static void register(ResourceLocation id, GuideBookCategory category){
        GuideReload.CategoryReload event = new GuideReload.CategoryReload(category);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isCanceled()) return;
        categories.put(id, event.getCategory());
    }
    public static void register(ResourceLocation id, GuideBookEntry entry){
        GuideReload.EntryReload event = new GuideReload.EntryReload(entry);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isCanceled()) return;
        entries.put(id, event.getEntry());
    }
    public static void register(Multiblock multiblock){
        GuideReload.MultiblockReload event = new GuideReload.MultiblockReload(multiblock);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isCanceled()) return;
        multiblocks.put(event.getMultiblock().getId(), event.getMultiblock());
    }
    public static Map<ResourceLocation, GuideBookData> books(){
        return Collections.unmodifiableMap(books);
    }
    public static Map<ResourceLocation, GuideBookCategory> categories(){
        return Collections.unmodifiableMap(categories);
    }
    public static Map<ResourceLocation, GuideBookEntry> entries(){
        return Collections.unmodifiableMap(entries);
    }
    public static Map<ResourceLocation, Multiblock> multiblocks(){
        return Collections.unmodifiableMap(multiblocks);
    }
    public static ResourceLocation getId(GuideBookData bookData){
        for(Map.Entry<ResourceLocation, GuideBookData> entry : books.entrySet())
            if(entry.getValue()==bookData) return entry.getKey();
        return BlatApi.loc("mising");
    }
    public static ResourceLocation getId(Multiblock multiblock){
        for(Map.Entry<ResourceLocation, Multiblock> entry : multiblocks.entrySet())
            if(entry.getValue().getId().equals(multiblock.getId())) return entry.getKey();
        return BlatApi.loc("mising");
    }
    public static ResourceLocation getId(GuideBookCategory category){
        for(Map.Entry<ResourceLocation, GuideBookCategory> entry : categories().entrySet())
            if(entry.getValue()==category) return entry.getKey();
        return BlatApi.loc("mising");
    }
    public static ResourceLocation getId(GuideBookEntry entryS){
        for(Map.Entry<ResourceLocation, GuideBookEntry> entry : entries().entrySet())
            if(entry.getValue()==entryS) return entry.getKey();
        return BlatApi.loc("mising");
    }
    
    public static GuideBookData getBook(ResourceLocation id){
        return books.get(id);
    }
    public static GuideBookCategory getCategory(ResourceLocation id){
        return categories.get(id);
    }
    public static GuideBookEntry getEntry(ResourceLocation id){
        return entries.get(id);
    }
    public static Multiblock getMultiblock(ResourceLocation id){
        return multiblocks.get(id);
    }
    
    public static void init(){
        AbstractMultiblock.register(DenseMultiblock.TYPE, DenseMultiblock::fromJson);
        AbstractMultiblock.register(SparseMultiblock.TYPE, SparseMultiblock::fromJson);
        
        GuideBookPage.addPageType(RecipePage.TYPE, RecipePage::json);
        GuideBookPage.addPageType(TextPage.TYPE, TextPage::json);
        GuideBookPage.addPageType(EmptyPage.TYPE, EmptyPage::json);
        GuideBookPage.addPageType(ImagePage.TYPE, ImagePage::json);
        GuideBookPage.addPageType(EntityPage.TYPE, EntityPage::json);
        GuideBookPage.addPageType(MultiblockPage.TYPE, MultiblockPage::json);
        
        RecipePage.addRecipeType(get(RecipeType.CRAFTING), CraftingRecipeRenderer.INSTANCE);
        RecipePage.addRecipeType(AnvilRecipe.type, AnvilRecipeRenderer.INSTANCE);
        
        RecipePage.addRecipeType(get(RecipeType.SMELTING), FurnaceRecipeRenderer.INSTANCE);
        RecipePage.addRecipeType(get(RecipeType.CAMPFIRE_COOKING), FurnaceRecipeRenderer.INSTANCE);
        RecipePage.addRecipeType(get(RecipeType.BLASTING), FurnaceRecipeRenderer.INSTANCE);
        RecipePage.addRecipeType(get(RecipeType.SMOKING), FurnaceRecipeRenderer.INSTANCE);
        
        addBookExtension(BlatApi.loc("task"), new BookTaskExtension());
    }
    
    private static ResourceLocation get(RecipeType<?> rt){
        return ResourceLocation.tryParse(rt.toString());
    }
    
    private static final Gson GSON = new Gson();
    public GuideManager() {
        super(GSON, "guide_book");
    }
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        books.clear();
        categories.clear();
        entries.clear();
        for(Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()){
            JsonElement element = entry.getValue();
            ResourceLocation rl = entry.getKey();
            ResourceLocation id = null;
            if(contains(rl, "multiblocks/")) {
                id = remove(rl, "multiblocks/");
                Multiblock multiblock = AbstractMultiblock.fromJson(element.getAsJsonObject()).setId(id);
                register(multiblock);
            }
            if(contains(rl, "book/")) {
                id = remove(rl, "book/");
                register(id, GuideBookData.json(element));
            }
            else if(contains(rl, "categories/")) {
                id = remove(rl, "categories/");
                register(id, GuideBookCategory.json(element));
            }
            else if(contains(rl, "entries/")) {
                id = remove(rl, "entries/");
                register(id, GuideBookEntry.json(element));
            }
            if(id==null) BlatApi.LOGGER.warn("Noncorrect file {}", rl);
            else BlatApi.LOGGER.info("Loading {} from json {}", id, rl);
        }
    }
    
    private static boolean contains(ResourceLocation rl, String str){
        return rl.toString().contains(str);
    }
    private static ResourceLocation replace(ResourceLocation rl, String str1, String str2){
        return ResourceLocation.tryParse(rl.toString().replace(str1, str2));
    }
    private static ResourceLocation remove(ResourceLocation rl, String str1){
        return replace(rl, str1, "");
    }
}