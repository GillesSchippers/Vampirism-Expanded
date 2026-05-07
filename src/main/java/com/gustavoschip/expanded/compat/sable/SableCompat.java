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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Sable compatibility helpers that detect whether an entity is sheltered inside a sub-
 * level and translate the eye position into the embedded world.
 */

public class SableCompat {

    /**
     * Returns whether the entity is sheltered by a Sable sub-level.
     * Sable uses virtual embedded worlds, so we only check bounding boxes
     * without querying blocks to avoid triggering chunk generation.
     */

    private static boolean isInSubLevel(@NotNull LivingEntity entity, @NotNull Level level) {
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return false;
        }

        Vec3 eyePos = entity.getEyePosition();
        for (SubLevel subLevel : container.getAllSubLevels()) {
            BoundingBox3dc bounds = subLevel.boundingBox();
            if (isUnderBounds(bounds, eyePos)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the position is under the supplied bounds.
     */

    private static boolean isUnderBounds(@NotNull BoundingBox3dc bounds, @NotNull Vec3 pos) {
        return pos.x >= bounds.minX() && pos.x <= bounds.maxX() && pos.z >= bounds.minZ() && pos.z <= bounds.maxZ() && pos.y <= bounds.maxY();
    }

    public static final class SableSubLevelHelper {

        private SableSubLevelHelper() {}

        public static boolean isInSubLevel(@NotNull LivingEntity entity, @NotNull Level level) {
            return SableCompat.isInSubLevel(entity, level);
        }
    }
}
