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

import com.gustavoschip.expanded.service.skill.AdvancedFlightService;
import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.BatVampireAction;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(value = BatVampireAction.class, remap = false)
public abstract class BatVampireActionMixin {

    @Inject(method = "canBeUsedBy(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;)Z", at = @At("RETURN"), cancellable = true)
    private void expanded$preventGroundingBatModeWhen(IVampirePlayer vampire, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        if (!VampiricGroundingService.canEnterBatMode(vampire.asEntity())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "activate(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;Lde/teamlapen/vampirism/api/entity/player/actions/IAction$ActivationContext;)Z", at = @At("HEAD"), cancellable = true)
    private void expanded$blockGroundingBatMode(IVampirePlayer vampire, IAction.ActivationContext context, CallbackInfoReturnable<Boolean> cir) {
        if (VampiricGroundingService.handleBatActivation(vampire.asEntity())) {
            return;
        }

        cir.setReturnValue(false);
    }

    @Inject(method = "onReActivated(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;)V", at = @At("TAIL"))
    private void expanded$applyBatModeBonusesOnReload(IVampirePlayer vampire, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        expanded$applyBatModeBonuses(vampire);
    }

    @Inject(method = "activate(Lde/teamlapen/vampirism/api/entity/player/vampire/IVampirePlayer;Lde/teamlapen/vampirism/api/entity/player/actions/IAction$ActivationContext;)Z", at = @At("TAIL"))
    private void expanded$applyBatModeBonusesOnActivate(IVampirePlayer vampire, IAction.ActivationContext context, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            expanded$applyBatModeBonuses(vampire);
        }
    }

    @Unique
    private static void expanded$applyBatModeBonuses(IVampirePlayer vampire) {
        if (vampire.asEntity() instanceof ServerPlayer player) {
            AdvancedFlightService.onBatActivated(player);
        }
    }
}
