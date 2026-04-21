package com.gustavoschip.expanded.event;

import com.gustavoschip.expanded.service.GarlicBloodService;
import com.gustavoschip.expanded.service.PoisonousBloodService;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class VampirismEvents {

    @SubscribeEvent
    public void onPlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        PoisonousBloodService.handlePlayerDrinkBlood(event);
        GarlicBloodService.handlePlayerDrinkBlood(event);
    }
}
