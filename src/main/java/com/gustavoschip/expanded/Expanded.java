package com.gustavoschip.expanded;

import com.gustavoschip.expanded.attachment.ModAttachments;
import com.gustavoschip.expanded.compat.ExpandedGuideBook;
import com.gustavoschip.expanded.event.NeoForgeEvents;
import com.gustavoschip.expanded.event.VampirismEvents;
import com.gustavoschip.expanded.skill.ModSkills;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;


@Mod(Expanded.MOD_ID)
public class Expanded {
    public static final String MOD_ID = "expanded";

    public Expanded(IEventBus modEventBus) {
        ModAttachments.register(modEventBus);
        ModSkills.register(modEventBus);

        if (ModList.get().isLoaded("guideapi_vp")) {
            NeoForge.EVENT_BUS.register(new ExpandedGuideBook());
        }

        NeoForge.EVENT_BUS.register(new NeoForgeEvents());
        NeoForge.EVENT_BUS.register(new VampirismEvents());
    }
}
