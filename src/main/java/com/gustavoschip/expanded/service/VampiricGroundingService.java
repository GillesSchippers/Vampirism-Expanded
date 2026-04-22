package com.gustavoschip.expanded.service;

import com.gustavoschip.expanded.skill.ModSkills;
import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.event.ActionEvent;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static com.gustavoschip.expanded.attachment.ModAttachments.VAMPIRIC_GROUNDING_ATTACHMENT;
import static de.teamlapen.vampirism.api.VampirismAPI.factionPlayerHandler;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public final class VampiricGroundingService {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation SUNDAMAGE_REDUCTION_ID = fromNamespaceAndPath(MOD_ID, "vampiric_grounding_sundamage");
    private static final ResourceLocation BLOOD_EXHAUSTION_INCREASE_ID = fromNamespaceAndPath(MOD_ID, "vampiric_grounding_blood_exhaustion");
    private static final double SUNDAMAGE_REDUCTION_MODIFIER = -0.75D;
    private static final double BLOOD_EXHAUSTION_INCREASE_MODIFIER = 0.5D;

    private VampiricGroundingService() {
    }

    public static boolean canSyncAttachment(ServerPlayer player) {
        return player != null && player.connection != null;
    }

    public static boolean hasVampiricGrounding(Player player) {
        return player.getData(VAMPIRIC_GROUNDING_ATTACHMENT);
    }

    public static boolean hasVampiricGrounding(ServerPlayer player) {
        if (!canSyncAttachment(player)) {
            return false;
        }
        return hasVampiricGrounding((Player) player);
    }

    public static boolean hasVampiricGroundingSkill(ServerPlayer player) {
        return factionPlayerHandler(player)
                .getCurrentFactionPlayer()
                .map(factionPlayer -> factionPlayer.getSkillHandler().isSkillEnabled(ModSkills.VAMPIRIC_GROUNDING.get()))
                .orElse(false);
    }

    public static void setVampiricGrounding(ServerPlayer player, boolean vampiricGrounding) {
        if (!canSyncAttachment(player)) {
            LOGGER.debug("Deferred vampiric grounding update for {} until login sync", player.getName().getString());
            return;
        }

        if (hasVampiricGrounding(player) == vampiricGrounding) {
            return;
        }

        player.setData(VAMPIRIC_GROUNDING_ATTACHMENT, vampiricGrounding);
        if (vampiricGrounding) {
            applyAttributeBonuses(player);
            deactivateBatMode(player);
        } else {
            removeAttributeBonuses(player);
        }
    }

    public static void syncFromVampireSkill(ServerPlayer player) {
        setVampiricGrounding(player, hasVampiricGroundingSkill(player));
    }

    public static void handleBatActionActivated(ActionEvent.ActionActivatedEvent event) {
        if (event.getAction() != VampireActions.BAT.get()) {
            return;
        }
        if (!(event.getFactionPlayer() instanceof IVampirePlayer vampirePlayer)) {
            return;
        }
        if (!(vampirePlayer.asEntity() instanceof ServerPlayer player) || !hasVampiricGrounding(player)) {
            return;
        }

        event.setCanceled(true);
        player.displayClientMessage(Component.translatable("text.expanded.vampiric_grounding.bat_disabled"), true);
    }

    private static void deactivateBatMode(ServerPlayer player) {
        VampirePlayer vampirePlayer = VampirePlayer.get(player);
        if (vampirePlayer.getActionHandler().isActionActive(VampireActions.BAT.get())) {
            vampirePlayer.getActionHandler().deactivateAction(VampireActions.BAT.get());
        }
    }

    private static void applyAttributeBonuses(ServerPlayer player) {
        AttributeInstance sundamage = player.getAttribute(ModAttributes.SUNDAMAGE);
        AttributeInstance bloodExhaustion = player.getAttribute(ModAttributes.BLOOD_EXHAUSTION);

        if (sundamage != null) {
            replaceModifier(sundamage, SUNDAMAGE_REDUCTION_ID, SUNDAMAGE_REDUCTION_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
        if (bloodExhaustion != null) {
            replaceModifier(bloodExhaustion, BLOOD_EXHAUSTION_INCREASE_ID, BLOOD_EXHAUSTION_INCREASE_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
    }

    private static void removeAttributeBonuses(ServerPlayer player) {
        AttributeInstance sundamage = player.getAttribute(ModAttributes.SUNDAMAGE);
        AttributeInstance bloodExhaustion = player.getAttribute(ModAttributes.BLOOD_EXHAUSTION);

        if (sundamage != null) {
            sundamage.removeModifier(SUNDAMAGE_REDUCTION_ID);
        }
        if (bloodExhaustion != null) {
            bloodExhaustion.removeModifier(BLOOD_EXHAUSTION_INCREASE_ID);
        }
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


