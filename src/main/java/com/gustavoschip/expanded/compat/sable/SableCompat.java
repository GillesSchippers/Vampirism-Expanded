/*
 * MIT License
 *
 * Copyright (c) 2026 Gilles Schippers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gustavoschip.expanded.compat.sable;

import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3dc;
import dev.ryanhcode.sable.sublevel.SubLevel;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Sable compatibility helpers that detect whether an entity is sheltered inside a sub-
 * level and translate the eye position into the embedded world.
 */

public class SableCompat {

    private static final double EYE_OFFSET = 32.0D;

    private static final long CACHE_REFRESH_INTERVAL = 40L;

    private static final double POSITION_REFRESH_DISTANCE_SQ = 0.25D;

    private static final Map<LivingEntity, ShelterCache> SHELTER_CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    private static boolean isInSubLevel(@NotNull LivingEntity entity, @NotNull Level level) {
        Vec3 eyePos = entity.getEyePosition();
        long gameTime = level.getGameTime();

        ShelterCache cache = SHELTER_CACHE.get(entity);
        if (cache != null && cache.canReuse(level, eyePos, gameTime)) {
            return cache.sheltered;
        }

        boolean sheltered = computeInSubLevel(level, eyePos);
        if (cache == null) {
            cache = new ShelterCache();
            SHELTER_CACHE.put(entity, cache);
        }
        cache.update(level, eyePos, gameTime, sheltered);
        return sheltered;
    }

    private static boolean computeInSubLevel(@NotNull Level level, @NotNull Vec3 eyePos) {
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return false;
        }

        // Create a search box to limit which sublevels we need to check
        // This dramatically reduces iterations for worlds with many sublevels
        double minX = eyePos.x - EYE_OFFSET;
        double maxX = eyePos.x + EYE_OFFSET;
        double minZ = eyePos.z - EYE_OFFSET;
        double maxZ = eyePos.z + EYE_OFFSET;

        // Iterate sub-levels and return true on first match (early exit optimization)
        for (SubLevel subLevel : container.getAllSubLevels()) {
            BoundingBox3dc bounds = subLevel.boundingBox();

            // Spatial filter: skip sublevels whose bounds don't overlap with our search area
            if (!isInSearchBounds(bounds, minX, maxX, minZ, maxZ)) {
                continue;
            }

            if (isUnderBounds(bounds, eyePos)) {
                return true; // Early exit - found shelter
            }
        }

        return false;
    }

    private static boolean isInSearchBounds(@NotNull BoundingBox3dc bounds, double minXSearch, double maxXSearch, double minZSearch, double maxZSearch) {
        return bounds.maxX() >= minXSearch && bounds.minX() <= maxXSearch && bounds.maxZ() >= minZSearch && bounds.minZ() <= maxZSearch;
    }

    private static boolean isUnderBounds(@NotNull BoundingBox3dc bounds, @NotNull Vec3 pos) {
        return pos.x >= bounds.minX() && pos.x <= bounds.maxX() && pos.z >= bounds.minZ() && pos.z <= bounds.maxZ() && pos.y <= bounds.maxY();
    }

    public static final class SableSubLevelHelper {

        private SableSubLevelHelper() {}

        public static boolean isInSubLevel(@NotNull LivingEntity entity, @NotNull Level level) {
            return SableCompat.isInSubLevel(entity, level);
        }
    }

    private static final class ShelterCache {

        private long lastSampleTick = Long.MIN_VALUE;
        private Object dimension;
        private double eyeX;
        private double eyeY;
        private double eyeZ;
        private boolean sheltered;

        private boolean canReuse(@NotNull Level level, @NotNull Vec3 eyePos, long gameTime) {
            if (lastSampleTick == Long.MIN_VALUE) {
                return false;
            }

            if (gameTime - lastSampleTick >= CACHE_REFRESH_INTERVAL) {
                return false;
            }

            if (dimension != level.dimension()) {
                return false;
            }

            double deltaX = eyePos.x - eyeX;
            double deltaY = eyePos.y - eyeY;
            double deltaZ = eyePos.z - eyeZ;
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ <= POSITION_REFRESH_DISTANCE_SQ;
        }

        private void update(@NotNull Level level, @NotNull Vec3 eyePos, long gameTime, boolean sheltered) {
            this.dimension = level.dimension();
            this.eyeX = eyePos.x;
            this.eyeY = eyePos.y;
            this.eyeZ = eyePos.z;
            this.lastSampleTick = gameTime;
            this.sheltered = sheltered;
        }
    }
}
