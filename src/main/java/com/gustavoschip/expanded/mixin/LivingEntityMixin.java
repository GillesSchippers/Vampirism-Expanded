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
import com.gustavoschip.expanded.service.tracker.SunDamageTracker;
import de.teamlapen.vampirism.core.ModDamageTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 1000, remap = true)
public abstract class LivingEntityMixin implements SunDamageTracker {

    @Unique
    private boolean expanded$lastDamageWasSun;

    @Inject(method = "hurt", at = @At("HEAD"))
    private void expanded$captureDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.expanded$lastDamageWasSun = source != null && source.is(ModDamageTypes.SUN_DAMAGE);
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void expanded$clearDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.expanded$lastDamageWasSun = false;
    }

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void expanded$cancelSunKnockback(double strength, double x, double z, CallbackInfo ci) {
        if (!this.expanded$lastDamageWasSun) return;

        LivingEntity self = (LivingEntity) (Object) this;

        if (self instanceof ServerPlayer player && VampireService.hasDayWalker(player)) {
            ci.cancel();
        }
    }

    @Override
    public boolean expanded$isLastDamageWasSun() {
        return this.expanded$lastDamageWasSun;
    }
}
