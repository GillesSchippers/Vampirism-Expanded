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
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.core.ModRegistries;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public final class AdvancedFlightService extends ModServices {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float ADVANCED_FLIGHT_SPEED_MULTIPLIER = 1.5F;
    private static final ResourceLocation BAT_ARMOR_PENALTY_ID = fromNamespaceAndPath(MOD_ID, "advanced_flight_bat_armor_penalty");
    private static final ResourceLocation BAT_TOUGHNESS_PENALTY_ID = fromNamespaceAndPath(MOD_ID, "advanced_flight_bat_toughness_penalty");

    private AdvancedFlightService() {
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return ModServices.canSyncAttachment(player);
    }

    public static boolean hasAdvancedFlight(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.ADVANCED_FLIGHT_ATTACHMENT);
    }

    public static boolean hasAdvancedFlight(ServerPlayer player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.ADVANCED_FLIGHT_ATTACHMENT);
    }

    public static boolean hasAdvancedFlightSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.ADVANCED_FLIGHT);
    }

    public static void setAdvancedFlight(ServerPlayer player, boolean advancedFlight) {
        if (!canSyncAttachment(player)) {
            LOGGER.debug("Deferred advanced flight update for {} until login sync", player.getName().getString());
            return;
        }

        if (hasAdvancedFlight(player) == advancedFlight) {
            return;
        }

        setBooleanAttachment(player, SkillAttachmentHolders.ADVANCED_FLIGHT_ATTACHMENT, advancedFlight, "advanced flight", LOGGER);
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

    public static void applyAdvancedFlight(ServerPlayer player) {
        if (!hasAdvancedFlight(player) || !isBatActive(player)) {
            return;
        }

        ResourceLocation batActionId = ModRegistries.ACTIONS.getKey(VampireActions.BAT.get());
        if (batActionId == null) {
            LOGGER.debug("Skipped advanced flight adjustment for {} because the bat action id was unavailable", player.getName().getString());
            return;
        }

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance toughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armor != null) {
            armor.removeModifier(batActionId);
            armor.removeModifier(BAT_ARMOR_PENALTY_ID);
        }
        if (toughness != null) {
            toughness.removeModifier(batActionId);
            toughness.removeModifier(BAT_TOUGHNESS_PENALTY_ID);
        }

        setFlightSpeed(player, getAdvancedFlightSpeed());
    }

    public static void handleBatActionActivated(ActionEvent.ActionActivatedEvent event) {
        if (event.getAction() != VampireActions.BAT.get()) {
            return;
        }
        if (!(event.getFactionPlayer() instanceof IVampirePlayer vampirePlayer)) {
            return;
        }
        if (!(vampirePlayer.asEntity() instanceof ServerPlayer player) || !hasAdvancedFlight(player)) {
            return;
        }

        applyAdvancedFlight(player);
    }

    private static void setFlightSpeed(Player player, float speed) {
        player.getAbilities().setFlyingSpeed(speed);
        player.onUpdateAbilities();
    }

    private static float getAdvancedFlightSpeed() {
        return de.teamlapen.vampirism.config.VampirismConfig.BALANCE.vaBatFlightSpeed.get().floatValue() * ADVANCED_FLIGHT_SPEED_MULTIPLIER;
    }
}





