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
