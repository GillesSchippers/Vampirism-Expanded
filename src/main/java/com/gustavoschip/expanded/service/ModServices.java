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

    protected static boolean canSyncAttachment(ServerPlayer player) {
        return player != null && player.connection != null;
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

    protected static void setBooleanAttachment(ServerPlayer player, DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> attachment, boolean value, String label, Logger logger) {
        if (!canSyncAttachment(player)) {
            logger.debug("Deferred {} update for {} until login sync", label, player.getName().getString());
            return;
        }
        if (hasBooleanAttachment(player, attachment) == value) {
            return;
        }

        player.setData(attachment, value);
        logger.debug("Set {} for {} to {}", label, player.getName().getString(), value);
    }

    protected static boolean hasSkillEnabled(ServerPlayer player, DeferredHolder<ISkill<?>, ? extends ISkill<? extends IFactionPlayer<?>>> skill) {
        return factionPlayerHandler(player)
                .getCurrentFactionPlayer()
                .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(skill.get()))
                .orElse(false);
    }
}

