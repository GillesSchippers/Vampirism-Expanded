package com.gustavoschip.expanded.skill;

import com.gustavoschip.expanded.skill.handler.HunterSkillHandlers;
import com.gustavoschip.expanded.skill.handler.VampireSkillHandlers;
import com.gustavoschip.expanded.skill.handler.WerewolfSkillHandlers;
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

import static com.gustavoschip.expanded.PoisonousBlood.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@SuppressWarnings("unused")
public final class ModSkills {
    private static final ResourceLocation HUNTER_FACTION_ID = fromNamespaceAndPath("vampirism", "hunter");
    private static final ResourceLocation VAMPIRE_FACTION_ID = fromNamespaceAndPath("vampirism", "vampire");
    private static final ResourceLocation WEREWOLF_FACTION_ID = fromNamespaceAndPath("werewolves", "werewolf");

    public static final DeferredRegister<ISkill<?>> SKILLS = DeferredRegister.create(VampirismRegistries.Keys.SKILL, MOD_ID);

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = SKILLS.register("hunter/root",
            () -> new FactionSkillBase<>(Either.left(Trees.HUNTER_LEVEL), HUNTER_FACTION_ID, 0, false)
                    .setToggleActions(
                            HunterSkillHandlers.doToggle(true),
                            HunterSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = SKILLS.register("vampire/root",
            () -> new FactionSkillBase<>(Either.left(Trees.VAMPIRE_LEVEL), VAMPIRE_FACTION_ID, 0, false)
                    .setToggleActions(
                            HunterSkillHandlers.doToggle(true),
                            HunterSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> WEREWOLF_ROOT = SKILLS.register("werewolf/root",
            () -> new FactionSkillBase<>(Either.left(Trees.WEREWOLF_LEVEL), WEREWOLF_FACTION_ID, 0, false)
                    .setToggleActions(
                            HunterSkillHandlers.doToggle(true),
                            HunterSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = SKILLS.register("poisonous/blood",
            () -> new ActionFactionSkill<>(Either.left(Trees.HUNTER_LEVEL), HUNTER_FACTION_ID, 1, true)
                    .setToggleActions(
                            HunterSkillHandlers.poisonousBloodToggle(true),
                            HunterSkillHandlers.poisonousBloodToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_RESOLVE = SKILLS.register("hunter/resolve",
            () -> new FactionSkillBase<>(Either.left(Trees.HUNTER_LEVEL), HUNTER_FACTION_ID, 1, true)
                    .setToggleActions(
                            HunterSkillHandlers.doToggle(true),
                            HunterSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_BLOOD_FOCUS = SKILLS.register("vampire/blood_focus",
            () -> new FactionSkillBase<>(Either.left(Trees.VAMPIRE_LEVEL), VAMPIRE_FACTION_ID, 1, true)
                    .setToggleActions(
                            VampireSkillHandlers.doToggle(true),
                            VampireSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_SHADOW_SENSE = SKILLS.register("vampire/shadow_sense",
            () -> new FactionSkillBase<>(Either.left(Trees.VAMPIRE_LEVEL), VAMPIRE_FACTION_ID, 1, true)
                    .setToggleActions(
                            VampireSkillHandlers.doToggle(true),
                            VampireSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> WEREWOLF_MOON_SENSE = SKILLS.register("werewolf/moon_sense",
            () -> new FactionSkillBase<>(Either.left(Trees.WEREWOLF_LEVEL), WEREWOLF_FACTION_ID, 1, true)
                    .setToggleActions(
                            WerewolfSkillHandlers.doToggle(true),
                            WerewolfSkillHandlers.doToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> WEREWOLF_PACK_INSTINCT = SKILLS.register("werewolf/pack_instinct",
            () -> new FactionSkillBase<>(Either.left(Trees.WEREWOLF_LEVEL), WEREWOLF_FACTION_ID, 1, true)
                    .setToggleActions(
                            WerewolfSkillHandlers.doToggle(true),
                            WerewolfSkillHandlers.doToggle(false)
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
        public static final ResourceKey<ISkillTree> WEREWOLF_LEVEL = tree("werewolf/level");

        private Trees() {
        }

        private static ResourceKey<ISkillTree> tree(String path) {
            return ResourceKey.create(VampirismRegistries.Keys.SKILL_TREE, fromNamespaceAndPath(MOD_ID, path));
        }
    }

    public static final class Nodes {
        public static final ResourceKey<ISkillNode> HUNTER_ROOT = node("hunter/root");
        public static final ResourceKey<ISkillNode> HUNTER_POISONOUS_BLOOD = node("hunter/poisonous_blood");
        public static final ResourceKey<ISkillNode> HUNTER_RESOLVE = node("hunter/resolve");

        public static final ResourceKey<ISkillNode> VAMPIRE_ROOT = node("vampire/root");
        public static final ResourceKey<ISkillNode> VAMPIRE_BLOOD_FOCUS = node("vampire/blood_focus");
        public static final ResourceKey<ISkillNode> VAMPIRE_SHADOW_SENSE = node("vampire/shadow_sense");

        public static final ResourceKey<ISkillNode> WEREWOLF_ROOT = node("werewolf/root");
        public static final ResourceKey<ISkillNode> WEREWOLF_MOON_SENSE = node("werewolf/moon_sense");
        public static final ResourceKey<ISkillNode> WEREWOLF_PACK_INSTINCT = node("werewolf/pack_instinct");

        private Nodes() {
        }

        private static ResourceKey<ISkillNode> node(String path) {
            return ResourceKey.create(VampirismRegistries.Keys.SKILL_NODE, fromNamespaceAndPath(MOD_ID, path));
        }
    }
}

