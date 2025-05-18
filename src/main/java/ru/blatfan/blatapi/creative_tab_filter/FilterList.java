package ru.blatfan.blatapi.creative_tab_filter;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 */

public class FilterList extends ArrayList<Filter> {
    public FilterButton btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll, btnReserved;
    public int filterIndex = 0;
    public boolean enabled = true;

    public Filter uncategorizedItems = null;
    public Button.OnPress btnReservedOnPress = null;
    public Component btnReservedTooltip = null;
    public ResourceLocation btnReservedIcon = null;
    public int btnReservedIconU = 0, btnReservedIconV = 0;

    /**
     * Creating an empty filter.
     *
     * @return an empty filter
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static FilterList empty() {
        return new FilterList();
    }
}