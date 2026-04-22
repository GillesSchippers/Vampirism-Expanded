package com.gustavoschip.expanded.attachment;

import com.gustavoschip.expanded.attachment.holder.SkillAttachmentHolders;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.gustavoschip.expanded.Expanded.MOD_ID;

@SuppressWarnings("unused")
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> POISONOUS_BLOOD_ATTACHMENT = SkillAttachmentHolders.POISONOUS_BLOOD_ATTACHMENT;
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> GARLIC_BLOOD_ATTACHMENT = SkillAttachmentHolders.GARLIC_BLOOD_ATTACHMENT;
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> VAMPIRIC_GROUNDING_ATTACHMENT = SkillAttachmentHolders.VAMPIRIC_GROUNDING_ATTACHMENT;
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ADVANCED_FLIGHT_ATTACHMENT = SkillAttachmentHolders.ADVANCED_FLIGHT_ATTACHMENT;

    protected ModAttachments() {
    }

    public static DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> registerBooleanAttachment(String id) {
        return ATTACHMENTS.register(id, () -> AttachmentType.builder(() -> false)
                .serialize(Codec.BOOL)
                .sync(ByteBufCodecs.BOOL)
                .copyOnDeath()
                .build());
    }

    public static void register(IEventBus modEventBus) {
        ATTACHMENTS.register(modEventBus);
    }
}

