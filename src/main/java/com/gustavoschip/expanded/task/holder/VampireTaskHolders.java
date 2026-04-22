package com.gustavoschip.expanded.task.holder;

import com.gustavoschip.expanded.task.ModTasks;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import net.minecraft.resources.ResourceKey;

public final class VampireTaskHolders {
    public static final ResourceKey<Task> VAMPIRE_SKILL_POINTS_1 = ModTasks.TaskHolders.task("vampire_skill_points_1");
    public static final ResourceKey<Task> VAMPIRE_SKILL_POINTS_2 = ModTasks.TaskHolders.task("vampire_skill_points_2");

    private VampireTaskHolders() {
    }
}

