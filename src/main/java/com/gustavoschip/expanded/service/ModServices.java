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

import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public abstract class ModServices {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected ModServices() {}

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player != null && player.connection != null && !player.hasDisconnected();
    }

    protected static boolean hasBooleanAttachment(Player player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment) {
        if (player instanceof ServerPlayer sp && !canSyncAttachment(sp)) {
            LOGGER.debug("Cannot check {} for {} until login sync", attachment.getId().getPath(), player.getName().getString());
            return false;
        }

        return player.hasData(attachment) && player.getData(attachment);
    }

    protected static boolean hasBooleanAttachment(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment) {
        if (!canSyncAttachment(player)) {
            LOGGER.debug("Cannot check {} for {} until login sync", attachment.getId().getPath(), player.getName().getString());
            return false;
        }
        return hasBooleanAttachment((Player) player, attachment);
    }

    protected static void setBooleanAttachment(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment, boolean value, String label) {
        setBooleanAttachment(player, attachment, value, label, 0);
    }

    private static void setBooleanAttachment(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment, boolean value, String label, int attempts) {
        if (!canSyncAttachment(player)) {
            if (attempts >= 40) {
                LOGGER.warn("Failed to sync {} for {} after retries", label, player.getName().getString());
                return;
            }

            LOGGER.debug("Deferred {} toggle {} for {} (attempt {})", label, value, player.getName().getString(), attempts + 1);

            player.server.tell(new net.minecraft.server.TickTask(player.server.getTickCount() + 1, () -> setBooleanAttachment(player, attachment, value, label, attempts + 1)));

            return;
        }

        if (hasBooleanAttachment(player, attachment) == value) {
            LOGGER.debug("{} for {} already {}", label, player.getName().getString(), value);
            return;
        }

        player.setData(attachment, value);
        LOGGER.debug("Set {} for {} to {}", label, player.getName().getString(), value);
    }

    protected static boolean hasSkillEnabled(ServerPlayer player, DeferredHolder<ISkill<?>, ? extends ISkill<? extends IFactionPlayer<?>>> skill) {
        return factionPlayerHandler(player)
            .getCurrentFactionPlayer()
            .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(skill.get()))
            .orElse(false);
    }
}
