package com.gustavoschip.expanded.mixin.client;

import com.gustavoschip.expanded.mixin.SkillHandlerAccessorMixin;
import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(targets = "de.teamlapen.vampirism.client.gui.screens.skills.SkillsTabScreen", remap = false)
public abstract class SkillsTabScreenMixin {
    @Shadow
    @Final
    private Holder<ISkillTree> skillTree;
    @Shadow
    @Final
    private ISkillHandler<?> skillHandler;

    @Inject(method = "getRemainingPoints", at = @At("HEAD"), cancellable = true)
    private void expanded$useExpandedRemainingPoints(CallbackInfoReturnable<Integer> cir) {
        if (!ModSkills.ExpandedSkillPointHelper.isExpandedTree(this.skillTree)) {
            return;
        }

        SkillHandlerAccessorMixin accessor = (SkillHandlerAccessorMixin) this.skillHandler;
        IFactionPlayer<?> player = accessor.expanded$getPlayer();
        ArrayList<ISkill<?>> enabledSkills = accessor.expanded$getEnabledSkills();
        cir.setReturnValue(ModSkills.ExpandedSkillPointHelper.getRemainingExpandedPoints(player, enabledSkills));
    }
}



