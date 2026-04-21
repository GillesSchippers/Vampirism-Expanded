package com.gustavoschip.expanded.compat;

import com.gustavoschip.expanded.skill.ModSkills;
import de.maxanier.guideapi.api.IPage;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.BookHelper;
import de.maxanier.guideapi.category.CategoryItemStack;
import de.maxanier.guideapi.page.PageTextImage;
import de.teamlapen.vampirism.modcompat.guide.EntryText;
import de.teamlapen.vampirism.modcompat.guide.VampirismGuideBookCategoriesEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static de.teamlapen.vampirism.modcompat.guide.GuideBook.translateComponent;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class ExpandedGuideBook {
    private static final ResourceLocation HUNTER_ROOT_TEXTURE = fromNamespaceAndPath(MOD_ID, "textures/skills/hunter_root.png");
    private static final ResourceLocation POISONOUS_BLOOD_TEXTURE = fromNamespaceAndPath(MOD_ID, "textures/skills/poisonous_blood.png");
    private static final ResourceLocation GARLIC_BLOOD_TEXTURE = fromNamespaceAndPath(MOD_ID, "textures/skills/garlic_blood.png");
    private static final ResourceLocation VAMPIRE_ROOT_TEXTURE = fromNamespaceAndPath(MOD_ID, "textures/skills/vampire_root.png");

    @SubscribeEvent
    public void onVampirismGuideBookCategories(VampirismGuideBookCategoriesEvent event) {
        createCategoriesEvent(event);
    }

    public static void createCategoriesEvent(VampirismGuideBookCategoriesEvent event) {
        BookHelper helper = new BookHelper.Builder(MOD_ID).build();
        CategoryAbstract expandedCategory = new CategoryItemStack(buildExpandedEntries(helper), translateComponent("guide.expanded.title"), new ItemStack(Items.BOOK));
        helper.registerLinkablePages(new ArrayList<>(List.of(expandedCategory)));

        event.categories.add(Math.max(event.categories.size() - 1, 0), expandedCategory);
    }

    private static Map<ResourceLocation, EntryAbstract> buildExpandedEntries(BookHelper helper) {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();

        entries.put(entry("overview"), new EntryText(imagePage(helper, "guide.expanded.overview.text", HUNTER_ROOT_TEXTURE), translateComponent("guide.expanded.overview")));
        entries.put(ModSkills.Nodes.HUNTER_ROOT.location(), new EntryText(imagePage(helper, "guide.expanded.hunter.text", HUNTER_ROOT_TEXTURE), translateComponent("guide.expanded.hunter")));
        entries.put(ModSkills.Nodes.POISONOUS_BLOOD.location(), new EntryText(imagePage(helper, "guide.expanded.poisonous_blood.text", POISONOUS_BLOOD_TEXTURE), translateComponent("guide.expanded.poisonous_blood")));
        entries.put(ModSkills.Nodes.GARLIC_BLOOD.location(), new EntryText(imagePage(helper, "guide.expanded.garlic_blood.text", GARLIC_BLOOD_TEXTURE), translateComponent("guide.expanded.garlic_blood")));
        entries.put(ModSkills.Nodes.VAMPIRE_ROOT.location(), new EntryText(imagePage(helper, "guide.expanded.vampire.text", VAMPIRE_ROOT_TEXTURE), translateComponent("guide.expanded.vampire")));

        return entries;
    }

    private static List<IPage> imagePage(BookHelper helper, String translationKey, ResourceLocation image) {
        return helper.addLinks(new ArrayList<>(List.of(new PageTextImage(translateComponent(translationKey), image, true))));
    }

    private static ResourceLocation entry(String path) {
        return fromNamespaceAndPath(MOD_ID, path);
    }
}
