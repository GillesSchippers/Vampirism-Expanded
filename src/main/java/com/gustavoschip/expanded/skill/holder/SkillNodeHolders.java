package com.gustavoschip.expanded.skill.holder;

import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillNode;
import net.minecraft.resources.ResourceKey;

public final class SkillNodeHolders {
    public static final ResourceKey<ISkillNode> HUNTER_ROOT = ModSkills.node("hunter_root");
    public static final ResourceKey<ISkillNode> HUNTER_1 = ModSkills.node("hunter_1");
    public static final ResourceKey<ISkillNode> VAMPIRE_ROOT = ModSkills.node("vampire_root");
    public static final ResourceKey<ISkillNode> VAMPIRE_1 = ModSkills.node("vampire_1");

    private SkillNodeHolders() {
    }
}
