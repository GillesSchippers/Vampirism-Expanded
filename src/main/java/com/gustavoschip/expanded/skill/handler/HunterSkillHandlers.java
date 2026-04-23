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

import com.gustavoschip.expanded.service.skill.GarlicBloodService;
import com.gustavoschip.expanded.service.skill.PoisonousBloodService;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.function.Consumer;

public final class HunterSkillHandlers {
    private static final Logger LOGGER = LogUtils.getLogger();

    private HunterSkillHandlers() {
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> poisonousBloodToggle(boolean poisonous) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                if (!PoisonousBloodService.canSyncAttachment(serverPlayer)) {
                    LOGGER.debug("Deferred poisonous blood toggle {} for {} until login sync", poisonous, serverPlayer.getName().getString());
                    return;
                }
                LOGGER.debug("Toggling poisonous blood to {} for {}", poisonous, serverPlayer.getName().getString());
                PoisonousBloodService.setPoisonousBlood(serverPlayer, poisonous);
            }
        };
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> garlicBloodToggle(boolean garlicBlood) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                if (!GarlicBloodService.canSyncAttachment(serverPlayer)) {
                    LOGGER.debug("Deferred garlic blood toggle {} for {} until login sync", garlicBlood, serverPlayer.getName().getString());
                    return;
                }
                LOGGER.debug("Toggling garlic blood to {} for {}", garlicBlood, serverPlayer.getName().getString());
                GarlicBloodService.setGarlicBlood(serverPlayer, garlicBlood);
            }
        };
    }
}

