package com.gustavoschip.expanded;

import com.gustavoschip.expanded.attachment.ModAttachments;
import com.gustavoschip.expanded.compat.guideapi.ExpandedGuideBook;
import com.gustavoschip.expanded.event.ModEvents;
import com.gustavoschip.expanded.service.ModServices;
import com.gustavoschip.expanded.skill.ModSkills;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;


@Mod(Expanded.MOD_ID)
public class Expanded {
    public static final String MOD_ID = "expanded";

    @SuppressWarnings("unused")
    public Expanded(IEventBus modEventBus) {
        ModAttachments.register(modEventBus);
        ModSkills.register(modEventBus);
        ModEvents.register(modEventBus);
        ModServices.register(modEventBus);

        if (FMLEnvironment.dist.isClient() && ModList.get().isLoaded("guideapi_vp")) {
            ExpandedGuideBook.register(modEventBus);
        }
    }
}
