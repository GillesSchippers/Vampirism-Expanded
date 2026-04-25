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

package com.gustavoschip.expanded.mixin.client;

import com.gustavoschip.expanded.mixin.SkillHandlerAccessorMixin;
import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.client.gui.screens.skills.SkillsTabScreen;
import java.util.ArrayList;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SkillsTabScreen.class, priority = 1000, remap = false)
public abstract class SkillsTabScreenMixin {

    @Shadow
    @Final
    private Holder<ISkillTree> skillTree;

    @Shadow
    @Final
    private ISkillHandler<?> skillHandler;

    @Inject(method = "getRemainingPoints", at = @At("HEAD"), cancellable = true)
    private void expanded$useExpandedRemainingPoints(CallbackInfoReturnable<Integer> cir) {
        if (!ModSkills.ExpandedSkillPointHelper.isExpandedTree(this.skillTree)) {
            return;
        }

        SkillHandlerAccessorMixin accessor = (SkillHandlerAccessorMixin) this.skillHandler;
        IFactionPlayer<?> player = accessor.expanded$getPlayer();
        ArrayList<ISkill<?>> enabledSkills = accessor.expanded$getEnabledSkills();
        cir.setReturnValue(ModSkills.ExpandedSkillPointHelper.getRemainingExpandedPoints(player, enabledSkills));
    }
}
