package com.gustavoschip.expanded.mixin;

import com.gustavoschip.expanded.skill.holder.SkillTreeHolders;
import com.gustavoschip.expanded.task.ModTasks;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.LinkedHashSet;

@Mixin(targets = "de.teamlapen.vampirism.entity.player.skills.SkillHandler", remap = false)
public abstract class SkillHandlerMixin<T extends IFactionPlayer<T>> {
    @Shadow
    public LinkedHashSet<Holder<ISkillTree>> unlockedTrees;
    @Shadow
    @Final
    private T player;
    @Shadow
    @Final
    private ArrayList<ISkill<T>> enabledSkills;

    // TODO: FIX this non-working logic. All point systems get overwritten with our one if our tree or nodes are unlocked!
    @Inject(method = "getLeftSkillPoints", at = @At("HEAD"), cancellable = true)
    private void expanded$onlyUseTaskPoints(CallbackInfoReturnable<Integer> cir) {
        ResourceLocation factionId = this.player.getFaction().getID();
        if (!ModTasks.TaskHolders.HUNTER_FACTION_ID.equals(factionId) && !ModTasks.TaskHolders.VAMPIRE_FACTION_ID.equals(factionId)) {
            return;
        }

        boolean usesExpandedTrees = this.unlockedTrees.stream().anyMatch(tree -> tree.is(SkillTreeHolders.HUNTER_LEVEL) || tree.is(SkillTreeHolders.VAMPIRE_LEVEL));
        if (!usesExpandedTrees) {
            return;
        }

        if (de.teamlapen.vampirism.config.VampirismConfig.SERVER.unlockAllSkills.get() && this.player.getLevel() == this.player.getMaxLevel()) {
            cir.setReturnValue(Integer.MAX_VALUE);
            return;
        }

        int points = ModTasks.TaskSkillPointStorage.getSkillPoints(this.player) - this.enabledSkills.stream().mapToInt(ISkill::getSkillPointCost).sum();
        cir.setReturnValue(Math.max(0, points));
    }
}


