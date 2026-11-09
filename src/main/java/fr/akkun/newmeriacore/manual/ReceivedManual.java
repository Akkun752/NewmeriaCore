package fr.akkun.newmeriacore.manual;

import java.util.List;
import java.util.Map;

/** Client side: the chapters of the manual last sent by the server, forgotten when leaving it. */
public final class ReceivedManual {
    private static Map<String, List<String>> chapters = Map.of();

    private ReceivedManual() {
    }

    public static void set(Map<String, List<String>> chapters) {
        ReceivedManual.chapters = Map.copyOf(chapters);
    }

    public static void clear() {
        chapters = Map.of();
    }

    /** @return the chapters in that language, or in {@link ManualChapters#FALLBACK_LANGUAGE} if there are none */
    public static List<String> chapters(String language) {
        List<String> inLanguage = chapters.get(language);
        return inLanguage != null ? inLanguage : chapters.getOrDefault(ManualChapters.FALLBACK_LANGUAGE, List.of());
    }
}
