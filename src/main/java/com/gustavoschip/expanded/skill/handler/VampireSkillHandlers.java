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

package com.gustavoschip.expanded.skill.handler;

import com.gustavoschip.expanded.service.skill.AdvancedFlightService;
import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class VampireSkillHandlers {
    private static final Logger LOGGER = LogUtils.getLogger();

    private VampireSkillHandlers() {
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> vampiricGroundingToggle(boolean vampiricGrounding) {
        return createToggleAction("vampiric grounding", vampiricGrounding, VampiricGroundingService::setVampiricGrounding);
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> advancedFlightToggle(boolean advancedFlight) {
        return createToggleAction("advanced flight", advancedFlight, AdvancedFlightService::setAdvancedFlight);
    }

    private static <T extends IFactionPlayer<T>> Consumer<T> createToggleAction(String label, boolean value, BiConsumer<ServerPlayer, Boolean> setter) {
        return player -> {
            if (!(player.asEntity() instanceof ServerPlayer serverPlayer)) {
                return;
            }
            if (!com.gustavoschip.expanded.service.ModServices.canSyncAttachment(serverPlayer)) {
                LOGGER.debug("Deferred {} toggle {} for {} until login sync", label, value, serverPlayer.getName().getString());
                return;
            }
            LOGGER.debug("Toggling {} to {} for {}", label, value, serverPlayer.getName().getString());
            setter.accept(serverPlayer, value);
        };
    }
}

