package com.gustavoschip.expanded.service;

import com.gustavoschip.expanded.skill.ModSkills;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
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
import static com.gustavoschip.expanded.attachment.ModAttachments.ADVANCED_FLIGHT_ATTACHMENT;
import static de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public final class AdvancedFlightService {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float ADVANCED_FLIGHT_SPEED_MULTIPLIER = 1.5F;
    private static final ResourceLocation BAT_ARMOR_PENALTY_ID = fromNamespaceAndPath(MOD_ID, "advanced_flight_bat_armor_penalty");
    private static final ResourceLocation BAT_TOUGHNESS_PENALTY_ID = fromNamespaceAndPath(MOD_ID, "advanced_flight_bat_toughness_penalty");

    private AdvancedFlightService() {
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player != null && player.connection != null;
    }

    public static boolean hasAdvancedFlight(Player player) {
        return player.getData(ADVANCED_FLIGHT_ATTACHMENT);
    }

    public static boolean hasAdvancedFlight(ServerPlayer player) {
        if (!canSyncAttachment(player)) {
            return false;
        }
        return hasAdvancedFlight((Player) player);
    }

    public static boolean hasAdvancedFlightSkill(ServerPlayer player) {
        return factionPlayerHandler(player)
                .getCurrentFactionPlayer()
                .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(ModSkills.ADVANCED_FLIGHT.get()))
                .orElse(false);
    }

    public static void setAdvancedFlight(ServerPlayer player, boolean advancedFlight) {
        if (!canSyncAttachment(player)) {
            LOGGER.debug("Deferred advanced flight update for {} until login sync", player.getName().getString());
            return;
        }

        if (hasAdvancedFlight(player) == advancedFlight) {
            return;
        }

        player.setData(ADVANCED_FLIGHT_ATTACHMENT, advancedFlight);
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
        if (vampirePlayer.asEntity() instanceof ServerPlayer player) {
            applyAdvancedFlight(player);
        }
    }

    private static void setFlightSpeed(Player player, float speed) {
        player.getAbilities().setFlyingSpeed(speed);
        player.onUpdateAbilities();
    }

    private static float getAdvancedFlightSpeed() {
        return de.teamlapen.vampirism.config.VampirismConfig.BALANCE.vaBatFlightSpeed.get().floatValue() * ADVANCED_FLIGHT_SPEED_MULTIPLIER;
    }
}



