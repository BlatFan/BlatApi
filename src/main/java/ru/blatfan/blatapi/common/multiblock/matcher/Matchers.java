/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package ru.blatfan.blatapi.common.multiblock.matcher;

import net.minecraft.world.level.block.Blocks;
import ru.blatfan.blatapi.BlatApi;

public class Matchers {
    public static final AnyMatcher ANY = new AnyMatcher();

    public static final PredicateMatcher AIR = new PredicateMatcher(Blocks.AIR.defaultBlockState(), BlatApi.loc("air"), false);

}
