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

package com.gustavoschip.expanded.task;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

import com.gustavoschip.expanded.attachment.holder.TaskAttachmentHolders;
import com.gustavoschip.expanded.task.holder.HunterTaskHolders;
import com.gustavoschip.expanded.task.holder.VampireTaskHolders;
import com.gustavoschip.expanded.task.reward.SkillPointTaskReward;
import com.gustavoschip.expanded.task.type.FactionLevelTaskUnlocker;
import com.mojang.serialization.MapCodec;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.ITaskRewardInstance;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import de.teamlapen.vampirism.api.entity.player.task.TaskReward;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class ModTasks {

    public static final DeferredRegister<MapCodec<? extends TaskUnlocker>> TASK_UNLOCKER = DeferredRegister.create(VampirismRegistries.Keys.TASK_UNLOCKER, MOD_ID);
    public static final DeferredRegister<MapCodec<? extends TaskReward>> TASK_REWARDS = DeferredRegister.create(VampirismRegistries.Keys.TASK_REWARD, MOD_ID);
    public static final DeferredRegister<MapCodec<? extends ITaskRewardInstance>> TASK_REWARD_INSTANCES = DeferredRegister.create(VampirismRegistries.Keys.TASK_REWARD_INSTANCE, MOD_ID);

    public static final DeferredHolder<MapCodec<? extends TaskUnlocker>, MapCodec<FactionLevelTaskUnlocker>> FACTION_LEVEL_UNLOCKER = TASK_UNLOCKER.register("faction_level", () ->
        FactionLevelTaskUnlocker.CODEC
    );
    public static final DeferredHolder<MapCodec<? extends TaskReward>, MapCodec<SkillPointTaskReward>> SKILL_POINT_REWARD = TASK_REWARDS.register("skill_points", () -> SkillPointTaskReward.CODEC);
    public static final DeferredHolder<MapCodec<? extends ITaskRewardInstance>, MapCodec<SkillPointTaskReward>> SKILL_POINT_REWARD_INSTANCE = TASK_REWARD_INSTANCES.register("skill_points", () ->
        SkillPointTaskReward.CODEC
    );

    private ModTasks() {}

    public static void register(IEventBus modEventBus) {
        TASK_UNLOCKER.register(modEventBus);
        TASK_REWARDS.register(modEventBus);
        TASK_REWARD_INSTANCES.register(modEventBus);
    }

    public static final class TaskHolders {

        public static final ResourceLocation HUNTER_FACTION_ID = fromNamespaceAndPath("vampirism", "hunter");
        public static final ResourceLocation VAMPIRE_FACTION_ID = fromNamespaceAndPath("vampirism", "vampire");

        public static final ResourceKey<Task> HUNTER_SKILL_POINTS_1 = HunterTaskHolders.HUNTER_SKILL_POINTS_1;
        public static final ResourceKey<Task> HUNTER_SKILL_POINTS_2 = HunterTaskHolders.HUNTER_SKILL_POINTS_2;
        public static final ResourceKey<Task> VAMPIRE_SKILL_POINTS_1 = VampireTaskHolders.VAMPIRE_SKILL_POINTS_1;
        public static final ResourceKey<Task> VAMPIRE_SKILL_POINTS_2 = VampireTaskHolders.VAMPIRE_SKILL_POINTS_2;

        private TaskHolders() {}

        public static ResourceKey<Task> task(String path) {
            return ResourceKey.create(VampirismRegistries.Keys.TASK, fromNamespaceAndPath(MOD_ID, path));
        }
    }

    public static final class TaskSkillPointStorage {

        private TaskSkillPointStorage() {}

        public static int getSkillPoints(IFactionPlayer<?> factionPlayer) {
            DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> attachment = getAttachmentForFaction(factionPlayer.getFaction().getID());
            return attachment == null ? 0 : factionPlayer.asEntity().getData(attachment);
        }

        public static void addSkillPoints(IFactionPlayer<?> factionPlayer, int amount) {
            addSkillPoints(factionPlayer, factionPlayer.getFaction().getID(), amount);
        }

        public static void addSkillPoints(IFactionPlayer<?> factionPlayer, ResourceLocation factionId, int amount) {
            if (amount <= 0 || !factionPlayer.getFaction().getID().equals(factionId)) {
                return;
            }

            DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> attachment = getAttachmentForFaction(factionId);
            if (attachment == null) {
                return;
            }

            addSkillPoints(factionPlayer, attachment, amount);
        }

        private static void addSkillPoints(IFactionPlayer<?> factionPlayer, DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> attachment, int amount) {
            Player player = factionPlayer.asEntity();
            player.setData(attachment, player.getData(attachment) + amount);
        }

        private static @Nullable DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> getAttachmentForFaction(ResourceLocation factionId) {
            if (TaskHolders.HUNTER_FACTION_ID.equals(factionId)) {
                return TaskAttachmentHolders.HUNTER_TASK_SKILL_POINTS_ATTACHMENT;
            }
            if (TaskHolders.VAMPIRE_FACTION_ID.equals(factionId)) {
                return TaskAttachmentHolders.VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT;
            }
            return null;
        }
    }
}
