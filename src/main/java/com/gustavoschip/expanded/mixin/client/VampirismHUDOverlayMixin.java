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

import com.gustavoschip.expanded.service.skill.HunterService;
import de.teamlapen.vampirism.client.gui.overlay.VampirismHUDOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = VampirismHUDOverlay.class, priority = 1000, remap = false)
public abstract class VampirismHUDOverlayMixin {

    @Unique
    private static final int POISONOUS_BLOOD_FANGS_COLOR = 0x099022;

    @ModifyVariable(method = "renderBloodFangs", at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private int expanded$forceGreenFangsForPoisonousPlayers(int color) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY) {
            return color;
        }

        Entity target = ((EntityHitResult) mc.hitResult).getEntity();
        if (target instanceof Player player && !target.isInvisible() && HunterService.hasPoisonousBlood(player)) {
            return POISONOUS_BLOOD_FANGS_COLOR;
        }
        return color;
    }
}
