package com.gustavoschip.poisonousblood.mixin.client;

import com.gustavoschip.poisonousblood.attachment.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "de.teamlapen.vampirism.client.gui.overlay.VampirismHUDOverlay", remap = false)
public abstract class VampirismHUDOverlayMixin {

    @ModifyVariable(
            method = "renderBloodFangs",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 2
    )
    private int poisonousblood$forceGreenFangsForPoisonousPlayers(int color) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY) {
            return color;
        }

        Entity target = ((EntityHitResult) mc.hitResult).getEntity();
        if (target instanceof Player player && !target.isInvisible() && player.getData(ModAttachments.POISONOUS_BLOOD_ATTACHMENT)) {
            return 0x099022;
        }
        return color;
    }
}
