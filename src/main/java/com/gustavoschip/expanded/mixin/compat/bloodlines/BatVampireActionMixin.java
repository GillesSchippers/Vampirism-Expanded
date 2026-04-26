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

package com.gustavoschip.expanded.mixin.compat.bloodlines;

import com.bawnorton.mixinsquared.TargetHandler;
import com.gustavoschip.expanded.service.skill.VampireSkillService;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.teamlapen.vampirism.entity.player.vampire.actions.BatVampireAction;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(type = Condition.Type.MOD, value = "bloodlines"))
@Mixin(value = BatVampireAction.class, priority = 1500, remap = false)
public abstract class BatVampireActionMixin {

    @TargetHandler(mixin = "com.thedrofdoctoring.bloodlines.mixin.BatVampireActionMixin", name = "setNobleBatSpeedMultiplier")
    @Restriction(require = @Condition(type = Condition.Type.MIXIN, value = "com.thedrofdoctoring.bloodlines.mixin.BatVampireActionMixin"))
    @WrapOperation(
        method = "@MixinSquared:Handler",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addPermanentModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V")
    )
    private void expanded$blockBloodlinesArmor(AttributeInstance instance, AttributeModifier modifier, Operation<Void> original, @Local(argsOnly = true) Player player) {
        if (VampireSkillService.hasBatArmor(player)) {
            return;
        }

        original.call(instance, modifier);
    }
}
