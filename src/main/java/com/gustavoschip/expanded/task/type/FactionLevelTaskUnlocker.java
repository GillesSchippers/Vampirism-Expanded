package com.gustavoschip.expanded.task.type;

import com.gustavoschip.expanded.task.ModTasks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.task.TaskUnlocker;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record FactionLevelTaskUnlocker(ResourceLocation faction, int minLevel, Optional<Integer> maxLevel,
                                       int minLordRank, Optional<Integer> maxLordRank) implements TaskUnlocker {

    public static final MapCodec<FactionLevelTaskUnlocker> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("faction")
                            .forGetter(FactionLevelTaskUnlocker::faction),

                    Codec.INT.fieldOf("minLevel")
                            .forGetter(FactionLevelTaskUnlocker::minLevel),

                    Codec.INT.optionalFieldOf("maxLevel")
                            .forGetter(FactionLevelTaskUnlocker::maxLevel),

                    Codec.INT.fieldOf("minLordRank")
                            .forGetter(FactionLevelTaskUnlocker::minLordRank),

                    Codec.INT.optionalFieldOf("maxLordRank")
                            .forGetter(FactionLevelTaskUnlocker::maxLordRank)

            ).apply(instance, FactionLevelTaskUnlocker::new));

    @Override
    public Component getDescription() {
        return Component.literal("Requires faction: %s, level: %d%s, lord rank: %d%s".formatted(
                faction,
                minLevel,
                maxLevel.map(max -> "-" + max).orElse("+"),
                minLordRank,
                maxLordRank.map(max -> "-" + max).orElse("+")
        ));
    }

    @Override
    public boolean isUnlocked(IFactionPlayer<?> playerEntity) {
        if (!this.faction.equals(playerEntity.getFaction().getID())) {
            return false;
        }

        int level = playerEntity.getLevel();
        if (level < minLevel) return false;

        if (maxLevel.isPresent() && level > maxLevel.get()) {
            return false;
        }

        int lordRank = FactionPlayerHandler.get(playerEntity.asEntity()).getLordLevel();

        if (lordRank < minLordRank) {
            return false;
        }

        return maxLordRank.isEmpty() || lordRank <= maxLordRank.get();
    }

    @Override
    public MapCodec<? extends TaskUnlocker> codec() {
        return ModTasks.FACTION_LEVEL_UNLOCKER.get();
    }
}