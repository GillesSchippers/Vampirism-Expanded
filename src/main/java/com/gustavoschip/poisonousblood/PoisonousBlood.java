package com.gustavoschip.poisonousblood;

import com.gustavoschip.poisonousblood.attachment.ModAttachments;
import com.gustavoschip.poisonousblood.events.VampirismEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;


@Mod(PoisonousBlood.MOD_ID)
public class PoisonousBlood {
    public static final String MOD_ID = "poisonousblood";

    public PoisonousBlood(IEventBus modEventBus, ModContainer modContainer) {
        ModAttachments.register(modEventBus);

        NeoForge.EVENT_BUS.register(new VampirismEvents());
    }
}
