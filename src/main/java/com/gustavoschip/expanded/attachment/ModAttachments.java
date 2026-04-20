package com.gustavoschip.expanded.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.gustavoschip.expanded.PoisonousBlood.MOD_ID;

public class ModAttachments {
    public static final String POISONOUS_BLOOD_ATTACHMENT_ID = "poisonous_blood";
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> POISONOUS_BLOOD_ATTACHMENT = ATTACHMENTS.register(POISONOUS_BLOOD_ATTACHMENT_ID, () ->
            AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL)
                    .sync(ByteBufCodecs.BOOL)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        ATTACHMENTS.register(modEventBus);
    }
}

