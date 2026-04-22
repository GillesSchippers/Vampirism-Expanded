package com.gustavoschip.expanded.skill;

import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.gustavoschip.expanded.skill.holder.SkillNodeHolders;
import com.gustavoschip.expanded.skill.holder.SkillTreeHolders;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.factions.ISkillNode;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@SuppressWarnings("unused")
public final class ModSkills {
    public static final DeferredRegister<ISkill<?>> SKILLS = DeferredRegister.create(VampirismRegistries.Keys.SKILL, MOD_ID);
    public static final ResourceLocation HUNTER_FACTION_ID = fromNamespaceAndPath("vampirism", "hunter");
    public static final ResourceLocation VAMPIRE_FACTION_ID = fromNamespaceAndPath("vampirism", "vampire");

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = SkillHolders.HUNTER_ROOT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = SkillHolders.POISONOUS_BLOOD;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> GARLIC_BLOOD = SkillHolders.GARLIC_BLOOD;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = SkillHolders.VAMPIRE_ROOT;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRIC_GROUNDING = SkillHolders.VAMPIRIC_GROUNDING;
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> ADVANCED_FLIGHT = SkillHolders.ADVANCED_FLIGHT;

    private ModSkills() {
    }

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

        private Trees() {
        }

    }

    public static final class Nodes {
        public static final ResourceKey<ISkillNode> HUNTER_ROOT = SkillNodeHolders.HUNTER_ROOT;
        public static final ResourceKey<ISkillNode> HUNTER_1 = SkillNodeHolders.HUNTER_1;

        public static final ResourceKey<ISkillNode> VAMPIRE_ROOT = SkillNodeHolders.VAMPIRE_ROOT;
        public static final ResourceKey<ISkillNode> VAMPIRE_1 = SkillNodeHolders.VAMPIRE_1;

        private Nodes() {
        }

    }
}

