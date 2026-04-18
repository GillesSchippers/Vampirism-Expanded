package com.gustavoschip.poisonousblood.service;

import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import static com.gustavoschip.poisonousblood.attachment.ModAttachments.POISONOUS_BLOOD_ATTACHMENT;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public final class PoisonousBloodService {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation HUNTER = fromNamespaceAndPath("vampirism", "hunter");
    public static final int POISONOUS_BLOOD_EFFECT_DURATION_TICKS = 60;

    private PoisonousBloodService() {
    }

    public static boolean hasPoisonousBlood(Player player) {
        return player.getData(POISONOUS_BLOOD_ATTACHMENT);
    }

    public static boolean hasPoisonousBlood(ServerPlayer player) {
        return hasPoisonousBlood((Player) player);
    }

    public static void setPoisonousBlood(ServerPlayer player, boolean poisonous) {
        if (hasPoisonousBlood(player) == poisonous) {
            LOGGER.debug("Poisonous blood already {} for {}", poisonous, player.getName().getString());
            return;
        }

        player.setData(POISONOUS_BLOOD_ATTACHMENT, poisonous);
        LOGGER.debug("Set poisonous blood for {} to {}", player.getName().getString(), poisonous);
    }

    public static boolean isPoisonousFaction(IPlayableFaction<?> faction) {
        return faction != null && HUNTER.equals(faction.getID());
    }

    public static void syncFromFaction(ServerPlayer player, IPlayableFaction<?> faction) {
        LOGGER.debug("Syncing poisonous blood state from faction for {} ({})", player.getName().getString(), faction != null ? faction.getID() : "None");
        setPoisonousBlood(player, isPoisonousFaction(faction));
    }

    public static void syncFromFaction(IFactionPlayerHandler handler) {
        if (!(handler.asEntity() instanceof ServerPlayer player)) {
            return;
        }

        syncFromFaction(player, handler.getCurrentFaction());
    }

    public static boolean isPoisonousBloodTarget(Entity entity) {
        return entity instanceof Player player && hasPoisonousBlood(player);
    }

    public static boolean interruptPoisonousBiteAttempt(ServerPlayer vampire, Entity target) {
        if (!isPoisonousBloodTarget(target)) {
            return false;
        }

        VampirePlayer.get(vampire).endFeeding(true);
        vampire.addEffect(new MobEffectInstance(ModEffects.POISON, POISONOUS_BLOOD_EFFECT_DURATION_TICKS));
        return true;
    }

    public static void handlePlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        if (!(event.getVampire().asEntity() instanceof ServerPlayer vampire)) {
            return;
        }

        Entity source = event.getBloodSource().getEntity().orElse(null);
        if (source == null || !interruptPoisonousBiteAttempt(vampire, source)) {
            return;
        }

        // Fallback safety: if a poisonous source somehow reaches the drink event, prevent blood gain.
        event.setAmount(0);
        event.setSaturationModifier(0);
        event.setUseRemaining(false);
    }
}

