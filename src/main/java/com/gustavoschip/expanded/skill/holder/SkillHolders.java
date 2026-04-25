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

@SuppressWarnings("unused")
public final class SkillHolders {

    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> HUNTER_ROOT = ModSkills.SKILLS.register("hunter_root", () ->
        new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 0, false)
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> INNATE_TOUGHNESS = ModSkills.SKILLS.register("innate_toughness", () ->
        new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 1, true).setToggleActions(
            HunterSkillHandlers.innateToughnessToggle(true),
            HunterSkillHandlers.innateToughnessToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> ___ = ModSkills.SKILLS.register("hunters_growth", () ->
        new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 3, true).setToggleActions(
            HunterSkillHandlers.huntersGrowthToggle(true),
            HunterSkillHandlers.huntersGrowthToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> __ = ModSkills.SKILLS.register("prepared_hunt", () ->
        new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 5, true).setToggleActions(
            HunterSkillHandlers.preparedHuntToggle(true),
            HunterSkillHandlers.preparedHuntToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> GARLIC_BLOOD = ModSkills.SKILLS.register("garlic_blood", () ->
        new FactionSkillBase<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 7, true).setToggleActions(
            HunterSkillHandlers.garlicBloodToggle(true),
            HunterSkillHandlers.garlicBloodToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> POISONOUS_BLOOD = ModSkills.SKILLS.register("poisonous_blood", () ->
        new ActionFactionSkill<>(Either.left(SkillTreeHolders.HUNTER_LEVEL), ModSkills.HUNTER_FACTION_ID, 9, true).setToggleActions(
            HunterSkillHandlers.poisonousBloodToggle(true),
            HunterSkillHandlers.poisonousBloodToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRE_ROOT = ModSkills.SKILLS.register("vampire_root", () ->
        new FactionSkillBase<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 0, false)
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> BAT_SPEED = ModSkills.SKILLS.register("bat_speed", () ->
        new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 1, true).setToggleActions(
            VampireSkillHandlers.batSpeedToggle(true),
            VampireSkillHandlers.batSpeedToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> BAT_ARMOR = ModSkills.SKILLS.register("bat_armor", () ->
        new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 3, true).setToggleActions(
            VampireSkillHandlers.batArmorToggle(true),
            VampireSkillHandlers.batArmorToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> BAT_LIQUID = ModSkills.SKILLS.register("bat_liquid", () ->
        new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 5, true).setToggleActions(
            VampireSkillHandlers.batLiquidToggle(true),
            VampireSkillHandlers.batLiquidToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> VAMPIRIC_CONSTITUTION = ModSkills.SKILLS.register("vampiric_constitution", () ->
        new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 7, true).setToggleActions(
            VampireSkillHandlers.vampiricConstitutionToggle(true),
            VampireSkillHandlers.vampiricConstitutionToggle(false)
        )
    );
    public static final DeferredHolder<ISkill<?>, ISkill<? extends IFactionPlayer<?>>> DAY_WALKER = ModSkills.SKILLS.register("day_walker", () ->
        new ActionFactionSkill<>(Either.left(SkillTreeHolders.VAMPIRE_LEVEL), ModSkills.VAMPIRE_FACTION_ID, 9, true).setToggleActions(
            VampireSkillHandlers.dayWalkerToggle(true),
            VampireSkillHandlers.dayWalkerToggle(false)
        )
    );

    private SkillHolders() {}
}
