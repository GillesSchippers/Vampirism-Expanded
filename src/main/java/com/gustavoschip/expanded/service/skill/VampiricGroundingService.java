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
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import org.slf4j.Logger;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public final class VampiricGroundingService extends ModServices {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation SUNDAMAGE_REDUCTION_ID = fromNamespaceAndPath(MOD_ID, "vampiric_grounding_sundamage");
    private static final ResourceLocation BLOOD_EXHAUSTION_INCREASE_ID = fromNamespaceAndPath(MOD_ID, "vampiric_grounding_blood_exhaustion");
    private static final double SUNDAMAGE_REDUCTION_MODIFIER = -0.75D;
    private static final double BLOOD_EXHAUSTION_INCREASE_MODIFIER = 0.5D;

    private VampiricGroundingService() {
    }

    public static boolean hasVampiricGrounding(Player player) {
        return hasBooleanAttachment(player, SkillAttachmentHolders.VAMPIRIC_GROUNDING_ATTACHMENT);
    }

    public static boolean hasVampiricGroundingSkill(ServerPlayer player) {
        return hasSkillEnabled(player, SkillHolders.VAMPIRIC_GROUNDING);
    }

    public static void setVampiricGrounding(ServerPlayer player, boolean vampiricGrounding) {
        if (!setBooleanAttachment(player, SkillAttachmentHolders.VAMPIRIC_GROUNDING_ATTACHMENT, vampiricGrounding, "vampiric grounding", LOGGER)) {
            return;
        }

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

    public static boolean canEnterBatMode(Player player) {
        return !hasVampiricGrounding(player);
    }

    public static boolean handleBatActivation(Player player) {
        if (canEnterBatMode(player)) {
            return true;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.translatable("text.expanded.vampiric_grounding.bat_disabled"), true);
        }
        return false;
    }

    public static void clearSunDisorientation(ServerPlayer player) {
        if (!hasVampiricGrounding(player)) {
            return;
        }

        if (player.hasEffect(MobEffects.CONFUSION)) {
            player.removeEffect(MobEffects.CONFUSION);
        }
    }


    public static void handleLivingKnockback(LivingKnockBackEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !VampiricGroundingService.hasVampiricGrounding(player)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntitySunDamageTracker tracker) || !tracker.expanded$isLastDamageWasSun()) {
            return;
        }

        event.setStrength(0.0F);
        event.setRatioX(0.0D);
        event.setRatioZ(0.0D);
        event.setCanceled(true);
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
            replaceModifier(sundamage, SUNDAMAGE_REDUCTION_ID, SUNDAMAGE_REDUCTION_MODIFIER);
        }
        if (bloodExhaustion != null) {
            replaceModifier(bloodExhaustion, BLOOD_EXHAUSTION_INCREASE_ID, BLOOD_EXHAUSTION_INCREASE_MODIFIER);
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

    // TODO: Create separate interface module
    public interface LivingEntitySunDamageTracker {
        boolean expanded$isLastDamageWasSun();
    }
}



