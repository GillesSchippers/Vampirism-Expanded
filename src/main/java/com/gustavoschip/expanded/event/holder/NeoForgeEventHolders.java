package com.gustavoschip.expanded.event.holder;

import com.gustavoschip.expanded.service.skill.AdvancedFlightService;
import com.gustavoschip.expanded.service.skill.GarlicBloodService;
import com.gustavoschip.expanded.service.skill.PoisonousBloodService;
import com.gustavoschip.expanded.service.skill.VampiricGroundingService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class NeoForgeEventHolders {
    public static final NeoForgeEventHolders INSTANCE = new NeoForgeEventHolders();

    private NeoForgeEventHolders() {
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PoisonousBloodService.syncFromHunterSkill(player);
        GarlicBloodService.syncFromHunterSkill(player);
        VampiricGroundingService.syncFromVampireSkill(player);
        AdvancedFlightService.syncFromVampireSkill(player);
    }

    @SubscribeEvent
    public void onLivingKnockBack(LivingKnockBackEvent event) {
        VampiricGroundingService.handleLivingKnockback(event);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        VampiricGroundingService.clearSunDisorientation(player);
    }
}
