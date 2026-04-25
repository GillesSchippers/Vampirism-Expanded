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

package com.gustavoschip.expanded.task.reward;

import com.gustavoschip.expanded.task.ModTasks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SkillPointTaskReward(
    int points,
    ResourceLocation source,
    ResourceLocation faction
) implements TaskReward, ITaskRewardInstance {
    public static final MapCodec<SkillPointTaskReward> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance
            .group(
                Codec.INT.fieldOf("points").forGetter(SkillPointTaskReward::points),
                ResourceLocation.CODEC.fieldOf("source").forGetter(SkillPointTaskReward::source),
                ResourceLocation.CODEC.fieldOf("faction").forGetter(SkillPointTaskReward::faction)
            )
            .apply(instance, SkillPointTaskReward::new)
    );

    // TODO: Investigate duplicate points in singleplayer?
    @Override
    public void applyReward(IFactionPlayer<?> player) {
        ModTasks.TaskSkillPointStorage.addSkillPoints(player, this.faction, this.points);
    }

    // TODO: Is this the cause of the issue described above? Shared/duplicated instances?
    @Override
    public @NotNull ITaskRewardInstance createInstance(IFactionPlayer<?> player) {
        return this;
    }

    @Override
    public MapCodec<SkillPointTaskReward> codec() {
        return ModTasks.SKILL_POINT_REWARD.get();
    }

    @Override
    public Component description() {
        return Component.translatable("task_reward.expanded.skill_points", this.points);
    }
}
