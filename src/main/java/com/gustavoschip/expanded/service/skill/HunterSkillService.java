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

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

import com.gustavoschip.expanded.attachment.holder.SkillAttachmentHolders;
import com.gustavoschip.expanded.service.ModServices;
import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public class HunterSkillService extends ModServices {

    private static final ResourceLocation ARMOR_ADDITION_ID = fromNamespaceAndPath(MOD_ID, "armor_addition");
    private static final ResourceLocation SCALE_ADDITION_ID = fromNamespaceAndPath(MOD_ID, "scale_addition");
    private static final double ARMOR_ADDITION_MODIFIER = 10.0D;
    private static final double SCALE_ADDITION_MODIFIER = 0.1D;
    private static final int POISONOUS_BLOOD_EFFECT_DURATION_TICKS = 60;
    private static final int GARLIC_EFFECT_DURATION_TICKS = 200;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void setInnateToughness(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.INNATE_TOUGHNESS_ATTACHMENT, enabled, "Innate Toughness");
        handleInnateToughnessStats(player, enabled);
    }

    public static void setHuntersGrowth(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.HUNTERS_GROWTH_ATTACHMENT, enabled, "Hunter's Growth");
        handleHuntersGrowthStats(player, enabled);
    }

    public static void setPreparedHunt(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.PREPARED_HUNT_ATTACHMENT, enabled, "Prepared Hunt");
    }

    public static void setPoisonousBlood(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT, enabled, "Poisonous Blood");
    }

    public static void setGarlicBlood(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.GARLIC_BLOOD_ATTACHMENT, enabled, "Garlic Blood");
    }

    public static boolean hasInnateToughnessSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.INNATE_TOUGHNESS);
    }

    public static boolean hasHuntersGrowthSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.HUNTERS_GROWTH);
    }

    public static boolean hasPreparedHuntSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.PREPARED_HUNT);
    }

    public static boolean hasGarlicBloodSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.GARLIC_BLOOD);
    }

    public static boolean hasPoisonousBloodSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.POISONOUS_BLOOD);
    }

    public static boolean hasInnateToughness(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.INNATE_TOUGHNESS_ATTACHMENT);
    }

    public static boolean hasHuntersGrowth(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.HUNTERS_GROWTH_ATTACHMENT);
    }

    public static boolean hasPreparedHunt(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.PREPARED_HUNT_ATTACHMENT);
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

    public static void handleInnateToughnessStats(ServerPlayer player, boolean enabled) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);

        if (armor == null) return;

        if (enabled) {
            replaceModifier(armor, ARMOR_ADDITION_ID, ARMOR_ADDITION_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        } else {
            armor.removeModifier(ARMOR_ADDITION_ID);
        }
    }

    public static void handleHuntersGrowthStats(ServerPlayer player, boolean enabled) {
        AttributeInstance scale = player.getAttribute(Attributes.SCALE);

        if (scale == null) return;

        if (enabled) {
            replaceModifier(scale, SCALE_ADDITION_ID, SCALE_ADDITION_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        } else {
            scale.removeModifier(SCALE_ADDITION_ID);
        }
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

    private static void applyGarlicEffect(ServerPlayer vampire, ServerPlayer sourcePlayer) {
        if (!isGarlicBloodTarget(sourcePlayer)) {
            return;
        }

        vampire.addEffect(new MobEffectInstance(ModEffects.GARLIC, GARLIC_EFFECT_DURATION_TICKS));
    }

    private static void replaceModifier(AttributeInstance attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        AttributeModifier current = attribute.getModifier(id);
        if (current != null && current.amount() == amount && current.operation() == operation) {
            return;
        }
        if (current != null) {
            attribute.removeModifier(id);
        }
        attribute.addPermanentModifier(new AttributeModifier(id, amount, operation));
    }
}
