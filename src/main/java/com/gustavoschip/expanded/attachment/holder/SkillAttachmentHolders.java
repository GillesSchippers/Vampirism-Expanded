package com.gustavoschip.expanded.attachment.holder;

import com.gustavoschip.expanded.attachment.ModAttachments;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class SkillAttachmentHolders {
    public static final String POISONOUS_BLOOD_ATTACHMENT_ID = "poisonous_blood";
    public static final String GARLIC_BLOOD_ATTACHMENT_ID = "garlic_blood";
    public static final String VAMPIRIC_GROUNDING_ATTACHMENT_ID = "vampiric_grounding";
    public static final String ADVANCED_FLIGHT_ATTACHMENT_ID = "advanced_flight";

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> POISONOUS_BLOOD_ATTACHMENT = ModAttachments.registerBooleanAttachment(POISONOUS_BLOOD_ATTACHMENT_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> GARLIC_BLOOD_ATTACHMENT = ModAttachments.registerBooleanAttachment(GARLIC_BLOOD_ATTACHMENT_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> VAMPIRIC_GROUNDING_ATTACHMENT = ModAttachments.registerBooleanAttachment(VAMPIRIC_GROUNDING_ATTACHMENT_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ADVANCED_FLIGHT_ATTACHMENT = ModAttachments.registerBooleanAttachment(ADVANCED_FLIGHT_ATTACHMENT_ID);

    private SkillAttachmentHolders() {
    }
}
