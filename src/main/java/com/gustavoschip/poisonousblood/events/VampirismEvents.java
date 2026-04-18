package com.gustavoschip.poisonousblood.events;

import com.gustavoschip.poisonousblood.service.PoisonousBloodService;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;

public class VampirismEvents {
    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onFactionChange(PlayerFactionEvent.FactionLevelChanged event) {
        IFactionPlayerHandler handler = event.getPlayer();

        if (!(handler.asEntity() instanceof ServerPlayer player)) return;

        IPlayableFaction<?> oldFaction = event.getOldFaction();
        IPlayableFaction<?> newFaction = handler.getCurrentFaction();

        if (oldFaction != null && oldFaction.equals(newFaction)) return;

        LOGGER.debug("Player {} changed faction from {} to {}", player.getName().getString(), oldFaction != null ? oldFaction.getID() : "None", newFaction != null ? newFaction.getID() : "None");

        PoisonousBloodService.syncFromFaction(player, newFaction);
    }

    @SubscribeEvent
    public void onPlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        PoisonousBloodService.handlePlayerDrinkBlood(event);
    }
}
