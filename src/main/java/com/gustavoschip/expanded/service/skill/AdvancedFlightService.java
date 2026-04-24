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
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public final class AdvancedFlightService extends ModServices {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float ADVANCED_FLIGHT_SPEED_MULTIPLIER = 1.5F;

    private AdvancedFlightService() {
    }

    public static boolean hasAdvancedFlight(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.ADVANCED_FLIGHT_ATTACHMENT);
    }

    public static boolean hasAdvancedFlightSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.ADVANCED_FLIGHT);
    }

    public static void setAdvancedFlight(ServerPlayer player, boolean advancedFlight) {
        if (!setBooleanAttachment(player, SkillAttachmentHolders.ADVANCED_FLIGHT_ATTACHMENT, advancedFlight, "advanced flight", LOGGER)) {
            return;
        }

        if (advancedFlight) {
            applyAdvancedFlight(player);
        }
    }

    public static boolean isBatActive(Player player) {
        return VampirePlayer.get(player).getActionHandler().isActionActive(VampireActions.BAT.get());
    }

    public static void syncFromVampireSkill(ServerPlayer player) {
        setAdvancedFlight(player, hasAdvancedFlightSkill(player));
    }

    public static void onBatActivated(ServerPlayer player) {
        applyBatFlightBonuses(player, false);
        player.setSwimming(false);
    }

    public static void applyAdvancedFlight(ServerPlayer player) {
        applyBatFlightBonuses(player, true);
    }

    public static boolean canUseBatModeInLiquids(Player player) {
        return hasAdvancedFlightEffect(player);
    }

    public static boolean shouldPreventSwimming(Player player) {
        return isBatActive(player) && hasAdvancedFlightEffect(player);
    }

    private static void applyBatFlightBonuses(ServerPlayer player, boolean requireBatActive) {
        if (!hasAdvancedFlightEffect(player) || (requireBatActive && !isBatActive(player))) {
            return;
        }

        setFlightSpeed(player);
    }


    private static boolean hasAdvancedFlightEffect(Player player) {
        if (hasAdvancedFlight(player)) {
            return true;
        }

        return player instanceof ServerPlayer serverPlayer && hasAdvancedFlightSkill(serverPlayer);
    }


    private static void setFlightSpeed(Player player) {
        Abilities abilities = player.getAbilities();
        float speed = abilities.getFlyingSpeed() * ADVANCED_FLIGHT_SPEED_MULTIPLIER;
        LOGGER.debug("Setting flight speed for {} to {}", player.getName().getString(), speed);
        abilities.setFlyingSpeed(speed);
        player.onUpdateAbilities();
    }
}





