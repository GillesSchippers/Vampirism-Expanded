package com.gustavoschip.expanded;

import com.gustavoschip.expanded.attachment.ModAttachments;
import com.gustavoschip.expanded.event.NeoForgeEvents;
import com.gustavoschip.expanded.event.VampirismEvents;
import com.gustavoschip.expanded.skill.ModSkills;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;


@Mod(PoisonousBlood.MOD_ID)
public class PoisonousBlood {
    public static final String MOD_ID = "expanded";

    public PoisonousBlood(IEventBus modEventBus) {
        ModAttachments.register(modEventBus);
        ModSkills.register(modEventBus);

        NeoForge.EVENT_BUS.register(new NeoForgeEvents());
        NeoForge.EVENT_BUS.register(new VampirismEvents());
    }
}
