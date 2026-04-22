package com.gustavoschip.expanded.skill;

import com.gustavoschip.expanded.skill.handler.HunterSkillHandlers;
import com.gustavoschip.expanded.skill.handler.VampireSkillHandlers;
import com.gustavoschip.expanded.skill.type.ActionFactionSkill;
import com.gustavoschip.expanded.skill.type.FactionSkillBase;
import com.mojang.datafixers.util.Either;
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
    private static final ResourceLocation HUNTER_FACTION_ID = fromNamespaceAndPath("vampirism", "hunter");
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = SKILLS.register("hunter_root",
            () -> new FactionSkillBase<>(Either.left(Trees.HUNTER_LEVEL), HUNTER_FACTION_ID, 0, false)
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = SKILLS.register("poisonous_blood",
            () -> new ActionFactionSkill<>(Either.left(Trees.HUNTER_LEVEL), HUNTER_FACTION_ID, 1, true)
                    .setToggleActions(
                            HunterSkillHandlers.poisonousBloodToggle(true),
                            HunterSkillHandlers.poisonousBloodToggle(false)
                    )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> GARLIC_BLOOD = SKILLS.register("garlic_blood",
            () -> new FactionSkillBase<>(Either.left(Trees.HUNTER_LEVEL), HUNTER_FACTION_ID, 1, true)
                    .setToggleActions(
                            HunterSkillHandlers.garlicBloodToggle(true),
                            HunterSkillHandlers.garlicBloodToggle(false)
                    )
    );
    private static final ResourceLocation VAMPIRE_FACTION_ID = fromNamespaceAndPath("vampirism", "vampire");
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = SKILLS.register("vampire_root",
            () -> new FactionSkillBase<>(Either.left(Trees.VAMPIRE_LEVEL), VAMPIRE_FACTION_ID, 0, false)
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRIC_GROUNDING = SKILLS.register("vampiric_grounding",
            () -> new ActionFactionSkill<>(Either.left(Trees.VAMPIRE_LEVEL), VAMPIRE_FACTION_ID, 1, true)
                    .setToggleActions(
                            VampireSkillHandlers.vampiricGroundingToggle(true),
                            VampireSkillHandlers.vampiricGroundingToggle(false)
                    )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> ADVANCED_FLIGHT = SKILLS.register("advanced_flight",
            () -> new ActionFactionSkill<>(Either.left(Trees.VAMPIRE_LEVEL), VAMPIRE_FACTION_ID, 1, true)
                    .setToggleActions(
                            VampireSkillHandlers.advancedFlightToggle(true),
                            VampireSkillHandlers.advancedFlightToggle(false)
                    )
    );

    private ModSkills() {
    }

    public static void register(IEventBus modEventBus) {
        SKILLS.register(modEventBus);
    }

    public static final class Trees {
        public static final ResourceKey<ISkillTree> HUNTER_LEVEL = tree("hunter/level");
        public static final ResourceKey<ISkillTree> VAMPIRE_LEVEL = tree("vampire/level");

        private Trees() {
        }

        private static ResourceKey<ISkillTree> tree(String path) {
            return ResourceKey.create(VampirismRegistries.Keys.SKILL_TREE, fromNamespaceAndPath(MOD_ID, path));
        }
    }

    public static final class Nodes {
        public static final ResourceKey<ISkillNode> HUNTER_ROOT = node("hunter_root");
        public static final ResourceKey<ISkillNode> HUNTER_1 = node("hunter_1");

        public static final ResourceKey<ISkillNode> VAMPIRE_ROOT = node("vampire_root");
        public static final ResourceKey<ISkillNode> VAMPIRE_1 = node("vampire_1");

        private Nodes() {
        }

        private static ResourceKey<ISkillNode> node(String path) {
            return ResourceKey.create(VampirismRegistries.Keys.SKILL_NODE, fromNamespaceAndPath(MOD_ID, path));
        }
    }
}

