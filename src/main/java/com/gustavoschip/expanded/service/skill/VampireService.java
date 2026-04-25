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
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public class VampireService extends ModServices {

    private static final ResourceLocation SUNDAMAGE_REDUCTION_ID = fromNamespaceAndPath(MOD_ID, "sun_damage_reduction");
    private static final ResourceLocation BLOOD_EXHAUSTION_REDUCTION_ID = fromNamespaceAndPath(MOD_ID, "blood_exhaustion_reduction");
    private static final double SUNDAMAGE_REDUCTION_MODIFIER = -0.5D;
    private static final double BLOOD_EXHAUSTION_REDUCTION_MODIFIER = -0.25D;
    private static final float FLIGHT_SPEED_MULTIPLIER = 1.5F;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void setBatSpeed(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.BAT_SPEED_ATTACHMENT, enabled, "Bat Speed", LOGGER);

        if (enabled) applyBatFlightBonuses(player, true);
    }

    public static void setBatArmor(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.BAT_ARMOR_ATTACHMENT, enabled, "Bat Armor", LOGGER);
    }

    public static void setBatLiquid(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.BAT_LIQUID_ATTACHMENT, enabled, "Bat Liquid", LOGGER);
    }

    public static void setVampiricConstitution(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.VAMPIRIC_CONSTITUTION_ATTACHMENT, enabled, "Vampiric Constitution", LOGGER);
        handleVampiricConstitutionStats(player, enabled);
    }

    public static void setDayWalker(ServerPlayer player, boolean enabled) {
        setBooleanAttachment(player, SkillAttachmentHolders.DAY_WALKER_ATTACHMENT, enabled, "Day Walker", LOGGER);
        handleDayWalkerStats(player, enabled);
    }

    public static boolean hasBatSpeedSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.BAT_SPEED);
    }

    public static boolean hasBatArmorSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.BAT_ARMOR);
    }

    public static boolean hasBatLiquidSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.BAT_LIQUID);
    }

    public static boolean hasVampiricConstitutionSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.VAMPIRIC_CONSTITUTION);
    }

    public static boolean hasDayWalkerSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.DAY_WALKER);
    }

    public static void syncFromVampireSkill(ServerPlayer player) {
        setBatSpeed(player, hasBatSpeedSkill(player));
        setBatArmor(player, hasBatArmorSkill(player));
        setBatLiquid(player, hasBatLiquidSkill(player));
        setVampiricConstitution(player, hasVampiricConstitutionSkill(player));
        setDayWalker(player, hasDayWalkerSkill(player));
    }

    public static boolean hasBatSpeed(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.BAT_SPEED_ATTACHMENT);
    }

    public static boolean hasBatArmor(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.BAT_ARMOR_ATTACHMENT);
    }

    public static boolean hasBatLiquid(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.BAT_LIQUID_ATTACHMENT);
    }

    public static boolean hasVampiricConstitution(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.VAMPIRIC_CONSTITUTION_ATTACHMENT);
    }

    public static boolean hasDayWalker(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.DAY_WALKER_ATTACHMENT);
    }

    public static void handleVampiricConstitutionStats(ServerPlayer player, boolean enabled) {
        AttributeInstance bloodExhaustion = player.getAttribute(ModAttributes.BLOOD_EXHAUSTION);

        if (bloodExhaustion == null) return;

        if (enabled) {
            replaceModifier(bloodExhaustion, BLOOD_EXHAUSTION_REDUCTION_ID, BLOOD_EXHAUSTION_REDUCTION_MODIFIER);
        } else {
            bloodExhaustion.removeModifier(BLOOD_EXHAUSTION_REDUCTION_ID);
        }
    }

    public static void handleDayWalkerStats(ServerPlayer player, boolean enabled) {
        AttributeInstance sunDamage = player.getAttribute(ModAttributes.SUNDAMAGE);

        if (sunDamage == null) return;

        if (enabled) {
            replaceModifier(sunDamage, SUNDAMAGE_REDUCTION_ID, SUNDAMAGE_REDUCTION_MODIFIER);
        } else {
            sunDamage.removeModifier(SUNDAMAGE_REDUCTION_ID);
        }
    }

    public static boolean isBatActive(Player player) {
        return VampirePlayer.get(player).getActionHandler().isActionActive(VampireActions.BAT.get());
    }

    public static boolean canUseBatModeInLiquids(Player player) {
        return hasBatLiquid(player);
    }

    public static boolean shouldPreventSwimming(Player player) {
        return isBatActive(player) && hasBatSpeedFlight(player);
    }

    public static void onBatActivated(ServerPlayer player) {
        applyBatFlightBonuses(player, false);
        player.setSwimming(false);
    }

    private static void replaceModifier(AttributeInstance attribute, ResourceLocation id, double amount) {
        AttributeModifier current = attribute.getModifier(id);
        if (current != null && current.amount() == amount && current.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            return;
        }
        if (current != null) {
            attribute.removeModifier(id);
        }
        attribute.addPermanentModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    private static void applyBatFlightBonuses(ServerPlayer player, boolean requireBatActive) {
        if (!hasBatSpeedFlight(player) || (requireBatActive && !isBatActive(player))) {
            return;
        }

        setFlightSpeedBuff(player);
    }

    private static boolean hasBatSpeedFlight(Player player) {
        if (hasBatSpeed(player)) {
            return true;
        }

        return player instanceof ServerPlayer serverPlayer && hasBatSpeedSkill(serverPlayer);
    }

    private static void setFlightSpeedBuff(Player player) {
        Abilities abilities = player.getAbilities();
        float speed = abilities.getFlyingSpeed() * FLIGHT_SPEED_MULTIPLIER;
        LOGGER.debug("Setting flight speed for {} to {}", player.getName().getString(), speed);
        abilities.setFlyingSpeed(speed);
        player.onUpdateAbilities();
    }
}
