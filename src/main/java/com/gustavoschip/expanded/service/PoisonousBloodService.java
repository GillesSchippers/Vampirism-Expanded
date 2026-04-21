package com.gustavoschip.expanded.service;

import com.gustavoschip.expanded.skill.ModSkills;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import static com.gustavoschip.expanded.attachment.ModAttachments.POISONOUS_BLOOD_ATTACHMENT;
import static de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler;

public final class PoisonousBloodService {
    public static final int POISONOUS_BLOOD_EFFECT_DURATION_TICKS = 60;
    private static final Logger LOGGER = LogUtils.getLogger();

    private PoisonousBloodService() {
    }

    public static boolean hasPoisonousBlood(Player player) {
        return player.getData(POISONOUS_BLOOD_ATTACHMENT);
    }

    public static boolean hasPoisonousBlood(ServerPlayer player) {
        if (!canSyncAttachment(player)) {
            return false;
        }
        return hasPoisonousBlood((Player) player);
    }

    public static void setPoisonousBlood(ServerPlayer player, boolean poisonous) {
        if (!canSyncAttachment(player)) {
            LOGGER.debug("Deferred poisonous blood update for {} until login sync", player.getName().getString());
            return;
        }

        if (hasPoisonousBlood(player) == poisonous) {
            LOGGER.debug("Poisonous blood already {} for {}", poisonous, player.getName().getString());
            return;
        }

        player.setData(POISONOUS_BLOOD_ATTACHMENT, poisonous);
        LOGGER.debug("Set poisonous blood for {} to {}", player.getName().getString(), poisonous);
    }

    public static boolean hasPoisonousBloodSkill(ServerPlayer player) {
        return factionPlayerHandler(player)
                .getCurrentFactionPlayer()
                .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(ModSkills.POISONOUS_BLOOD.get()))
                .orElse(false);
    }

    public static void syncFromHunterSkill(ServerPlayer player) {
        setPoisonousBlood(player, hasPoisonousBloodSkill(player));
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player.connection != null;
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

