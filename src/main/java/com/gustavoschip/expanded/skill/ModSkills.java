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

import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.gustavoschip.expanded.skill.holder.SkillNodeHolders;
import com.gustavoschip.expanded.skill.holder.SkillTreeHolders;
import com.gustavoschip.expanded.task.ModTasks;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillNode;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillPointProvider;
import de.teamlapen.vampirism.api.entity.player.skills.SkillPointProviders;
import java.util.Collection;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public final class ModSkills {

    public static final DeferredRegister<ISkill<?>> SKILLS = DeferredRegister.create(VampirismRegistries.Keys.SKILL, MOD_ID);
    public static final ResourceLocation HUNTER_FACTION_ID = fromNamespaceAndPath("vampirism", "hunter");
    public static final ResourceLocation VAMPIRE_FACTION_ID = fromNamespaceAndPath("vampirism", "vampire");
    public static final ISkillPointProvider TASK_SKILL_POINTS = SkillPointProviders.register(
        fromNamespaceAndPath(MOD_ID, "task_skill_points"),
        ModTasks.TaskSkillPointStorage::getSkillPoints
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = SkillHolders.HUNTER_ROOT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = SkillHolders.POISONOUS_BLOOD;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> GARLIC_BLOOD = SkillHolders.GARLIC_BLOOD;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = SkillHolders.VAMPIRE_ROOT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRIC_GROUNDING = SkillHolders.VAMPIRIC_GROUNDING;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> ADVANCED_FLIGHT = SkillHolders.ADVANCED_FLIGHT;

    private ModSkills() {}

    public static void register(IEventBus modEventBus) {
        SKILLS.register(modEventBus);
    }

    public static ResourceKey<ISkillTree> tree(String path) {
        return ResourceKey.create(VampirismRegistries.Keys.SKILL_TREE, fromNamespaceAndPath(MOD_ID, path));
    }

    public static ResourceKey<ISkillNode> node(String path) {
        return ResourceKey.create(VampirismRegistries.Keys.SKILL_NODE, fromNamespaceAndPath(MOD_ID, path));
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

        public static final ResourceKey<ISkillNode> VAMPIRE_ROOT = SkillNodeHolders.VAMPIRE_ROOT;
        public static final ResourceKey<ISkillNode> VAMPIRE_1 = SkillNodeHolders.VAMPIRE_1;
        public static final ResourceKey<ISkillNode> VAMPIRE_2 = SkillNodeHolders.VAMPIRE_2;
        public static final ResourceKey<ISkillNode> VAMPIRE_3 = SkillNodeHolders.VAMPIRE_3;

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

            int spentPoints = enabledSkills
                .stream()
                .filter(ExpandedSkillPointHelper::usesExpandedPoints)
                .mapToInt(ISkill::getSkillPointCost)
                .sum();
            return Math.max(0, ModTasks.TaskSkillPointStorage.getSkillPoints(player) - spentPoints);
        }
    }
}
