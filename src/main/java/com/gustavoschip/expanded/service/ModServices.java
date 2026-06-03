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

package com.gustavoschip.expanded.service;

import static de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler;

import com.gustavoschip.expanded.attachment.ModAttachments;
import com.gustavoschip.expanded.attachment.holder.ExpandedAttachmentHolders;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import java.lang.management.ManagementFactory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Shared service helpers for attachment sync, skill lookups, and debugger-gated debug
 * logging.
 */

public abstract class ModServices {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String DEBUG_PROPERTY = "expanded.debug";

    public static boolean shouldLogDebug() {
        return LOGGER.isDebugEnabled() && (isDebuggerAttached() || Boolean.getBoolean(DEBUG_PROPERTY));
    }

    private static boolean isDebuggerAttached() {
        return ManagementFactory.getRuntimeMXBean()
            .getInputArguments()
            .stream()
            .anyMatch(arg -> arg.contains("jdwp") || arg.contains("Xrunjdwp"));
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player != null && player.server != null && player.connection != null && !player.hasDisconnected() && !player.isRemoved();
    }

    public static boolean canSyncAttachment(LocalPlayer player) {
        return player != null && player.connection != null && !player.isRemoved();
    }

    public static @NotNull ExpandedAttachmentHolders getSharedAttachment(@NotNull Player player) {
        if (player.hasData(ModAttachments.SHARED_ATTACHMENT)) {
            return player.getData(ModAttachments.SHARED_ATTACHMENT);
        }
        return new ExpandedAttachmentHolders();
    }

    public static void setSharedAttachment(ServerPlayer player, ExpandedAttachmentHolders data) {
        setSharedAttachment(player, data, 0);
    }

    private static void setSharedAttachment(ServerPlayer player, ExpandedAttachmentHolders data, int attempts) {
        if (!canSyncAttachment(player)) {
            if (attempts >= 40) {
                LOGGER.warn("Failed to sync shared attachment for {} after retries", player.getName().getString());
                return;
            }

            player.server.tell(new TickTask(player.server.getTickCount() + 1, () -> setSharedAttachment(player, data, attempts + 1)));
            return;
        }

        player.setData(ModAttachments.SHARED_ATTACHMENT, data);
        if (shouldLogDebug()) {
            LOGGER.debug("Synced shared attachment for {}", player.getName().getString());
        }

        // Trigger skill handler sync callback to ensure client and server resyncs after successful attachment update
        onAttachmentSyncSuccess(player);
    }

    private static void onAttachmentSyncSuccess(@NotNull ServerPlayer player) {
        try {
            if (shouldLogDebug()) {
                LOGGER.debug("Triggering attachment sync callback for {}", player.getName().getString());
            }

            // Use Vampirism's canonical sync helper to broadcast the updated faction/player
            // handler state to the client(s). This mirrors Vampirism's internal behavior and
            // ensures skill handlers and UI see the updated attachment state.
            try {
                de.teamlapen.lib.HelperLib.sync(de.teamlapen.vampirism.entity.factions.FactionPlayerHandler.get(player), player, true);
                if (shouldLogDebug()) {
                    LOGGER.debug("Attachment sync callback completed for {} via HelperLib.sync", player.getName().getString());
                }
            } catch (Exception e) {
                LOGGER.error("Failed to trigger Vampirism resync for {}", player.getName().getString(), e);
            }
        } catch (Exception e) {
            LOGGER.error("Error during attachment sync callback for {}", player.getName().getString(), e);
        }
    }

    public static boolean hasSkill(ServerPlayer player, DeferredHolder<ISkill<?>, ? extends ISkill<? extends IFactionPlayer<?>>> skill) {
        return factionPlayerHandler(player)
            .getCurrentFactionPlayer()
            .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(skill.get()))
            .orElse(false);
    }

    public static boolean has(Player player, String fieldName) {
        return has(getSharedAttachment(player), fieldName);
    }

    private static boolean has(ExpandedAttachmentHolders attachment, String fieldName) {
        try {
            java.lang.reflect.Field field = ExpandedAttachmentHolders.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(attachment);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            LOGGER.warn("Field '{}' is not a boolean type", fieldName);
            return false;
        } catch (NoSuchFieldException e) {
            LOGGER.warn("Attachment field '{}' not found", fieldName);
            return false;
        } catch (IllegalAccessException e) {
            LOGGER.error("Cannot access attachment field '{}'", fieldName, e);
            return false;
        }
    }
}
