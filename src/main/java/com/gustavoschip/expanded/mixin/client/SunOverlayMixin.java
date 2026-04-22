package com.gustavoschip.expanded.mixin.client;

import com.gustavoschip.expanded.attachment.holder.SkillAttachmentHolders;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "de.teamlapen.vampirism.client.gui.overlay.SunOverlay", remap = false)
public abstract class SunOverlayMixin {
    @Unique
    private static final float GROUNDING_START_SCALE = 1.5F;
    @Unique
    private static final float GROUNDING_END_SCALE = 1.0F;

    @Unique
    private static final float GROUNDING_ALPHA = 0.5F;

    @Unique
    private static boolean expanded$HasVampiricGrounding() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null &&
                mc.player.getData(SkillAttachmentHolders.VAMPIRIC_GROUNDING_ATTACHMENT);
    }

    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/teamlapen/vampirism/client/gui/overlay/SunOverlay;scaleBy(FFFFLnet/minecraft/client/gui/GuiGraphics;)V"
            )
    )
    private void expanded$modifySunOverlayScale(Args args) {
        if (!expanded$HasVampiricGrounding()) {
            return;
        }

        args.set(2, GROUNDING_START_SCALE);
        args.set(3, GROUNDING_END_SCALE);
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/teamlapen/vampirism/client/gui/overlay/SunOverlay;renderTextureOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;F)V"
            ),
            index = 2
    )
    private float expanded$modifySunOverlayAlpha(float originalAlpha) {
        return expanded$HasVampiricGrounding() ? GROUNDING_ALPHA : originalAlpha;
    }
}

