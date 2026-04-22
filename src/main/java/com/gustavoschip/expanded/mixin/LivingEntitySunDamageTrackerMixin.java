package com.gustavoschip.expanded.mixin;

import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntitySunDamageTrackerMixin implements VampiricGroundingService.LivingEntitySunDamageTracker {
    @Unique
    private boolean expanded$lastDamageWasSun;

    @Inject(method = "hurt", at = @At("HEAD"))
    private void expanded$captureDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.expanded$lastDamageWasSun = source != null && "sun".equals(source.getMsgId());
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void expanded$clearDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.expanded$lastDamageWasSun = false;
    }

    @Override
    public boolean expanded$isLastDamageWasSun() {
        return this.expanded$lastDamageWasSun;
    }
}


