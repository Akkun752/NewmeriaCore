package fr.akkun.newmeriacore.manual;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Gathers the chapters of the Newmeria Manual from the data of every mod (and datapack) on the server.
 *
 * <p>A mod adds chapters to the manual by shipping text files at
 * {@code data/<modid>/newmeria_manual/<language>/<NNN>_<name>.txt}, e.g.
 * {@code data/newmeriaservercore/newmeria_manual/fr_fr/500_commandes.txt}. Nothing else is needed: no
 * code, and the mod can be installed on the server only, since the server sends the chapters to the players.
 *
 * <ul>
 *   <li>Chapters of all the mods are put in the order of their file names, so the number at the start of
 *       the name decides where a chapter goes (000 = first, 900 = last).</li>
 *   <li>A chapter missing in a language is taken from {@value #FALLBACK_LANGUAGE}.</li>
 *   <li>The text format is described in {@code ManualClientEvents}: sections separated by a {@code ---} line.</li>
 * </ul>
 */
public final class ManualChapters {
    public static final String DIRECTORY = "newmeria_manual";
    public static final String FALLBACK_LANGUAGE = "fr_fr";
    private static final String EXTENSION = ".txt";

    private ManualChapters() {
    }

    /** @return for each language, the text of all its chapters, in order */
    public static Map<String, List<String>> load(ResourceManager resourceManager) {
        // language -> "<file name>@<namespace>" -> text. Sorting on that key puts the chapters in order.
        Map<String, Map<String, String>> byLanguage = new HashMap<>();
        Map<Identifier, Resource> files = resourceManager.listResources(DIRECTORY, id -> id.getPath().endsWith(EXTENSION));
        for (Map.Entry<Identifier, Resource> file : files.entrySet()) {
            Identifier id = file.getKey();
            String[] parts = id.getPath().substring(DIRECTORY.length() + 1).split("/");
            if (parts.length != 2) {
                NewmeriaCore.LOGGER.warn("Ignoring manual chapter {}: expected {}/<language>/<chapter>{}", id, DIRECTORY, EXTENSION);
                continue;
            }
            String language = parts[0];
            String chapter = parts[1].substring(0, parts[1].length() - EXTENSION.length()) + "@" + id.getNamespace();
            try (BufferedReader reader = file.getValue().openAsReader()) {
                String text = reader.lines().collect(Collectors.joining("\n"));
                byLanguage.computeIfAbsent(language, l -> new HashMap<>()).put(chapter, text);
            } catch (IOException e) {
                NewmeriaCore.LOGGER.error("Could not read manual chapter {}", id, e);
            }
        }

        Map<String, String> fallback = byLanguage.getOrDefault(FALLBACK_LANGUAGE, Map.of());
        Map<String, List<String>> result = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> language : byLanguage.entrySet()) {
            Map<String, String> chapters = new TreeMap<>(fallback);
            chapters.putAll(language.getValue());
            result.put(language.getKey(), List.copyOf(chapters.values()));
        }
        return result;
    }
}
