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

package com.gustavoschip.expanded.compat.create;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import de.teamlapen.vampirism.config.VampirismConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Create compatibility helpers that detect whether an entity is sheltered by a contraption
 * when Vampirism checks sunlight exposure.
 */

public class CreateCompat {

    /**
     * Value that holds the offsets for handling eye positions.
     */

    private static final double EYE_OFFSET = 32.0D;

    /**
     * Returns whether the entity is sheltered by a Create contraption or its bounding box.
     */

    public static boolean isInContraption(@NotNull LivingEntity entity, @NotNull Level level) {
        Entity vehicle = entity.getVehicle();
        if (!(vehicle instanceof AbstractContraptionEntity contraptionEntity)) {
            return isUnderContraption(level, entity.getEyePosition());
        }

        Contraption contraption = contraptionEntity.getContraption();
        if (contraption == null) {
            return isUnderContraption(level, entity.getEyePosition());
        }

        Vec3 localEyePos = contraptionEntity.toLocalVector(entity.getEyePosition(), 0);
        if (isContraptionSunBlocked(contraption, localEyePos)) {
            return true;
        }

        return isUnderContraption(level, entity.getEyePosition());
    }

    /**
     * Checks nearby contraptions around the eye position.
     */

    private static boolean isUnderContraption(@NotNull Level level, @NotNull Vec3 eyePos) {
        AABB searchBox = new AABB(eyePos.x - EYE_OFFSET, level.getMinBuildHeight(), eyePos.z - EYE_OFFSET, eyePos.x + EYE_OFFSET, level.getMinBuildHeight() + level.getHeight(), eyePos.z + EYE_OFFSET);

        // TODO: Optimize to prevent checking every contraption for every player.
        for (AbstractContraptionEntity contraptionEntity : level.getEntitiesOfClass(AbstractContraptionEntity.class, searchBox)) {
            Contraption contraption = contraptionEntity.getContraption();
            if (contraption != null) {
                Vec3 localEyePos = contraptionEntity.toLocalVector(eyePos, 0);
                try {
                    if (isContraptionSunBlocked(contraption, localEyePos)) {
                        return true;
                    }
                } catch (Exception e) {
                    if (isUnderBoundingBox(contraptionEntity.getBoundingBox(), eyePos)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns whether the eye position lies inside the contraption's bounding box.
     */

    private static boolean isUnderBoundingBox(@NotNull AABB box, @NotNull Vec3 eyePos) {
        return eyePos.x >= box.minX && eyePos.x <= box.maxX && eyePos.z >= box.minZ && eyePos.z <= box.maxZ && eyePos.y <= box.maxY;
    }

    /**
     * Returns whether the contraption world blocks direct sunlight at the local position.
     */

    private static boolean isContraptionSunBlocked(@NotNull Contraption contraption, @NotNull Vec3 pos) {
        LevelAccessor world = contraption.getContraptionWorld();
        if (world == null) {
            return false;
        }

        return !canBlockSeeSun(world, pos);
    }

    /**
     * Determines whether sunlight reaches the supplied position inside a contraption world.
     */

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
            } else if (state.canOcclude()) {
                return false;
            }
        }

        return true;
    }

    public static final class CreateContraptionHelper {

        private CreateContraptionHelper() {}

        public static boolean isInContraption(@NotNull LivingEntity entity, @NotNull Level level) {
            return CreateCompat.isInContraption(entity, level);
        }
    }
}
