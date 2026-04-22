package com.gustavoschip.expanded.event;

import com.gustavoschip.expanded.event.holder.NeoForgeEventHolders;
import com.gustavoschip.expanded.event.holder.VampirismEventHolders;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

@SuppressWarnings("unused")
public final class ModEvents {

    private ModEvents() {
    }

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(VampirismEventHolders.INSTANCE);
        NeoForge.EVENT_BUS.register(NeoForgeEventHolders.INSTANCE);
    }
}

