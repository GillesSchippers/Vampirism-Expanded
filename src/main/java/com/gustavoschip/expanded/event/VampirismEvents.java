package com.gustavoschip.expanded.event;

import com.gustavoschip.expanded.service.AdvancedFlightService;
import com.gustavoschip.expanded.service.GarlicBloodService;
import com.gustavoschip.expanded.service.PoisonousBloodService;
import com.gustavoschip.expanded.service.VampiricGroundingService;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class VampirismEvents {

    @SubscribeEvent
    public void onPlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        PoisonousBloodService.handlePlayerDrinkBlood(event);
        GarlicBloodService.handlePlayerDrinkBlood(event);
    }

    @SubscribeEvent
    public void onActionActivated(ActionEvent.ActionActivatedEvent event) {
        VampiricGroundingService.handleBatActionActivated(event);
    }

    @SubscribeEvent
    public void onActionUpdateEvent(ActionEvent.ActionUpdateEvent event) {
        AdvancedFlightService.handleBatActionActivated(event);
    }
}
