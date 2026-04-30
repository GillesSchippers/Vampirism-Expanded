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

package com.gustavoschip.expanded.attachment.holder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class ExpandedAttachmentHolders {

    public static final Codec<ExpandedAttachmentHolders> CODEC = RecordCodecBuilder.create(instance ->
        instance
            .group(
                Codec.BOOL.fieldOf("innate_toughness")
                    .orElse(false)
                    .forGetter(d -> d.innateToughness),
                Codec.BOOL.fieldOf("hunters_growth")
                    .orElse(false)
                    .forGetter(d -> d.huntersGrowth),
                Codec.BOOL.fieldOf("prepared_hunt")
                    .orElse(false)
                    .forGetter(d -> d.preparedHunt),
                Codec.BOOL.fieldOf("poisonous_blood")
                    .orElse(false)
                    .forGetter(d -> d.poisonousBlood),
                Codec.BOOL.fieldOf("garlic_blood")
                    .orElse(false)
                    .forGetter(d -> d.garlicBlood),
                Codec.BOOL.fieldOf("bat_speed")
                    .orElse(false)
                    .forGetter(d -> d.batSpeed),
                Codec.BOOL.fieldOf("bat_armor")
                    .orElse(false)
                    .forGetter(d -> d.batArmor),
                Codec.BOOL.fieldOf("bat_liquid")
                    .orElse(false)
                    .forGetter(d -> d.batLiquid),
                Codec.BOOL.fieldOf("vampiric_constitution")
                    .orElse(false)
                    .forGetter(d -> d.vampiricConstitution),
                Codec.BOOL.fieldOf("day_walker")
                    .orElse(false)
                    .forGetter(d -> d.dayWalker),
                Codec.INT.fieldOf("hunter_task_skill_points")
                    .orElse(0)
                    .forGetter(d -> d.hunterTaskSkillPoints),
                Codec.INT.fieldOf("vampire_task_skill_points")
                    .orElse(0)
                    .forGetter(d -> d.vampireTaskSkillPoints)
            )
            .apply(instance, ExpandedAttachmentHolders::new)
    );
    public boolean innateToughness;
    public boolean huntersGrowth;
    public boolean preparedHunt;
    public boolean poisonousBlood;
    public boolean garlicBlood;
    public boolean batSpeed;
    public boolean batArmor;
    public boolean batLiquid;
    public boolean vampiricConstitution;
    public boolean dayWalker;
    public int hunterTaskSkillPoints;
    public int vampireTaskSkillPoints;

    public ExpandedAttachmentHolders() {
        // defaults
    }

    public ExpandedAttachmentHolders(
        boolean innateToughness,
        boolean huntersGrowth,
        boolean preparedHunt,
        boolean poisonousBlood,
        boolean garlicBlood,
        boolean batSpeed,
        boolean batArmor,
        boolean batLiquid,
        boolean vampiricConstitution,
        boolean dayWalker,
        int hunterTaskSkillPoints,
        int vampireTaskSkillPoints
    ) {
        this.innateToughness = innateToughness;
        this.huntersGrowth = huntersGrowth;
        this.preparedHunt = preparedHunt;
        this.poisonousBlood = poisonousBlood;
        this.garlicBlood = garlicBlood;
        this.batSpeed = batSpeed;
        this.batArmor = batArmor;
        this.batLiquid = batLiquid;
        this.vampiricConstitution = vampiricConstitution;
        this.dayWalker = dayWalker;
        this.hunterTaskSkillPoints = hunterTaskSkillPoints;
        this.vampireTaskSkillPoints = vampireTaskSkillPoints;
    }
}
