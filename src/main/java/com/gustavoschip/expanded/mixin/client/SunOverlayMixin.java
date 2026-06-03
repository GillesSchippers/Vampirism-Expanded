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

package com.gustavoschip.expanded.mixin.client;

import static com.gustavoschip.expanded.service.ModServices.canSyncAttachment;
import static com.gustavoschip.expanded.service.ModServices.has;

import com.gustavoschip.expanded.attachment.holder.ExpandedAttachmentHolders;
import de.teamlapen.vampirism.client.gui.overlay.SunOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Softens Vampirism's sun overlay for Day Walker players once the local player has synced
 * state.
 */

@Mixin(value = SunOverlay.class, priority = 1000, remap = false)
public abstract class SunOverlayMixin {

    @Unique
    private static final float GROUNDING_START_SCALE = 1.5F;

    @Unique
    private static final float GROUNDING_END_SCALE = 1.0F;

    @Unique
    private static final float GROUNDING_ALPHA = 0.5F;

    @Unique
    private static boolean expanded$HasDayWalker() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer localPlayer = mc.player;

        if (localPlayer == null) {
            return false;
        }

        return canSyncAttachment(localPlayer) && has(localPlayer, ExpandedAttachmentHolders.DAY_WALKER);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/client/gui/overlay/SunOverlay;scaleBy(FFFFLnet/minecraft/client/gui/GuiGraphics;)V"))
    private void expanded$modifySunOverlayScale(Args args) {
        if (!expanded$HasDayWalker()) {
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
        return expanded$HasDayWalker() ? GROUNDING_ALPHA : originalAlpha;
    }
}
