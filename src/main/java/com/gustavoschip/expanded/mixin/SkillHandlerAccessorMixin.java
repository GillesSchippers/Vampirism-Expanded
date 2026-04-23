package com.gustavoschip.expanded.mixin;

import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(targets = "de.teamlapen.vampirism.entity.player.skills.SkillHandler", remap = false)
public interface SkillHandlerAccessorMixin {
    @Accessor("enabledSkills")
    ArrayList<ISkill<?>> expanded$getEnabledSkills();

    @Accessor("player")
    IFactionPlayer<?> expanded$getPlayer();
}


