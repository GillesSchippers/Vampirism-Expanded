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

/**
 * Attachment payload that stores all Expanded skill flags and per-faction task skill
 * points for a player.
 */

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
    /**
     * Cached value for innate toughness.
     */

    public boolean innateToughness;
    /**
     * Cached value for hunters growth.
     */

    public boolean huntersGrowth;
    /**
     * Cached value for prepared hunt.
     */

    public boolean preparedHunt;
    /**
     * Cached value for poisonous blood.
     */

    public boolean poisonousBlood;
    /**
     * Cached value for garlic blood.
     */

    public boolean garlicBlood;
    /**
     * Cached value for bat speed.
     */

    public boolean batSpeed;
    /**
     * Cached value for bat armor.
     */

    public boolean batArmor;
    /**
     * Cached value for bat liquid.
     */

    public boolean batLiquid;
    /**
     * Cached value for vampiric constitution.
     */

    public boolean vampiricConstitution;
    /**
     * Cached value for day walker.
     */

    public boolean dayWalker;
    /**
     * Cached value for hunter task skill points.
     */

    public int hunterTaskSkillPoints;
    /**
     * Cached value for vampire task skill points.
     */

    public int vampireTaskSkillPoints;

    /**
     * Creates a new instance of ExpandedAttachmentHolders.
     */

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
