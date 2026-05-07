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

    /**
     * Field name constant for innate toughness hunter skill state.
     */
    public static final String INNATE_TOUGHNESS = "innateToughness";

    /**
     * Field name constant for hunters growth skeleton skill state.
     */
    public static final String HUNTERS_GROWTH = "huntersGrowth";

    /**
     * Field name constant for prepared hunt skill state.
     */
    public static final String PREPARED_HUNT = "preparedHunt";

    /**
     * Field name constant for poisonous blood skill state.
     */
    public static final String POISONOUS_BLOOD = "poisonousBlood";

    /**
     * Field name constant for garlic blood skill state.
     */
    public static final String GARLIC_BLOOD = "garlicBlood";

    /**
     * Field name constant for bat speed vampire skill state.
     */
    public static final String BAT_SPEED = "batSpeed";

    /**
     * Field name constant for bat armor vampire skill state.
     */
    public static final String BAT_ARMOR = "batArmor";

    /**
     * Field name constant for bat liquid traversal skill state.
     */
    public static final String BAT_LIQUID = "batLiquid";

    /**
     * Field name constant for vampiric constitution skill state.
     */
    public static final String VAMPIRIC_CONSTITUTION = "vampiricConstitution";

    /**
     * Field name constant for day walker skill state.
     */
    public static final String DAY_WALKER = "dayWalker";

    /**
     * Field name constant for hunter task skill points state.
     */
    public static final String HUNTER_TASK_SKILL_POINTS = "hunter_task_skill_points";

    /**
     * Field name constant for vampire task skill points state.
     */
    public static final String VAMPIRE_TASK_SKILL_POINTS = "vampire_task_skill_points";

    public static final Codec<ExpandedAttachmentHolders> CODEC = RecordCodecBuilder.create(instance ->
        instance
            .group(
                Codec.BOOL.fieldOf(INNATE_TOUGHNESS)
                    .orElse(false)
                    .forGetter(d -> d.innateToughness),
                Codec.BOOL.fieldOf(HUNTERS_GROWTH)
                    .orElse(false)
                    .forGetter(d -> d.huntersGrowth),
                Codec.BOOL.fieldOf(PREPARED_HUNT)
                    .orElse(false)
                    .forGetter(d -> d.preparedHunt),
                Codec.BOOL.fieldOf(POISONOUS_BLOOD)
                    .orElse(false)
                    .forGetter(d -> d.poisonousBlood),
                Codec.BOOL.fieldOf(GARLIC_BLOOD)
                    .orElse(false)
                    .forGetter(d -> d.garlicBlood),
                Codec.BOOL.fieldOf(BAT_SPEED)
                    .orElse(false)
                    .forGetter(d -> d.batSpeed),
                Codec.BOOL.fieldOf(BAT_ARMOR)
                    .orElse(false)
                    .forGetter(d -> d.batArmor),
                Codec.BOOL.fieldOf(BAT_LIQUID)
                    .orElse(false)
                    .forGetter(d -> d.batLiquid),
                Codec.BOOL.fieldOf(VAMPIRIC_CONSTITUTION)
                    .orElse(false)
                    .forGetter(d -> d.vampiricConstitution),
                Codec.BOOL.fieldOf(DAY_WALKER)
                    .orElse(false)
                    .forGetter(d -> d.dayWalker),
                Codec.INT.fieldOf(HUNTER_TASK_SKILL_POINTS)
                    .orElse(0)
                    .forGetter(d -> d.hunterTaskSkillPoints),
                Codec.INT.fieldOf(VAMPIRE_TASK_SKILL_POINTS)
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
