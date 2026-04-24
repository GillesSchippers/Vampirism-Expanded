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

import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(value = VampirePlayer.class, remap = false)
public abstract class VampirePlayerMixin {

    @Inject(method = "determineBiteType", at = @At("RETURN"), cancellable = true)
    private void expanded$treatPoisonousPlayersAsHunterCreatures(LivingEntity entity, CallbackInfoReturnable<IVampirePlayer.BITE_TYPE> cir) {
        if (cir.getReturnValue() != IVampirePlayer.BITE_TYPE.SUCK_BLOOD_PLAYER) {
            return;
        }

        if (com.gustavoschip.expanded.service.skill.PoisonousBloodService.isPoisonousBloodTarget(entity)) {
            // HUNTER_CREATURE path interrupts the bite attempt immediately and poisons the vampire.
            cir.setReturnValue(IVampirePlayer.BITE_TYPE.HUNTER_CREATURE);
        }
    }
}


