package com.gustavoschip.expanded.mixin;

import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(targets = "de.teamlapen.vampirism.entity.player.skills.SkillHandler", remap = false)
public abstract class SkillHandlerMixin<T extends IFactionPlayer<T>> {
    @Shadow
    @Final
    private T player;
    @Shadow
    @Final
    private ArrayList<ISkill<T>> enabledSkills;

    @Redirect(method = "canSkillBeEnabled", at = @At(value = "INVOKE", target = "Lde/teamlapen/vampirism/entity/player/skills/SkillHandler;getLeftSkillPoints()I"))
    private int expanded$useExpandedPointsForExpandedSkills(de.teamlapen.vampirism.entity.player.skills.SkillHandler<?> instance, ISkill<T> skill) {
        if (!ModSkills.ExpandedSkillPointHelper.usesExpandedPoints(skill)) {
            return instance.getLeftSkillPoints();
        }

        return ModSkills.ExpandedSkillPointHelper.getRemainingExpandedPoints(this.player, this.enabledSkills);
    }
}


