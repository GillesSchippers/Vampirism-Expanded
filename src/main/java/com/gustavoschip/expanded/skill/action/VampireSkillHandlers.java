package com.gustavoschip.expanded.skill.action;

import com.mojang.logging.LogUtils;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.function.Consumer;

public final class VampireSkillHandlers {
    private static final Logger LOGGER = LogUtils.getLogger();

    private VampireSkillHandlers() {
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> doToggle(boolean toggle) {
        return player -> {
            if (player.asEntity() instanceof ServerPlayer serverPlayer) {
                return;
            }
        };
    }
}

