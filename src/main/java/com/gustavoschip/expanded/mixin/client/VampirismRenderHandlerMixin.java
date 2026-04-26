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

import com.gustavoschip.expanded.service.skill.HunterSkillService;
import de.teamlapen.vampirism.client.renderer.RenderHandler;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = RenderHandler.class, priority = 1000, remap = false)
public abstract class VampirismRenderHandlerMixin {

    @Unique
    private static final int POISONOUS_BLOOD_VISION_RED = 0x07;

    @Unique
    private static final int POISONOUS_BLOOD_VISION_GREEN = 0xE0;

    @Unique
    private static final int POISONOUS_BLOOD_VISION_BLUE = 0x07;

    @Unique
    private Entity expanded$currentBloodVisionEntity;

    @Inject(method = "onRenderLivingPost", at = @At("HEAD"))
    private void expanded$captureBloodVisionEntity(RenderLivingEvent.Post<?, ?> event, CallbackInfo ci) {
        this.expanded$currentBloodVisionEntity = event.getEntity();
    }

    @ModifyArgs(method = "onRenderLivingPost", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/client/renderer/RenderHandler$OutlineBuffer;setColor(IIII)V", remap = false))
    private void expanded$setGreenBloodVisionColorForPoisonousPlayers(Args args) {
        Entity entity = this.expanded$currentBloodVisionEntity;
        if (!(entity instanceof Player player) || !HunterSkillService.hasPoisonousBlood(player)) {
            return;
        }

        Player viewer = Minecraft.getInstance().player;
        if (viewer == null || !VampirismPlayerAttributes.get(viewer).getVampSpecial().blood_vision_garlic) {
            return;
        }

        args.set(0, POISONOUS_BLOOD_VISION_RED);
        args.set(1, POISONOUS_BLOOD_VISION_GREEN);
        args.set(2, POISONOUS_BLOOD_VISION_BLUE);
    }

    @Inject(method = "onRenderLivingPost", at = @At("RETURN"))
    private void expanded$clearBloodVisionEntity(RenderLivingEvent.Post<?, ?> event, CallbackInfo ci) {
        this.expanded$currentBloodVisionEntity = null;
    }
}
