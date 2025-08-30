/*
 * Silent Lib -- InitialSpawnItems
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.blatfan.blatapi.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.BlatApi;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author SilentChaos512
 * from Silent Lib
 */
@ParametersAreNonnullByDefault
public final class InitialSpawnItems {
    public static final InitialSpawnItems INSTANCE = new InitialSpawnItems();
    private static final String NBT_KEY = BlatApi.MOD_ID + ".spawn_item";

    private final Map<ResourceLocation, Function<Player, Collection<ItemStack>>> spawnItems = new HashMap<>();
    
    private InitialSpawnItems() {}
    
    public static void add(ResourceLocation key, Function<Player, Collection<ItemStack>> itemFactory) {
        INSTANCE.spawnItems.put(key, itemFactory);
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag givenItems = PlayerUtil.getPersistedDataSubcompound(player, NBT_KEY);

        spawnItems.forEach((key, factory) -> handleSpawnItems(player, givenItems, key, factory.apply(player)));
    }

    private static void handleSpawnItems(Player player, CompoundTag givenItems, ResourceLocation key, Collection<ItemStack> items) {
        if (items.isEmpty()) return;

        String nbtKey = key.toString().replace(':', '.');
        if (!givenItems.getBoolean(nbtKey)) {
            items.forEach(stack -> {
                BlatApi.LOGGER.debug("Giving player {} spawn item \"{}\": {}", player.getScoreboardName(), nbtKey, stack);
                PlayerUtil.addItem(player, stack);
                givenItems.putBoolean(nbtKey, true);
            });
        }
    }
}
