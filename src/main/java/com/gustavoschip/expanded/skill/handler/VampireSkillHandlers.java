package com.gustavoschip.expanded.skill.handler;

import com.gustavoschip.expanded.service.skill.AdvancedFlightService;
import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.function.Consumer;

public final class VampireSkillHandlers {
    private static final Logger LOGGER = LogUtils.getLogger();

    private VampireSkillHandlers() {
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> vampiricGroundingToggle(boolean vampiricGrounding) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                if (!VampiricGroundingService.canSyncAttachment(serverPlayer)) {
                    LOGGER.debug("Deferred vampiric grounding toggle {} for {} until login sync", vampiricGrounding, serverPlayer.getName().getString());
                    return;
                }
                LOGGER.debug("Toggling vampiric grounding to {} for {}", vampiricGrounding, serverPlayer.getName().getString());
                VampiricGroundingService.setVampiricGrounding(serverPlayer, vampiricGrounding);
            }
        };
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> advancedFlightToggle(boolean advancedFlight) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                if (!AdvancedFlightService.canSyncAttachment(serverPlayer)) {
                    LOGGER.debug("Deferred advanced flight toggle {} for {} until login sync", advancedFlight, serverPlayer.getName().getString());
                    return;
                }
                LOGGER.debug("Toggling advanced flight to {} for {}", advancedFlight, serverPlayer.getName().getString());
                AdvancedFlightService.setAdvancedFlight(serverPlayer, advancedFlight);
            }
        };
    }
}

