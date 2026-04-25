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

package com.gustavoschip.expanded.skill.holder;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

import com.gustavoschip.expanded.compat.guideapi.utils.GuideBookEntry;
import com.gustavoschip.expanded.skill.ModSkills;
import de.teamlapen.vampirism.api.entity.factions.ISkillTree;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public final class SkillTreeHolders {

    public static final ResourceKey<ISkillTree> HUNTER_LEVEL = ModSkills.tree("hunter/level");
    public static final ResourceKey<ISkillTree> VAMPIRE_LEVEL = ModSkills.tree("vampire/level");
    public static final GuideBookEntry HUNTER_LEVEL_GUIDE = new GuideBookEntry(
        HUNTER_LEVEL.location(),
        "tree.expanded.hunter",
        "guide.expanded.hunter_tree.text",
        texture("hunter_root.png")
    );
    public static final GuideBookEntry VAMPIRE_LEVEL_GUIDE = new GuideBookEntry(
        VAMPIRE_LEVEL.location(),
        "tree.expanded.vampire",
        "guide.expanded.vampire_tree.text",
        texture("vampire_root.png")
    );

    private SkillTreeHolders() {}

    private static ResourceLocation texture(String path) {
        return fromNamespaceAndPath(MOD_ID, "textures/skills/" + path);
    }
}
