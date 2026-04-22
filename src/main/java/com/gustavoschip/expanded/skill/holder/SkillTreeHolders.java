package com.gustavoschip.expanded.skill.holder;

import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import net.minecraft.resources.ResourceKey;

public final class SkillTreeHolders {
    public static final ResourceKey<ISkillTree> HUNTER_LEVEL = ModSkills.tree("hunter/level");
    public static final ResourceKey<ISkillTree> VAMPIRE_LEVEL = ModSkills.tree("vampire/level");

    private SkillTreeHolders() {
    }
}
