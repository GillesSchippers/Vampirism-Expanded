package com.gustavoschip.expanded.service.skill;

import com.gustavoschip.expanded.attachment.holder.SkillAttachmentHolders;
import com.gustavoschip.expanded.service.ModServices;
import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public final class PoisonousBloodService extends ModServices {
    public static final int POISONOUS_BLOOD_EFFECT_DURATION_TICKS = 60;
    private static final Logger LOGGER = LogUtils.getLogger();

    private PoisonousBloodService() {
    }

    public static boolean hasPoisonousBlood(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT);
    }

    public static boolean hasPoisonousBlood(ServerPlayer player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT);
    }

    public static void setPoisonousBlood(ServerPlayer player, boolean poisonous) {
        setBooleanAttachment(player, SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT, poisonous, "poisonous blood", LOGGER);
    }

    public static boolean hasPoisonousBloodSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.POISONOUS_BLOOD);
    }

    public static void syncFromHunterSkill(ServerPlayer player) {
        setPoisonousBlood(player, hasPoisonousBloodSkill(player));
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return ModServices.canSyncAttachment(player);
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


