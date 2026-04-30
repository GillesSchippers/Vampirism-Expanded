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

import de.teamlapen.vampirism.config.VampirismConfig;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SableCompat {

    public static boolean isInSubLevel(@NotNull LivingEntity entity, @NotNull Level level) {
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return false;
        }

        Vec3 eyePos = entity.getEyePosition();
        for (SubLevel subLevel : container.getAllSubLevels()) {
            Object bounds = subLevel.boundingBox();
            if (contains(bounds, eyePos)) {
                Vec3 localEyePos = transformPositionInverse(subLevel, eyePos);
                if (localEyePos != null && !canBlockSeeSun(subLevel.getPlot().getEmbeddedLevelAccessor(), localEyePos)) {
                    return true;
                }
            }

            if (isUnderBounds(bounds, eyePos)) {
                return true;
            }
        }

        return false;
    }

    private static boolean contains(@NotNull Object bounds, @NotNull Vec3 pos) {
        return pos.x >= minX(bounds) && pos.x < maxX(bounds) && pos.y >= minY(bounds) && pos.y < maxY(bounds) && pos.z >= minZ(bounds) && pos.z < maxZ(bounds);
    }

    private static boolean isUnderBounds(@NotNull Object bounds, @NotNull Vec3 pos) {
        return pos.x >= minX(bounds) && pos.x <= maxX(bounds) && pos.z >= minZ(bounds) && pos.z <= maxZ(bounds) && pos.y <= maxY(bounds);
    }

    private static double minX(@NotNull Object bounds) {
        return coordinate(bounds, "minX");
    }

    private static double maxX(@NotNull Object bounds) {
        return coordinate(bounds, "maxX");
    }

    private static double minY(@NotNull Object bounds) {
        return coordinate(bounds, "minY");
    }

    private static double maxY(@NotNull Object bounds) {
        return coordinate(bounds, "maxY");
    }

    private static double minZ(@NotNull Object bounds) {
        return coordinate(bounds, "minZ");
    }

    private static double maxZ(@NotNull Object bounds) {
        return coordinate(bounds, "maxZ");
    }

    private static double coordinate(@NotNull Object bounds, @NotNull String method) {
        try {
            return ((Number) bounds.getClass().getMethod(method).invoke(bounds)).doubleValue();
        } catch (ReflectiveOperationException e) {
            return Double.NaN;
        }
    }

    private static @Nullable Vec3 transformPositionInverse(@NotNull SubLevel subLevel, @NotNull Vec3 eyePos) {
        try {
            Object pose = subLevel.logicalPose();
            java.lang.reflect.Method transform = pose.getClass().getMethod("transformPositionInverse", Vec3.class);
            return (Vec3) transform.invoke(pose, eyePos);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static boolean canBlockSeeSun(@NotNull LevelAccessor world, @NotNull Vec3 pos) {
        int y = (int) Math.floor(pos.y);
        int maxY = world.getMinBuildHeight() + world.getHeight();
        int liquidBlocks = 0;
        BlockPos basePos = new BlockPos((int) Math.floor(pos.x), y, (int) Math.floor(pos.z));

        for (int currentY = y + 1; currentY < maxY; currentY++) {
            BlockPos checkPos = new BlockPos(basePos.getX(), currentY, basePos.getZ());
            BlockState state = world.getBlockState(checkPos);
            if (!state.getFluidState().isEmpty()) {
                liquidBlocks++;
                if (liquidBlocks >= VampirismConfig.BALANCE.vpSundamageWaterblocks.get()) {
                    return false;
                }
            } else if (state.canOcclude() && (state.isFaceSturdy(world, checkPos, net.minecraft.core.Direction.DOWN) || state.isFaceSturdy(world, checkPos, net.minecraft.core.Direction.UP))) {
                return false;
            } else if (state.getLightBlock(world, checkPos) > 0) {
                return false;
            }
        }

        return true;
    }

    public static final class SableSubLevelHelper {

        private SableSubLevelHelper() {}

        public static boolean isInSubLevel(@NotNull LivingEntity entity, @NotNull Level level) {
            return SableCompat.isInSubLevel(entity, level);
        }
    }
}
