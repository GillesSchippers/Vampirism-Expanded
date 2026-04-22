package com.gustavoschip.expanded.task.reward;

import com.gustavoschip.expanded.task.ModTasks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SkillPointTaskReward(int points, ResourceLocation source,
                                   ResourceLocation faction) implements TaskReward, ITaskRewardInstance {
    public static final MapCodec<SkillPointTaskReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("points").forGetter(SkillPointTaskReward::points),
            ResourceLocation.CODEC.fieldOf("source").forGetter(SkillPointTaskReward::source),
            ResourceLocation.CODEC.fieldOf("faction").forGetter(SkillPointTaskReward::faction)
    ).apply(instance, SkillPointTaskReward::new));

    @Override
    public void applyReward(IFactionPlayer<?> player) {
        player.getTaskManager().resetUniqueTask(ResourceKey.create(VampirismRegistries.Keys.TASK, this.source));
        ModTasks.TaskSkillPointStorage.addSkillPoints(player, this.faction, this.points);
    }

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

