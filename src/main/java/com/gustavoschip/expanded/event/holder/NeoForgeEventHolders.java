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

package com.gustavoschip.expanded.event.holder;

import com.gustavoschip.expanded.service.skill.AdvancedFlightService;
import com.gustavoschip.expanded.service.skill.GarlicBloodService;
import com.gustavoschip.expanded.service.skill.PoisonousBloodService;
import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class NeoForgeEventHolders {
    public static final NeoForgeEventHolders INSTANCE = new NeoForgeEventHolders();

    private NeoForgeEventHolders() {
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PoisonousBloodService.syncFromHunterSkill(player);
        GarlicBloodService.syncFromHunterSkill(player);
        VampiricGroundingService.syncFromVampireSkill(player);
        AdvancedFlightService.syncFromVampireSkill(player);
    }

    @SubscribeEvent
    public void onLivingKnockBack(LivingKnockBackEvent event) {
        VampiricGroundingService.handleLivingKnockback(event);
    }
}
