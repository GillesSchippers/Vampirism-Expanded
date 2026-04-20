package com.gustavoschip.expanded.skill.handler;

import com.gustavoschip.expanded.service.PoisonousBloodService;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.function.Consumer;

public final class HunterSkillHandlers {
    private static final Logger LOGGER = LogUtils.getLogger();

    private HunterSkillHandlers() {
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> doToggle(boolean toggle) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                return;
            }
        };
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> poisonousBloodToggle(boolean poisonous) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                LOGGER.debug("Toggling poisonous blood to {} for {}", poisonous, serverPlayer.getName().getString());
                PoisonousBloodService.setPoisonousBlood(serverPlayer, poisonous);
                return;
            }
            LOGGER.debug("Skipped poisonous blood toggle {} for non-server entity {}", poisonous, player.asEntity().getName().getString());
        };
    }
}

