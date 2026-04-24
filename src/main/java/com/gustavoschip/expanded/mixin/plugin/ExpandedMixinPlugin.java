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

package com.gustavoschip.expanded.mixin.plugin;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExpandedMixinPlugin extends RestrictiveMixinConfigPlugin {
    private static final List<String> COMMON_MIXINS = List.of(
            "BatVampireActionMixin",
            "EntityMixin",
            "LivingEntityMixin",
            "SkillHandlerAccessorMixin",
            "SkillHandlerMixin",
            "VampirePlayerMixin"
    );

    private static final List<String> CLIENT_MIXINS = List.of(
            "client.SkillsTabScreenMixin",
            "client.SunOverlayMixin",
            "client.VampirismHUDOverlayMixin",
            "client.VampirismRenderHandlerMixin"
    );

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // No target filtering yet; conditional mixin rules can be added here later.
    }

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>(COMMON_MIXINS);

        if (FMLEnvironment.dist.isClient()) {
            mixins.addAll(CLIENT_MIXINS);
        }

        return mixins;
    }
}