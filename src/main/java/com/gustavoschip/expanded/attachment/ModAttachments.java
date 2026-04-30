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

package com.gustavoschip.expanded.attachment;

import static com.gustavoschip.expanded.Expanded.MOD_ID;

import com.gustavoschip.expanded.attachment.holder.ExpandedAttachmentHolders;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Registers the shared player attachment used to persist Expanded skill toggles and task
 * points across sync, death, and reconnects.
 */

public abstract class ModAttachments {

    /**
     * Deferred register for Expanded attachment types.
     */

    protected static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);

    /**
     * Registers this mod's attachment types on the supplied event bus.
     */

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ExpandedAttachmentHolders>> SHARED_ATTACHMENT = ATTACHMENTS.register("shared_data", () ->
        AttachmentType.builder(ExpandedAttachmentHolders::new).serialize(ExpandedAttachmentHolders.CODEC).sync(ByteBufCodecs.fromCodec(ExpandedAttachmentHolders.CODEC)).copyOnDeath().build()
    );

    /**
     * Registers this mod's attachment types on the supplied event bus.
     */

    public static void register(IEventBus modEventBus) {
        ATTACHMENTS.register(modEventBus);
    }
}
