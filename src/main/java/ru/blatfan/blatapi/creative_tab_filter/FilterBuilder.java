package ru.blatfan.blatapi.creative_tab_filter;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 */

public class FilterBuilder {
    /**
     * The core of Filters API - the hashmap which stores all filters.
     */
    public static final HashMap<CreativeModeTab, FilterList> FILTERS = new HashMap<>();

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerFilter(CreativeModeTab, Component, Supplier)
     * @since 1.0.0+1.20.1
     */
    public static Filter registerFilter(ResourceKey<CreativeModeTab> creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        return FilterBuilder.registerFilter(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), filterName, filterIcon);
    }

    /**
     * Register a filter for specific creative mode tab.
     *
     * @param creativeModeTab specific creative mode tab
     * @param filterName      name of the filter
     * @param filterIcon      icon for filter
     * @return Filter instance
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static Filter registerFilter(CreativeModeTab creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.add(filter);
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(CreativeModeTab)
     * @since 1.0.0+1.20.1
     */
    public static Filter registerUncategorizedItemsFilter(ResourceKey<CreativeModeTab> creativeModeTab) {
        return FilterBuilder.registerUncategorizedItemsFilter(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab));
    }

    /**
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(CreativeModeTab, Component, Supplier)
     * @since 1.0.0
     */
    public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab) {
        Filter filter = new Filter(Component.translatable("filter.filters.uncategorized"), () -> new ItemStack(Blocks.BARRIER), new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(CreativeModeTab, Supplier)
     * @since 1.0.0+1.20.1
     */
    public static Filter registerUncategorizedItemsFilter(ResourceKey<CreativeModeTab> creativeModeTab, Supplier<ItemStack> filterIcon) {
        return FilterBuilder.registerUncategorizedItemsFilter(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), filterIcon);
    }

    /**
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(CreativeModeTab, Component, Supplier)
     * @since 1.0.0
     */
    public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(Component.translatable("filter.filters.uncategorized"), filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(CreativeModeTab, Component, Supplier)
     * @since 1.0.0+1.20.1
     */
    public static Filter registerUncategorizedItemsFilter(ResourceKey<CreativeModeTab> creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        return FilterBuilder.registerUncategorizedItemsFilter(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), filterName, filterIcon);
    }

    /**
     * Register a filter for uncategorized items in the specific creative mode tab.
     * "Uncategorized items filter" is for that not of current mod. Category the block/item when registering block/item is recommended.
     *
     * @param creativeModeTab specific creative mode tab
     * @param filterName      name of the filter
     * @param filterIcon      icon for filter
     * @return Filter instance
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #setReservedButton(CreativeModeTab, Component, Button.OnPress)
     * @since 1.0.0+1.20.1
     */
    public static void setReservedButton(ResourceKey<CreativeModeTab> creativeModeTab, Component tooltip, Button.OnPress onPress) {
        FilterBuilder.setReservedButton(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), tooltip, onPress, Filter.ICONS, 64, 0);
    }

    /**
     * @author ZiYueCommentary
     * @see #setReservedButton(CreativeModeTab, Component, Button.OnPress, ResourceLocation, int, int)
     * @since 1.0.0
     */
    public static void setReservedButton(CreativeModeTab creativeModeTab, Component tooltip, Button.OnPress onPress) {
        FilterBuilder.setReservedButton(creativeModeTab, tooltip, onPress, Filter.ICONS, 64, 0);
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #setReservedButton(CreativeModeTab, Component, Button.OnPress, ResourceLocation, int, int)
     * @since 1.0.0+1.20.1
     */
    public static void setReservedButton(ResourceKey<CreativeModeTab> creativeModeTab, Component tooltip, Button.OnPress onPress, ResourceLocation icon, int iconU, int iconV) {
        FilterBuilder.setReservedButton(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), tooltip, onPress, icon, iconU, iconV);
    }

    /**
     * Configure the third button on the left.
     *
     * @param creativeModeTab specific creative mode tab
     * @param tooltip         Component when hovering the button
     * @param onPress         function when clicking the button, set this as null to make the button invisible
     * @param icon            the icon of the button
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void setReservedButton(CreativeModeTab creativeModeTab, Component tooltip, Button.OnPress onPress, ResourceLocation icon, int iconU, int iconV) {
        FilterList filters = FilterBuilder.FILTERS.get(creativeModeTab);
        filters.btnReservedTooltip = tooltip;
        filters.btnReservedOnPress = onPress;
        filters.btnReservedIcon = icon;
        filters.btnReservedIconU = iconU;
        filters.btnReservedIconV = iconV;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #filtersVisibility(CreativeModeTab, boolean)
     * @since 1.0.0+1.20.1
     */
    public static void filtersVisibility(ResourceKey<CreativeModeTab> creativeModeTab, boolean visible) {
        FilterBuilder.filtersVisibility(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), visible);
    }

    /**
     * Disable or enable the filters of the tab.
     *
     * @param creativeModeTab the creative mode tab
     * @param visible         whether the filters are enabled
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void filtersVisibility(CreativeModeTab creativeModeTab, boolean visible) {
        if (FilterBuilder.FILTERS.containsKey(creativeModeTab))
            FilterBuilder.FILTERS.get(creativeModeTab).enabled = visible;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #isItemCategorized(CreativeModeTab, Item)
     * @since 1.0.0+1.20.1
     */
    public static boolean isItemCategorized(ResourceKey<CreativeModeTab> creativeModeTab, Item item) {
        return FilterBuilder.isItemCategorized(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab), item);
    }

    /**
     * Check whether the item is categorized in specific creative mode tab.
     *
     * @param creativeModeTab the creative mode tab
     * @param item            the item
     * @return a boolean value, true is categorized, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isItemCategorized(CreativeModeTab creativeModeTab, Item item) {
        for (Filter filter : FilterBuilder.FILTERS.get(creativeModeTab)) {
            if (filter.items.contains(item)) return true;
        }
        return false;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #isTabHasFilters(CreativeModeTab)
     * @since 1.0.0+1.20.1
     */
    public static boolean isTabHasFilters(ResourceKey<CreativeModeTab> creativeModeTab) {
        return FilterBuilder.isTabHasFilters(BuiltInRegistries.CREATIVE_MODE_TAB.get(creativeModeTab));
    }

    /**
     * Check whether the creative mode tab has filters or the filters are enabled.
     *
     * @param creativeModeTab the creative mode tab
     * @return a boolean value, true is available, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(CreativeModeTab creativeModeTab) {
        return (FilterBuilder.FILTERS.containsKey(creativeModeTab) && !FilterBuilder.FILTERS.get(creativeModeTab).isEmpty() && (FilterBuilder.FILTERS.get(creativeModeTab).enabled));
    }
}