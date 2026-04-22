package com.gustavoschip.expanded.attachment.holder;

import com.gustavoschip.expanded.attachment.ModAttachments;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TaskAttachmentHolders {
    public static final String HUNTER_TASK_SKILL_POINTS_ATTACHMENT_ID = "hunter_task_skill_points";
    public static final String VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT_ID = "vampire_task_skill_points";

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> HUNTER_TASK_SKILL_POINTS_ATTACHMENT = ModAttachments.registerIntegerAttachment(HUNTER_TASK_SKILL_POINTS_ATTACHMENT_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT = ModAttachments.registerIntegerAttachment(VAMPIRE_TASK_SKILL_POINTS_ATTACHMENT_ID);

    private TaskAttachmentHolders() {
    }
}

