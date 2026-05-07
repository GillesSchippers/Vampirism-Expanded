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

import com.gustavoschip.expanded.attachment.holder.ExpandedAttachmentHolders;
import com.gustavoschip.expanded.service.ModServices;
import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Applies vampire skill state to attachments, attributes, bat form, and sun-damage side
 * effects.
 */

public class VampireSkillService extends ModServices {

    /**
     * Resource location used to identify the sun-damage reduction modifier.
     */

    private static final ResourceLocation SUNDAMAGE_REDUCTION_ID = fromNamespaceAndPath(MOD_ID, "sun_damage_reduction");
    /**
     * Resource location used to identify the blood-exhaustion reduction modifier.
     */

    private static final ResourceLocation BLOOD_EXHAUSTION_REDUCTION_ID = fromNamespaceAndPath(MOD_ID, "blood_exhaustion_reduction");
    /**
     * Attribute modifier amount applied to sun damage when Day Walker is enabled.
     */

    private static final double SUNDAMAGE_REDUCTION_MODIFIER = -0.5D;
    /**
     * Attribute modifier amount applied to blood exhaustion when Vampiric Constitution is
     * enabled.
     */

    private static final double BLOOD_EXHAUSTION_REDUCTION_MODIFIER = -0.25D;
    /**
     * Multiplier applied to flying speed while bat flight bonuses are active.
     */

    private static final float FLIGHT_SPEED_MULTIPLIER = 1.5F;
    /**
     * Logger used for vampire-skill side effect debug messages.
     */

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Updates the cached bat speed flag and reapplies flight bonuses when appropriate.
     */

    public static void setBatSpeed(ServerPlayer player, boolean enabled) {
        ExpandedAttachmentHolders data = getSharedAttachment(player);
        data.batSpeed = enabled;
        setSharedAttachment(player, data);

        if (enabled) applyBatFlightBonuses(player, true);
    }

    /**
     * Updates the cached bat armor flag.
     */

    public static void setBatArmor(ServerPlayer player, boolean enabled) {
        ExpandedAttachmentHolders data = getSharedAttachment(player);
        data.batArmor = enabled;
        setSharedAttachment(player, data);
    }

    /**
     * Updates the cached bat liquid flag.
     */

    public static void setBatLiquid(ServerPlayer player, boolean enabled) {
        ExpandedAttachmentHolders data = getSharedAttachment(player);
        data.batLiquid = enabled;
        setSharedAttachment(player, data);
    }

    /**
     * Updates the cached vampiric constitution flag and reapplies the attribute bonus.
     */

    public static void setVampiricConstitution(ServerPlayer player, boolean enabled) {
        ExpandedAttachmentHolders data = getSharedAttachment(player);
        data.vampiricConstitution = enabled;
        setSharedAttachment(player, data);
        handleVampiricConstitutionStats(player, enabled);
    }

    /**
     * Updates the cached day walker flag and reapplies the sunlight resistance bonus.
     */

    public static void setDayWalker(ServerPlayer player, boolean enabled) {
        ExpandedAttachmentHolders data = getSharedAttachment(player);
        data.dayWalker = enabled;
        setSharedAttachment(player, data);
        handleDayWalkerStats(player, enabled);
    }

    /**
     * Applies or removes the blood exhaustion reduction modifier.
     */

    public static void handleVampiricConstitutionStats(@NotNull ServerPlayer player, boolean enabled) {
        AttributeInstance bloodExhaustion = player.getAttribute(ModAttributes.BLOOD_EXHAUSTION);

        if (bloodExhaustion == null) return;

        if (enabled) {
            replaceModifier(bloodExhaustion, BLOOD_EXHAUSTION_REDUCTION_ID, BLOOD_EXHAUSTION_REDUCTION_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        } else {
            bloodExhaustion.removeModifier(BLOOD_EXHAUSTION_REDUCTION_ID);
        }
    }

    /**
     * Applies or removes the sun damage reduction modifier.
     */

    public static void handleDayWalkerStats(@NotNull ServerPlayer player, boolean enabled) {
        AttributeInstance sunDamage = player.getAttribute(ModAttributes.SUNDAMAGE);

        if (sunDamage == null) return;

        if (enabled) {
            replaceModifier(sunDamage, SUNDAMAGE_REDUCTION_ID, SUNDAMAGE_REDUCTION_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        } else {
            sunDamage.removeModifier(SUNDAMAGE_REDUCTION_ID);
        }
    }

    /**
     * Returns whether the player is currently in bat form.
     */

    public static boolean isBatActive(Player player) {
        return VampirePlayer.get(player).getActionHandler().isActionActive(VampireActions.BAT.get());
    }

    /**
     * Returns whether bat form may be used in liquids.
     */

    public static boolean canUseBatModeInLiquids(Player player) {
        return has(player, ExpandedAttachmentHolders.BAT_LIQUID);
    }

    /**
     * Returns whether swimming should be suppressed while bat flight is active.
     */

    public static boolean shouldPreventSwimming(Player player) {
        return isBatActive(player) && hasBatSpeedFlight(player);
    }

    /**
     * Applies bat-flight bonuses immediately after bat form activates.
     */

    public static void onBatActivated(ServerPlayer player) {
        applyBatFlightBonuses(player, false);
        player.setSwimming(false);
    }

    /**
     * Performs the replace modifier operation.
     */

    private static void replaceModifier(@NotNull AttributeInstance attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        AttributeModifier current = attribute.getModifier(id);
        if (current != null && current.amount() == amount && current.operation() == operation) {
            return;
        }
        if (current != null) {
            attribute.removeModifier(id);
        }
        attribute.addPermanentModifier(new AttributeModifier(id, amount, operation));
    }

    /**
     * Applies bat flight bonuses.
     */

    private static void applyBatFlightBonuses(ServerPlayer player, boolean requireBatActive) {
        if (!hasBatSpeedFlight(player) || (requireBatActive && !isBatActive(player))) {
            return;
        }

        setFlightSpeedBuff(player);
    }

    /**
     * Returns whether has bat speed flight.
     */

    private static boolean hasBatSpeedFlight(Player player) {
        if (has(player, ExpandedAttachmentHolders.BAT_SPEED)) {
            return true;
        }

        return player instanceof ServerPlayer serverPlayer && hasSkill(serverPlayer, SkillHolders.BAT_SPEED);
    }

    /**
     * Sets flight speed buff.
     */

    private static void setFlightSpeedBuff(@NotNull Player player) {
        Abilities abilities = player.getAbilities();
        float speed = abilities.getFlyingSpeed() * FLIGHT_SPEED_MULTIPLIER;
        if (ModServices.shouldLogDebug()) {
            LOGGER.debug("Expanded bat flight speed for {} -> {}", player.getName().getString(), speed);
        }
        abilities.setFlyingSpeed(speed);
        player.onUpdateAbilities();
    }
}
