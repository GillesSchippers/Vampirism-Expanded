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

