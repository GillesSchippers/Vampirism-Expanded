package com.gustavoschip.expanded.service;

import com.gustavoschip.expanded.skill.ModSkills;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import static com.gustavoschip.expanded.attachment.ModAttachments.GARLIC_BLOOD_ATTACHMENT;
import static de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler;

public final class GarlicBloodService {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int GARLIC_EFFECT_DURATION_TICKS = 20 * 10;

    private GarlicBloodService() {
    }

    public static boolean hasGarlicBloodSkill(Player player) {
        return factionPlayerHandler(player)
                .getCurrentFactionPlayer()
                .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(ModSkills.GARLIC_BLOOD.get()))
                .orElse(false);
    }

    public static boolean hasGarlicBlood(Player player) {
        return player.getData(GARLIC_BLOOD_ATTACHMENT);
    }

    public static boolean hasGarlicBlood(ServerPlayer player) {
        return hasGarlicBlood((Player) player);
    }

    public static void setGarlicBlood(ServerPlayer player, boolean garlicBlood) {
        if (hasGarlicBlood(player) == garlicBlood) {
            LOGGER.debug("Garlic blood already {} for {}", garlicBlood, player.getName().getString());
            return;
        }

        player.setData(GARLIC_BLOOD_ATTACHMENT, garlicBlood);
        LOGGER.debug("Set garlic blood for {} to {}", player.getName().getString(), garlicBlood);
    }

    public static void syncFromHunterSkill(ServerPlayer player) {
        setGarlicBlood(player, hasGarlicBloodSkill(player));
    }

    public static boolean isGarlicBloodTarget(Entity entity) {
        return entity instanceof Player player && hasGarlicBlood(player);
    }

    public static void applyGarlicEffect(ServerPlayer vampire, ServerPlayer sourcePlayer) {
        if (!isGarlicBloodTarget(sourcePlayer)) {
            return;
        }

        vampire.addEffect(new MobEffectInstance(ModEffects.GARLIC, GARLIC_EFFECT_DURATION_TICKS));
        LOGGER.debug("Applied garlic to {} after drinking from {}", vampire.getName().getString(), sourcePlayer.getName().getString());
    }

    public static void handlePlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        if (event.getAmount() <= 0) {
            return;
        }

        if (!(event.getVampire().asEntity() instanceof ServerPlayer vampire)) {
            return;
        }

        Entity source = event.getBloodSource().getEntity().orElse(null);
        if (!(source instanceof ServerPlayer sourcePlayer)) {
            return;
        }

        applyGarlicEffect(vampire, sourcePlayer);
    }
}

