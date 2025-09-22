/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package ru.blatfan.blatapi.common.multiblock.matcher;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import ru.blatfan.blatapi.BlatApi;

/**
 * Matches any block, including air, and does not display anything.
 */
public class AnyMatcher extends DisplayOnlyMatcher {

    public static final ResourceLocation TYPE = BlatApi.loc("any");

    protected AnyMatcher() {
        super(Blocks.AIR.defaultBlockState());
    }

    public static AnyMatcher fromJson(JsonObject json) {
        return Matchers.ANY;
    }

    @Override
    public ResourceLocation getType() {
        return TYPE;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
    }
}
