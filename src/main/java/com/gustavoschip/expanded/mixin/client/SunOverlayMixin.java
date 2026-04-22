package com.gustavoschip.expanded.mixin.client;

import com.gustavoschip.expanded.attachment.ModAttachments;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "de.teamlapen.vampirism.client.gui.overlay.SunOverlay", remap = false)
public abstract class SunOverlayMixin {
    @Unique
    private static final float GROUNDING_SUN_OVERLAY_SCALE_START = 1.35F;
    @Unique
    private static final float GROUNDING_SUN_OVERLAY_SCALE_END = 0.75F;

    @ModifyArgs(
            method = "render",
            at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/client/gui/overlay/SunOverlay;scaleBy(FFFFLnet/minecraft/client/gui/GuiGraphics;)V")
    )
    private void expanded$shrinkSunOverlayWhenGrounded(Args args) {
        if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getData(ModAttachments.VAMPIRIC_GROUNDING_ATTACHMENT)) {
            return;
        }

        args.set(2, GROUNDING_SUN_OVERLAY_SCALE_START);
        args.set(3, GROUNDING_SUN_OVERLAY_SCALE_END);
    }
}

