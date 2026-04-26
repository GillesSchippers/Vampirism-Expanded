/*
 * MIT License
 *
 * Copyright (c) 2026 Gilles Schippers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gustavoschip.expanded.command.holder;

import com.gustavoschip.expanded.task.ModTasks;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class ExpandedCommandHolders {

    private ExpandedCommandHolders() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("expanded")
                .requires(source -> source.hasPermission(2))
                .then(
                    Commands.literal("points").then(
                        Commands.argument("targets", EntityArgument.players()).then(
                            Commands.argument("points", IntegerArgumentType.integer()).executes(context ->
                                givePoints(context.getSource(), EntityArgument.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "points"))
                            )
                        )
                    )
                )
        );
    }

    private static int givePoints(CommandSourceStack source, Collection<ServerPlayer> targets, int points) {
        int affected = 0;
        ServerPlayer singleAffectedPlayer = null;

        for (ServerPlayer player : targets) {
            java.util.Optional<? extends IFactionPlayer<?>> factionPlayer = FactionPlayerHandler.get(player).getCurrentFactionPlayer();

            if (factionPlayer.isPresent()) {
                ModTasks.TaskSkillPointStorage.addSkillPoints(factionPlayer.get(), factionPlayer.get().getFaction().getID(), points);

                affected++;

                if (affected == 1) {
                    singleAffectedPlayer = player;
                } else {
                    singleAffectedPlayer = null;
                }
            }
        }

        final int finalAffected = affected;
        final ServerPlayer finalSingleAffectedPlayer = singleAffectedPlayer;

        source.sendSuccess(
            () -> {
                if (finalAffected == 0) {
                    return Component.literal("No valid players were affected.");
                } else if (finalAffected == 1) {
                    return Component.literal("Gave " + points + " points to " + finalSingleAffectedPlayer.getName().getString() + ".");
                }

                return Component.literal("Gave " + points + " points to " + finalAffected + " player(s).");
            },
            true
        );

        return affected;
    }
}
