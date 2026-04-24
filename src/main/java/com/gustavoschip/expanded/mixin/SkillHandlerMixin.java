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

package com.gustavoschip.expanded.mixin;

import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.entity.player.skills.SkillHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@SuppressWarnings({"unused", "UnusedMixin", "DefaultAnnotationParam"})
@Mixin(value = SkillHandler.class, priority = 1000, remap = false)
public abstract class SkillHandlerMixin<T extends IFactionPlayer<T>> {
    @Shadow
    @Final
    private T player;
    @Shadow
    @Final
    private ArrayList<ISkill<T>> enabledSkills;

    @Redirect(method = "canSkillBeEnabled", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/entity/player/skills/SkillHandler;getLeftSkillPoints()I"))
    private int expanded$useExpandedPointsForExpandedSkills(de.teamlapen.vampirism.entity.player.skills.SkillHandler<?> instance, ISkill<T> skill) {
        if (!ModSkills.ExpandedSkillPointHelper.usesExpandedPoints(skill)) {
            return instance.getLeftSkillPoints();
        }

        return ModSkills.ExpandedSkillPointHelper.getRemainingExpandedPoints(this.player, this.enabledSkills);
    }
}


