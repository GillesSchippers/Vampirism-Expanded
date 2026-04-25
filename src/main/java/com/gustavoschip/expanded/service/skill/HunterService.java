/*
 * MIT License
 *
 * Copyright (c) 2026 Gilles Schippers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

@SuppressWarnings("unused")
public class HunterService extends ModServices {

    public static final int POISONOUS_BLOOD_EFFECT_DURATION_TICKS = 60;
    public static final int GARLIC_EFFECT_DURATION_TICKS = 200;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void setPoisonousBlood(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT, enabled, "Poisonous Blood", LOGGER);
    }

    public static void setGarlicBlood(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.GARLIC_BLOOD_ATTACHMENT, enabled, "Garlic Blood", LOGGER);
    }

    public static boolean hasGarlicBloodSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.GARLIC_BLOOD);
    }

    public static boolean hasPoisonousBloodSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.POISONOUS_BLOOD);
    }

    public static void syncFromHunterSkill(ServerPlayer player) {
        setPoisonousBlood(player, hasPoisonousBloodSkill(player));
        setGarlicBlood(player, hasGarlicBloodSkill(player));
    }

    public static boolean hasPoisonousBlood(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT);
    }

    public static boolean hasGarlicBlood(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.GARLIC_BLOOD_ATTACHMENT);
    }

    public static boolean isPoisonousBloodTarget(Entity entity) {
        return entity instanceof Player player && hasPoisonousBlood(player);
    }

    public static boolean isGarlicBloodTarget(Entity entity) {
        return entity instanceof Player player && hasGarlicBlood(player);
    }

    public static boolean interruptPoisonousBiteAttempt(ServerPlayer vampire, Entity target) {
        if (!isPoisonousBloodTarget(target)) {
            return false;
        }

        VampirePlayer.get(vampire).endFeeding(true);
        vampire.addEffect(new MobEffectInstance(ModEffects.POISON, POISONOUS_BLOOD_EFFECT_DURATION_TICKS));
        return true;
    }

    public static void applyGarlicEffect(ServerPlayer vampire, ServerPlayer sourcePlayer) {
        if (!isGarlicBloodTarget(sourcePlayer)) {
            return;
        }

        vampire.addEffect(new MobEffectInstance(ModEffects.GARLIC, GARLIC_EFFECT_DURATION_TICKS));
    }

    public static void handlePlayerDrinkBlood(BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        if (event.getAmount() <= 0) {
            return;
        }

        if (!(event.getVampire().asEntity() instanceof ServerPlayer vampire)) {
            return;
        }

        Entity source = event.getBloodSource().getEntity().orElse(null);
        if (source == null) {
            return;
        }

        if (interruptPoisonousBiteAttempt(vampire, source)) {
            event.setAmount(0);
            event.setSaturationModifier(0);
            event.setUseRemaining(false);
            return;
        }

        if (source instanceof ServerPlayer sourcePlayer) {
            applyGarlicEffect(vampire, sourcePlayer);
        }
    }
}
