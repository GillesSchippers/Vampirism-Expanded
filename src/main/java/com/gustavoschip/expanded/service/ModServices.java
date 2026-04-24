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

import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;

import static de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler;

@SuppressWarnings("unused")
public abstract class ModServices {
    protected ModServices() {
    }

    public static void register(IEventBus modEventBus) {
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player != null;
    }

    protected static boolean hasBooleanAttachment(Player player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment) {
        return player.getData(attachment);
    }

    protected static boolean hasBooleanAttachment(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment) {
        if (!canSyncAttachment(player)) {
            return false;
        }
        return hasBooleanAttachment((Player) player, attachment);
    }

    protected static boolean setBooleanAttachment(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment, boolean value, String label, Logger logger) {
        if (!canSyncAttachment(player)) {
            logger.debug("Deferred {} update for {} until login sync", label, player.getName().getString());
            return false;
        }
        if (hasBooleanAttachment(player, attachment) == value) {
            return false;
        }

        player.setData(attachment, value);
        logger.debug("Set {} for {} to {}", label, player.getName().getString(), value);
        return true;
    }

    protected static boolean hasSkillEnabled(ServerPlayer player, DeferredHolder<ISkill<?>, ? extends ISkill<? extends IFactionPlayer<?>>> skill) {
        return factionPlayerHandler(player)
                .getCurrentFactionPlayer()
                .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(skill.get()))
                .orElse(false);
    }
}

