package com.gustavoschip.expanded.event.holder;

import com.gustavoschip.expanded.service.skill.AdvancedFlightService;
import com.gustavoschip.expanded.service.skill.GarlicBloodService;
import com.gustavoschip.expanded.service.skill.PoisonousBloodService;
import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import net.neoforged.bus.api.SubscribeEvent;

public final class VampirismEventHolders {
    public static final VampirismEventHolders INSTANCE = new VampirismEventHolders();

    private VampirismEventHolders() {
    }

    @SubscribeEvent
    public void onPlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        PoisonousBloodService.handlePlayerDrinkBlood(event);
        GarlicBloodService.handlePlayerDrinkBlood(event);
    }

    @SubscribeEvent
    public void onActionActivated(ActionEvent.ActionActivatedEvent event) {
        VampiricGroundingService.handleBatActionActivated(event);
        AdvancedFlightService.handleBatActionActivated(event);
    }
}
