package com.gustavoschip.expanded.skill.type;

import com.mojang.datafixers.util.Either;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ActionFactionSkill<T extends IFactionPlayer<T>> extends FactionSkillBase<T> {
    public ActionFactionSkill(Either<ResourceKey<ISkillTree>, TagKey<ISkillTree>> tree, ResourceLocation factionId, int skillPointCost, boolean hasDefaultDescription) {
        super(tree, factionId, skillPointCost, hasDefaultDescription);
    }

    @Override
    public @NotNull ActionFactionSkill<T> setToggleActions(Consumer<T> activate, Consumer<T> deactivate) {
        super.setToggleActions(activate, deactivate);
        return this;
    }

}

