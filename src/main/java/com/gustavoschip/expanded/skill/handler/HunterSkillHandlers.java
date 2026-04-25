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

package com.gustavoschip.expanded.skill.handler;

import static com.gustavoschip.expanded.skill.ModSkills.createToggleAction;

import com.gustavoschip.expanded.service.skill.HunterService;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import java.util.function.Consumer;

public final class HunterSkillHandlers {

    private HunterSkillHandlers() {}

    public static <T extends IFactionPlayer<T>> Consumer<T> innateToughnessToggle(boolean enabled) {
        //return createToggleAction("Innate Toughness", enabled, HunterService::setInnateToughness);
        return createToggleAction("Innate Toughness", enabled, null); // TODO
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> huntersGrowthToggle(boolean enabled) {
        //return createToggleAction("Hunters Growth", enabled, HunterService::setHuntersGrowth);
        return createToggleAction("Hunters Growth", enabled, null); // TODO
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> preparedHuntToggle(boolean enabled) {
        //return createToggleAction("Prepared Hunt", enabled, HunterService::setPreparedHunt);
        return createToggleAction("Prepared Hunt", enabled, null); // TODO
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> garlicBloodToggle(boolean enabled) {
        return createToggleAction("Garlic Blood", enabled, HunterService::setGarlicBlood);
    }

    public static <T extends IFactionPlayer<T>> Consumer<T> poisonousBloodToggle(boolean enabled) {
        return createToggleAction("Poisonous Blood", enabled, HunterService::setPoisonousBlood);
    }
}
