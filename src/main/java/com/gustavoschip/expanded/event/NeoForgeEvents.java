package com.gustavoschip.expanded.event;

import com.gustavoschip.expanded.service.AdvancedFlightService;
import com.gustavoschip.expanded.service.GarlicBloodService;
import com.gustavoschip.expanded.service.PoisonousBloodService;
import com.gustavoschip.expanded.service.VampiricGroundingService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class NeoForgeEvents {

    // Redundancy: Might need to remove overhead
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PoisonousBloodService.syncFromHunterSkill(player);
            GarlicBloodService.syncFromHunterSkill(player);
            VampiricGroundingService.syncFromVampireSkill(player);
            AdvancedFlightService.syncFromVampireSkill(player);
        }
    }
}

