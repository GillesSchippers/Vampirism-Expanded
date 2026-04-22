package com.gustavoschip.expanded.mixin.client;

import com.gustavoschip.expanded.attachment.holder.SkillAttachmentHolders;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "de.teamlapen.vampirism.client.gui.overlay.VampirismHUDOverlay", remap = false)
public abstract class VampirismHUDOverlayMixin {
    @Unique
    private static final int POISONOUS_BLOOD_FANGS_COLOR = 0x099022;

    @ModifyVariable(
            method = "renderBloodFangs",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 2
    )
    private int expanded$forceGreenFangsForPoisonousPlayers(int color) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY) {
            return color;
        }

        Entity target = ((EntityHitResult) mc.hitResult).getEntity();
        if (target instanceof Player player && !target.isInvisible() && player.getData(SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT)) {
            return POISONOUS_BLOOD_FANGS_COLOR;
        }
        return color;
    }
}
