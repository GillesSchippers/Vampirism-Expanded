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

package com.gustavoschip.expanded.skill.type;

import com.mojang.datafixers.util.Either;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public class ActionFactionSkill<T extends IFactionPlayer<T>> extends FactionSkillBase<T> {

    public ActionFactionSkill(
        Either<ResourceKey<ISkillTree>, TagKey<ISkillTree>> tree,
        ResourceLocation factionId,
        int skillPointCost,
        boolean hasDefaultDescription
    ) {
        super(tree, factionId, skillPointCost, hasDefaultDescription);
    }

    @Override
    public @NotNull ActionFactionSkill<T> setToggleActions(Consumer<T> activate, Consumer<T> deactivate) {
        super.setToggleActions(activate, deactivate);
        return this;
    }
}
