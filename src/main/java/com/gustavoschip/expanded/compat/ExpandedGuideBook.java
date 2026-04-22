package com.gustavoschip.expanded.compat;

import com.gustavoschip.expanded.skill.ModSkills;
import com.mojang.blaze3d.systems.RenderSystem;
import de.maxanier.guideapi.api.IPage;
import de.maxanier.guideapi.api.impl.Book;
import de.maxanier.guideapi.api.impl.Page;
import de.maxanier.guideapi.api.impl.abstraction.CategoryAbstract;
import de.maxanier.guideapi.api.impl.abstraction.EntryAbstract;
import de.maxanier.guideapi.api.util.BookHelper;
import de.maxanier.guideapi.api.util.GuiHelper;
import de.maxanier.guideapi.category.CategoryItemStack;
import de.maxanier.guideapi.gui.BaseScreen;
import de.maxanier.guideapi.page.PageText;
import de.teamlapen.vampirism.modcompat.guide.EntryText;
import de.teamlapen.vampirism.modcompat.guide.VampirismGuideBookCategoriesEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gustavoschip.expanded.Expanded.MOD_ID;
import static de.teamlapen.vampirism.core.ModBlocks.VAMPIRE_ORCHID;
import static de.teamlapen.vampirism.modcompat.guide.GuideBook.translateComponent;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class ExpandedGuideBook {
    private static final int GUIDE_IMAGE_X = 60;
    private static final int GUIDE_IMAGE_Y = 12;
    private static final int GUIDE_IMAGE_WIDTH = 52;
    private static final int GUIDE_IMAGE_HEIGHT = 52;
    private static final int GUIDE_TEXT_Y_OFFSET = 58;

    private static final ResourceLocation POISONOUS_BLOOD_TEXTURE = fromNamespaceAndPath(MOD_ID, "textures/skills/poisonous_blood.png");
    private static final ResourceLocation GARLIC_BLOOD_TEXTURE = fromNamespaceAndPath(MOD_ID, "textures/skills/garlic_blood.png");

    @SubscribeEvent
    public void onVampirismGuideBookCategories(VampirismGuideBookCategoriesEvent event) {
        createCategoriesEvent(event);
    }

    public static void createCategoriesEvent(VampirismGuideBookCategoriesEvent event) {
        BookHelper helper = new BookHelper.Builder(MOD_ID).build();
        CategoryAbstract expandedCategory = new CategoryItemStack(buildExpandedEntries(helper), translateComponent("guide.expanded.title"), new ItemStack(VAMPIRE_ORCHID));
        helper.registerLinkablePages(new ArrayList<>(List.of(expandedCategory)));

        event.categories.add(Math.max(event.categories.size() - 1, 0), expandedCategory);
    }

    private static Map<ResourceLocation, EntryAbstract> buildExpandedEntries(BookHelper helper) {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();

        entries.put(entry("overview"), new EntryText(textPage(helper, "guide.expanded.overview.text"), translateComponent("guide.expanded.overview")));
        entries.put(ModSkills.Nodes.POISONOUS_BLOOD.location(), new EntryText(imagePage(helper, "guide.expanded.poisonous_blood.text", POISONOUS_BLOOD_TEXTURE), translateComponent("guide.expanded.poisonous_blood")));
        entries.put(ModSkills.Nodes.GARLIC_BLOOD.location(), new EntryText(imagePage(helper, "guide.expanded.garlic_blood.text", GARLIC_BLOOD_TEXTURE), translateComponent("guide.expanded.garlic_blood")));

        return entries;
    }

    private static List<IPage> textPage(BookHelper helper, String translationKey) {
        return helper.addLinks(new ArrayList<>(List.of(new PageText(
                translateComponent(translationKey)
        ))));
    }

    private static List<IPage> imagePage(BookHelper helper, String translationKey, ResourceLocation image) {
        return helper.addLinks(new ArrayList<>(List.of(new ScaledPageTextImage(
                translateComponent(translationKey),
                image,
                GUIDE_TEXT_Y_OFFSET,
                GUIDE_IMAGE_X,
                GUIDE_IMAGE_Y,
                GUIDE_IMAGE_WIDTH,
                GUIDE_IMAGE_HEIGHT
        ))));
    }

    private static ResourceLocation entry(String path) {
        return fromNamespaceAndPath(MOD_ID, path);
    }
}

class ScaledPageTextImage extends Page {
    private final PageText pageText;
    private final ResourceLocation image;
    private final int imageX;
    private final int imageY;
    private final int imageWidth;
    private final int imageHeight;

    public ScaledPageTextImage(FormattedText text, ResourceLocation image, int textYOffset, int imageX, int imageY, int imageWidth, int imageHeight) {
        this.pageText = new PageText(text, textYOffset);
        this.image = image;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void draw(GuiGraphics graphics, RegistryAccess registryAccess, Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, BaseScreen guiBase, Font fontRendererObj) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, image);
        GuiHelper.drawSizedIconWithoutColor(graphics, guiLeft + imageX, guiTop + imageY, imageWidth, imageHeight, 0);

        pageText.draw(graphics, registryAccess, book, category, entry, guiLeft, guiTop, mouseX, mouseY, guiBase, fontRendererObj);
    }
}