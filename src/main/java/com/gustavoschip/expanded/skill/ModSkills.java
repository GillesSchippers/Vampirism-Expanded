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

package com.gustavoschip.expanded.skill;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

import com.gustavoschip.expanded.service.ModServices;
import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.gustavoschip.expanded.skill.holder.SkillNodeHolders;
import com.gustavoschip.expanded.skill.holder.SkillTreeHolders;
import com.gustavoschip.expanded.task.ModTasks;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillNode;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillPointProvider;
import de.teamlapen.vampirism.api.entity.player.skills.SkillPointProviders;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public abstract class ModSkills {

    public static final DeferredRegister<ISkill<?>> SKILLS = DeferredRegister.create(VampirismRegistries.Keys.SKILL, MOD_ID);
    public static final ResourceLocation HUNTER_FACTION_ID = fromNamespaceAndPath("vampirism", "hunter");
    public static final ResourceLocation VAMPIRE_FACTION_ID = fromNamespaceAndPath("vampirism", "vampire");
    public static final ISkillPointProvider TASK_SKILL_POINTS = SkillPointProviders.register(fromNamespaceAndPath(MOD_ID, "task_skill_points"), ModTasks.TaskSkillPointStorage::getSkillPoints);
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = SkillHolders.HUNTER_ROOT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> INNATE_TOUGHNESS = SkillHolders.INNATE_TOUGHNESS;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTERS_GROWTH = SkillHolders.HUNTERS_GROWTH;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> PREPARED_HUNT = SkillHolders.PREPARED_HUNT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = SkillHolders.POISONOUS_BLOOD;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> GARLIC_BLOOD = SkillHolders.GARLIC_BLOOD;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = SkillHolders.VAMPIRE_ROOT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> BAT_SPEED = SkillHolders.BAT_SPEED;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> BAT_ARMOR = SkillHolders.BAT_ARMOR;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> BAT_LIQUID = SkillHolders.BAT_LIQUID;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRIC_CONSTITUTION = SkillHolders.VAMPIRIC_CONSTITUTION;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> DAY_WALKER = SkillHolders.DAY_WALKER;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(IEventBus modEventBus) {
        SKILLS.register(modEventBus);
    }

    public static ResourceKey<ISkillTree> tree(String path) {
        return ResourceKey.create(VampirismRegistries.Keys.SKILL_TREE, fromNamespaceAndPath(MOD_ID, path));
    }

    public static ResourceKey<ISkillNode> node(String path) {
        return ResourceKey.create(VampirismRegistries.Keys.SKILL_NODE, fromNamespaceAndPath(MOD_ID, path));
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> createToggleAction(String label, boolean value, BiConsumer<ServerPlayer, Boolean> setter) {
        return factionPlayer -> {
            if (!(factionPlayer.asEntity() instanceof ServerPlayer serverPlayer)) {
                return;
            }

            runToggleWhenReady(serverPlayer, label, value, setter, 0);
        };
    }

    private static void runToggleWhenReady(ServerPlayer player, String label, boolean value, BiConsumer<ServerPlayer, Boolean> setter, int attempts) {
        if (player.isRemoved() || !player.isAlive()) {
            LOGGER.debug("Cancelled {} toggle for {} (player invalid)", label, player.getName().getString());
            return;
        }

        if (!ModServices.canSyncAttachment(player)) {
            if (attempts >= 40) {
                // ~2 seconds at 20 TPS
                LOGGER.debug("Aborted {} toggle {} for {} after {} retries", label, value, player.getName().getString(), attempts);
                return;
            }

            LOGGER.debug("Deferred {} toggle {} for {} until login sync (attempt {})", label, value, player.getName().getString(), attempts + 1);

            player.server.execute(() -> runToggleWhenReady(player, label, value, setter, attempts + 1));
            return;
        }

        LOGGER.debug("Toggling {} to {} for {}", label, value, player.getName().getString());
        setter.accept(player, value);
    }

    public static final class Trees {

        public static final ResourceKey<ISkillTree> HUNTER_LEVEL = SkillTreeHolders.HUNTER_LEVEL;
        public static final ResourceKey<ISkillTree> VAMPIRE_LEVEL = SkillTreeHolders.VAMPIRE_LEVEL;

        private Trees() {}
    }

    public static final class Nodes {

        public static final ResourceKey<ISkillNode> HUNTER_ROOT = SkillNodeHolders.HUNTER_ROOT;
        public static final ResourceKey<ISkillNode> HUNTER_1 = SkillNodeHolders.HUNTER_1;
        public static final ResourceKey<ISkillNode> HUNTER_2 = SkillNodeHolders.HUNTER_2;
        public static final ResourceKey<ISkillNode> HUNTER_3 = SkillNodeHolders.HUNTER_3;
        public static final ResourceKey<ISkillNode> HUNTER_4 = SkillNodeHolders.HUNTER_4;
        public static final ResourceKey<ISkillNode> HUNTER_5 = SkillNodeHolders.HUNTER_5;

        public static final ResourceKey<ISkillNode> VAMPIRE_ROOT = SkillNodeHolders.VAMPIRE_ROOT;
        public static final ResourceKey<ISkillNode> VAMPIRE_1 = SkillNodeHolders.VAMPIRE_1;
        public static final ResourceKey<ISkillNode> VAMPIRE_2 = SkillNodeHolders.VAMPIRE_2;
        public static final ResourceKey<ISkillNode> VAMPIRE_3 = SkillNodeHolders.VAMPIRE_3;
        public static final ResourceKey<ISkillNode> VAMPIRE_4 = SkillNodeHolders.VAMPIRE_4;
        public static final ResourceKey<ISkillNode> VAMPIRE_5 = SkillNodeHolders.VAMPIRE_5;

        private Nodes() {}
    }

    public static final class ExpandedSkillPointHelper {

        private ExpandedSkillPointHelper() {}

        public static boolean usesExpandedPoints(ISkill<?> skill) {
            return skill.allowedSkillTrees().map(ExpandedSkillPointHelper::isExpandedTree, tag -> false);
        }

        public static boolean isExpandedTree(Holder<ISkillTree> tree) {
            return tree.is(SkillTreeHolders.HUNTER_LEVEL) || tree.is(SkillTreeHolders.VAMPIRE_LEVEL);
        }

        public static boolean isExpandedTree(ResourceKey<ISkillTree> tree) {
            return SkillTreeHolders.HUNTER_LEVEL.equals(tree) || SkillTreeHolders.VAMPIRE_LEVEL.equals(tree);
        }

        public static int getRemainingExpandedPoints(IFactionPlayer<?> player, Collection<? extends ISkill<?>> enabledSkills) {
            if (de.teamlapen.vampirism.config.VampirismConfig.SERVER.unlockAllSkills.get() && player.getLevel() == player.getMaxLevel()) {
                return Integer.MAX_VALUE;
            }

            int spentPoints = enabledSkills.stream().filter(ExpandedSkillPointHelper::usesExpandedPoints).mapToInt(ISkill::getSkillPointCost).sum();
            return Math.max(0, ModTasks.TaskSkillPointStorage.getSkillPoints(player) - spentPoints);
        }
    }
}
