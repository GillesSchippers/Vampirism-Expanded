package com.gustavoschip.expanded.skill.holder;

import com.gustavoschip.expanded.skill.ModSkills;
import com.gustavoschip.expanded.skill.handler.HunterSkillHandlers;
import com.gustavoschip.expanded.skill.handler.VampireSkillHandlers;
import com.gustavoschip.expanded.skill.type.ActionFactionSkill;
import com.gustavoschip.expanded.skill.type.FactionSkillBase;
import com.mojang.datafixers.util.Either;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class SkillHolders {
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = ModSkills.SKILLS.register("hunter_root",
            () -> new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 0, false)
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = ModSkills.SKILLS.register("poisonous_blood",
            () -> new ActionFactionSkill<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 9, true)
                    .setToggleActions(
                            HunterSkillHandlers.poisonousBloodToggle(true),
                            HunterSkillHandlers.poisonousBloodToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> GARLIC_BLOOD = ModSkills.SKILLS.register("garlic_blood",
            () -> new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 9, true)
                    .setToggleActions(
                            HunterSkillHandlers.garlicBloodToggle(true),
                            HunterSkillHandlers.garlicBloodToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = ModSkills.SKILLS.register("vampire_root",
            () -> new FactionSkillBase<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 0, false)
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRIC_GROUNDING = ModSkills.SKILLS.register("vampiric_grounding",
            () -> new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 9, true)
                    .setToggleActions(
                            VampireSkillHandlers.vampiricGroundingToggle(true),
                            VampireSkillHandlers.vampiricGroundingToggle(false)
                    )
    );

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> ADVANCED_FLIGHT = ModSkills.SKILLS.register("advanced_flight",
            () -> new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 9, true)
                    .setToggleActions(
                            VampireSkillHandlers.advancedFlightToggle(true),
                            VampireSkillHandlers.advancedFlightToggle(false)
                    )
    );

    private SkillHolders() {
    }
}
