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

package com.gustavoschip.expanded.task.type;

import com.gustavoschip.expanded.task.ModTasks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record FactionLevelTaskUnlocker(ResourceLocation faction, int minLevel, Optional<Integer> maxLevel,
                                       int minLordRank, Optional<Integer> maxLordRank) implements TaskUnlocker {

    public static final MapCodec<FactionLevelTaskUnlocker> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("faction")
                            .forGetter(FactionLevelTaskUnlocker::faction),

                    Codec.INT.fieldOf("minLevel")
                            .forGetter(FactionLevelTaskUnlocker::minLevel),

                    Codec.INT.optionalFieldOf("maxLevel")
                            .forGetter(FactionLevelTaskUnlocker::maxLevel),

                    Codec.INT.fieldOf("minLordRank")
                            .forGetter(FactionLevelTaskUnlocker::minLordRank),

                    Codec.INT.optionalFieldOf("maxLordRank")
                            .forGetter(FactionLevelTaskUnlocker::maxLordRank)

            ).apply(instance, FactionLevelTaskUnlocker::new));

    @Override
    public Component getDescription() {
        return Component.literal("Requires faction: %s, level: %d%s, lord rank: %d%s".formatted(
                faction,
                minLevel,
                maxLevel.map(max -> "-" + max).orElse("+"),
                minLordRank,
                maxLordRank.map(max -> "-" + max).orElse("+")
        ));
    }

    @Override
    public boolean isUnlocked(IFactionPlayer<?> playerEntity) {
        if (!this.faction.equals(playerEntity.getFaction().getID())) {
            return false;
        }

        int level = playerEntity.getLevel();
        if (level < minLevel) return false;

        if (maxLevel.isPresent() && level > maxLevel.get()) {
            return false;
        }

        int lordRank = FactionPlayerHandler.get(playerEntity.asEntity()).getLordLevel();

        if (lordRank < minLordRank) {
            return false;
        }

        return maxLordRank.isEmpty() || lordRank <= maxLordRank.get();
    }

    @Override
    public MapCodec<? extends TaskUnlocker> codec() {
        return ModTasks.FACTION_LEVEL_UNLOCKER.get();
    }
}