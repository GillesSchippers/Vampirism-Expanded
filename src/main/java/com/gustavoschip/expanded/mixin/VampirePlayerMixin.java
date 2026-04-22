package com.gustavoschip.expanded.mixin;

import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "de.teamlapen.vampirism.entity.player.vampire.VampirePlayer", remap = false)
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


