package fr.akkun.newmeriacore.client;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Right-clicking with the Newmeria Manual opens it in the vanilla book screen. Its text is not in
 * the code: it is read, each time the book is opened, from a plain text file in the mod's assets,
 * {@code assets/newmeriacore/texts/newmeria_manual/<language>.txt} (the game language, e.g.
 * {@code fr_fr}; {@value #FALLBACK_LANGUAGE} when there is no file for that language).
 *
 * <p>File format: sections separated by a line containing only {@value #PAGE_SEPARATOR}, each
 * starting on a new page. Line breaks are kept and the usual {@code §} formatting codes work. A
 * section too long for one page simply continues on the next ones: the text never has to be fitted
 * to the page by hand.
 */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID, value = Dist.CLIENT)
public class ManualClientEvents {
    private static final String PAGE_SEPARATOR = "---";
    private static final String FALLBACK_LANGUAGE = "fr_fr";
    // What the vanilla book screen can show on a page (BookViewScreen: TEXT_WIDTH, TEXT_HEIGHT / line height).
    private static final int PAGE_TEXT_WIDTH = 114;
    private static final int LINES_PER_PAGE = 14;

    @SubscribeEvent
    static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getLevel().isClientSide() || !event.getItemStack().is(ModItems.NEWMERIA_MANUAL.get())) {
            return;
        }
        Minecraft.getInstance().gui.setScreen(new BookViewScreen(new BookViewScreen.BookAccess(loadPages())));
    }

    private static Identifier file(String language) {
        return Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "texts/newmeria_manual/" + language + ".txt");
    }

    private static List<Component> loadPages() {
        Minecraft minecraft = Minecraft.getInstance();
        Optional<Resource> resource = minecraft.getResourceManager().getResource(file(minecraft.getLanguageManager().getSelected()));
        if (resource.isEmpty()) {
            resource = minecraft.getResourceManager().getResource(file(FALLBACK_LANGUAGE));
        }
        if (resource.isEmpty()) {
            return List.of();
        }

        List<Component> pages = new ArrayList<>();
        StringBuilder section = new StringBuilder();
        try (BufferedReader reader = resource.get().openAsReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.strip().equals(PAGE_SEPARATOR)) {
                    pages.addAll(paginate(section.toString()));
                    section.setLength(0);
                } else {
                    section.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            NewmeriaCore.LOGGER.error("Could not read the Newmeria Manual text", e);
            return List.of();
        }
        pages.addAll(paginate(section.toString()));
        return pages;
    }

    /**
     * Turns {@code §} codes into real text styles. Left as raw codes, vanilla handles them poorly
     * in a book: {@code §r} resets to white instead of the page's black, and a style does not carry
     * over to the next line when the text wraps.
     */
    private static Component parseFormatting(String text) {
        MutableComponent result = Component.empty();
        Style style = Style.EMPTY;
        StringBuilder run = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            ChatFormatting format = c == '§' && i + 1 < text.length() ? ChatFormatting.getByCode(text.charAt(i + 1)) : null;
            if (format == null) {
                run.append(c);
                continue;
            }
            if (!run.isEmpty()) {
                result.append(Component.literal(run.toString()).withStyle(style));
                run.setLength(0);
            }
            // Same rules as vanilla's legacy codes: a colour clears bold/italic/..., "r" clears everything.
            style = style.applyLegacyFormat(format);
            i++;
        }
        if (!run.isEmpty()) {
            result.append(Component.literal(run.toString()).withStyle(style));
        }
        return result;
    }

    /** Wraps a section to the width of a page and cuts it into as many pages as it needs. */
    private static List<Component> paginate(String section) {
        List<FormattedText> lines = Minecraft.getInstance().font.getSplitter()
                .splitLines(parseFormatting(section.stripTrailing()), PAGE_TEXT_WIDTH, Style.EMPTY);

        List<Component> pages = new ArrayList<>();
        MutableComponent page = Component.empty();
        int linesOnPage = 0;
        for (FormattedText line : lines) {
            if (linesOnPage == LINES_PER_PAGE) {
                pages.add(page);
                page = Component.empty();
                linesOnPage = 0;
            }
            // A page the text overflowed onto never starts with the blank line of a paragraph break.
            if (linesOnPage == 0 && !pages.isEmpty() && line.getString().isBlank()) {
                continue;
            }
            if (linesOnPage > 0) {
                page.append("\n");
            }
            MutableComponent target = page;
            line.visit((style, text) -> {
                target.append(Component.literal(text).withStyle(style));
                return Optional.empty();
            }, Style.EMPTY);
            linesOnPage++;
        }
        pages.add(page);
        return pages;
    }
}
