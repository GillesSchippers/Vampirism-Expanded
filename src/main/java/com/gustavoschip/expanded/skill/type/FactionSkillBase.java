package com.gustavoschip.expanded.skill.type;

import com.mojang.datafixers.util.Either;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.entity.player.skills.VampirismSkill;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FactionSkillBase<T extends IFactionPlayer<T>> extends VampirismSkill<T> {
    private final ResourceLocation factionId;

    public FactionSkillBase(Either<ResourceKey<ISkillTree>, TagKey<ISkillTree>> tree, ResourceLocation factionId, int skillPointCost, boolean hasDefaultDescription) {
        super(tree, skillPointCost, hasDefaultDescription);
        this.factionId = factionId;
    }

    @Override
    public @NotNull Optional<IPlayableFaction<?>> getFaction() {
        Object faction = VampirismAPI.factionRegistry().getFactionByID(this.factionId);
        if (faction instanceof IPlayableFaction<?> playableFaction) {
            return Optional.of(playableFaction);
        }
        return Optional.empty();
    }

}

