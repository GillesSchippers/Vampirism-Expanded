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

import com.gustavoschip.expanded.service.skill.VampireService;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.BatVampireAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BatVampireAction.class, priority = 1000, remap = false)
public abstract class BatVampireActionMixin {

    @Redirect(
        method = { "canBeUsedBy(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;)Z", "onUpdate(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;)Z" },
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWater()Z")
    )
    private boolean expanded$allowBatModeInLiquids(Player player) {
        return player.isInWater() && !VampireService.canUseBatModeInLiquids(player);
    }

    @Inject(method = "activate(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;Lde/teamlapen/vampirism/api/entity/player/actions/IAction$ActivationContext;)Z", at = @At("TAIL"))
    private void expanded$applyBatModeBonusesOnActivate(IVampirePlayer vampire, IAction.ActivationContext context, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && (vampire.asEntity() instanceof ServerPlayer player)) {
            VampireService.onBatActivated(player);
        }
    }

    @WrapOperation(
        method = "setModifier(Lnet/minecraft/world/entity/player/Player;Z)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addPermanentModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V")
    )
    private void expanded$blockMainVampirismBatArmor(
        AttributeInstance instance,
        AttributeModifier modifier,
        Operation<Void> original,
        @Local(argsOnly = true) Player player,
        @Local(argsOnly = true) boolean enabled
    ) {
        if (enabled && VampireService.hasBatArmor(player) && (instance == player.getAttribute(Attributes.ARMOR) || instance == player.getAttribute(Attributes.ARMOR_TOUGHNESS))) {
            return;
        }

        original.call(instance, modifier);
    }
}
