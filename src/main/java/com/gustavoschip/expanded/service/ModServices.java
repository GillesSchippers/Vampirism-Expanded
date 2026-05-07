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

    /**
     * Logger used by the shared service helpers for sync and debug diagnostics.
     */

    private static final Logger LOGGER = LogUtils.getLogger();
    /**
     * System property that enables debug logging without a debugger attached.
     */

    private static final String DEBUG_PROPERTY = "expanded.debug";

    /**
     * Returns whether debug logging should be emitted for this mod.
     * <p>
     * Debug output stays quiet during normal play and only activates when a debugger
     * is attached, or when the optional -Dexpanded.debug=true override is supplied.
     */
    public static boolean shouldLogDebug() {
        return LOGGER.isDebugEnabled() && (isDebuggerAttached() || Boolean.getBoolean(DEBUG_PROPERTY));
    }

    /**
     * Detects the standard JVM debug-agent arguments used by JDWP-based debuggers.
     */
    private static boolean isDebuggerAttached() {
        return ManagementFactory.getRuntimeMXBean()
            .getInputArguments()
            .stream()
            .anyMatch(arg -> arg.contains("jdwp") || arg.contains("Xrunjdwp"));
    }

    /**
     * Returns whether the supplied server player is still valid for attachment sync.
     */

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player != null && player.server != null && player.connection != null && !player.hasDisconnected() && !player.isRemoved();
    }

    /**
     * Returns whether the supplied server player is still valid for attachment sync.
     */

    public static boolean canSyncAttachment(LocalPlayer player) {
        return player != null && player.connection != null && !player.isRemoved();
    }

    /**
     * Returns the shared Expanded attachment, creating a fresh copy when needed.
     */

    public static @NotNull ExpandedAttachmentHolders getSharedAttachment(@NotNull Player player) {
        if (player.hasData(ModAttachments.SHARED_ATTACHMENT)) {
            return player.getData(ModAttachments.SHARED_ATTACHMENT);
        }
        return new ExpandedAttachmentHolders();
    }

    /**
     * Syncs the shared Expanded attachment back to the server player.
     */

    public static void setSharedAttachment(ServerPlayer player, ExpandedAttachmentHolders data) {
        setSharedAttachment(player, data, 0);
    }

    /**
     * Syncs the shared Expanded attachment back to the server player.
     */

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
    }

    /**
     * Returns whether the supplied skill is currently enabled for the player's faction.
     */

    public static boolean hasSkill(ServerPlayer player, DeferredHolder<ISkill<?>, ? extends ISkill<? extends IFactionPlayer<?>>> skill) {
        return factionPlayerHandler(player)
            .getCurrentFactionPlayer()
            .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(skill.get()))
            .orElse(false);
    }

    /**
     * Generic access method for reading boolean attachment fields using reflection.
     */

    public static boolean has(Player player, String fieldName) {
        return has(getSharedAttachment(player), fieldName);
    }

    /**
     * Generic access method for reading boolean attachment fields using reflection.
     */

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
