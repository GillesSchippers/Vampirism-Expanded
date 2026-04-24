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

package com.gustavoschip.expanded.compat.guideapi;

import com.gustavoschip.expanded.compat.guideapi.utils.GuideBookEntry;
import com.gustavoschip.expanded.compat.guideapi.utils.GuideBookScaledPageTextImage;
import com.gustavoschip.expanded.skill.holder.SkillHolders;
import com.gustavoschip.expanded.task.ModTasks;
import de.maxanier.guideapi.api.IPage;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.BookHelper;
import de.maxanier.guideapi.category.CategoryItemStack;
import de.maxanier.guideapi.page.PageText;
import de.teamlapen.vampirism.modcompat.guide.EntryText;
import de.teamlapen.vampirism.modcompat.guide.VampirismGuideBookCategoriesEvent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static de.teamlapen.vampirism.core.ModBlocks.VAMPIRE_ORCHID;
import static de.teamlapen.vampirism.modcompat.guide.GuideBook.translateComponent;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class GuideBookCompat {
    private static final int GUIDE_IMAGE_X = 60;
    private static final int GUIDE_IMAGE_Y = 12;
    private static final int GUIDE_IMAGE_WIDTH = 52;
    private static final int GUIDE_IMAGE_HEIGHT = 52;
    private static final int GUIDE_TEXT_Y_OFFSET = 58;

    protected GuideBookCompat() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(new GuideBookCompat());
    }

    public static void createCategoriesEvent(VampirismGuideBookCategoriesEvent event) {
        BookHelper helper = new BookHelper.Builder(MOD_ID).build();
        CategoryAbstract expandedCategory = new CategoryItemStack(buildExpandedEntries(helper), translateComponent("guide.expanded.title"), new ItemStack(VAMPIRE_ORCHID));
        helper.registerLinkablePages(new ArrayList<>(List.of(expandedCategory)));

        event.categories.add(Math.max(event.categories.size() - 1, 0), expandedCategory);
    }

    private static Map<ResourceLocation, EntryAbstract> buildExpandedEntries(BookHelper helper) {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
        entries.put(entry("overview"), new EntryText(textPage(helper, translateComponent("guide.expanded.overview.text")), translateComponent("guide.expanded.overview")));

        addEntry(entries, helper, ModTasks.TaskGuideEntries.TASKS);
//      addEntry(entries, helper, ModTasks.TaskGuideEntries.HUNTER_SKILL_POINTS_1);
//      addEntry(entries, helper, ModTasks.TaskGuideEntries.HUNTER_SKILL_POINTS_2);
//      addEntry(entries, helper, ModTasks.TaskGuideEntries.VAMPIRE_SKILL_POINTS_1);
//      addEntry(entries, helper, ModTasks.TaskGuideEntries.VAMPIRE_SKILL_POINTS_2);
        addEntry(entries, helper, SkillHolders.POISONOUS_BLOOD_GUIDE);
        addEntry(entries, helper, SkillHolders.GARLIC_BLOOD_GUIDE);
        addEntry(entries, helper, SkillHolders.VAMPIRIC_GROUNDING_GUIDE);
        addEntry(entries, helper, SkillHolders.ADVANCED_FLIGHT_GUIDE);

        return entries;
    }

    private static void addEntry(Map<ResourceLocation, EntryAbstract> entries, BookHelper helper, GuideBookEntry guideEntry) {
        entries.put(guideEntry.getId(), buildEntry(helper, guideEntry));
    }

    private static EntryAbstract buildEntry(BookHelper helper, GuideBookEntry guideEntry) {
        List<IPage> pages = guideEntry.getTexture() == null
                ? textPage(helper, guideEntry.getDescription())
                : imagePage(helper, guideEntry.getDescription(), guideEntry.getTexture());
        return new EntryText(pages, guideEntry.getName());
    }

    @SuppressWarnings("SameParameterValue")
    private static List<IPage> textPage(BookHelper helper, FormattedText text) {
        return helper.addLinks(new ArrayList<>(List.of(new PageText(text))));
    }

    @SuppressWarnings("SameParameterValue")
    private static List<IPage> imagePage(BookHelper helper, FormattedText text, ResourceLocation image) {
        return helper.addLinks(new ArrayList<>(List.of(new GuideBookScaledPageTextImage(
                text,
                image,
                GUIDE_TEXT_Y_OFFSET,
                GUIDE_IMAGE_X,
                GUIDE_IMAGE_Y,
                GUIDE_IMAGE_WIDTH,
                GUIDE_IMAGE_HEIGHT
        ))));
    }

    @SuppressWarnings("SameParameterValue")
    private static ResourceLocation entry(String path) {
        return fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public void onVampirismGuideBookCategories(VampirismGuideBookCategoriesEvent event) {
        createCategoriesEvent(event);
    }
}