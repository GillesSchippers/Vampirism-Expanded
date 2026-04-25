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

import com.gustavoschip.expanded.attachment.ModAttachments;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TaskAttachmentHolders {

    public static final String HUNTER_TASK_SKILL_POINTS_ATTACHMENT_ID = "hunter_task_skill_points";
    public static final String VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT_ID = "vampire_task_skill_points";

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> HUNTER_TASK_SKILL_POINTS_ATTACHMENT =
        ModAttachments.registerIntegerAttachment(HUNTER_TASK_SKILL_POINTS_ATTACHMENT_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT =
        ModAttachments.registerIntegerAttachment(VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT_ID);

    private TaskAttachmentHolders() {}
}
