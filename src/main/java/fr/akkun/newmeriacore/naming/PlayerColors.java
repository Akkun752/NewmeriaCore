package fr.akkun.newmeriacore.naming;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Persistent, server-wide mapping of player username -> chat/tab list name color. Stored as a
 * small hand-written YAML file: a flat "PlayerName: "§code"" mapping is simple enough not to need
 * a real YAML library. Loaded once (lazily) and kept in memory; every write goes straight back to
 * disk too.
 */
public final class PlayerColors {
    private static @Nullable Map<String, ChatFormatting> cache;

    private PlayerColors() {
    }

    public static @Nullable ChatFormatting get(MinecraftServer server, String username) {
        return colors(server).get(username);
    }

    public static void set(MinecraftServer server, String username, ChatFormatting color) {
        colors(server).put(username, color);
        writeToDisk(server, cache);
    }

    private static Map<String, ChatFormatting> colors(MinecraftServer server) {
        if (cache == null) {
            cache = load(server);
        }
        return cache;
    }

    private static Path file(MinecraftServer server) {
        return server.getFile("config/" + NewmeriaCore.MOD_ID + "/player_colors.yml");
    }

    private static Map<String, ChatFormatting> load(MinecraftServer server) {
        Path file = file(server);
        if (!Files.exists(file)) {
            Map<String, ChatFormatting> defaults = new LinkedHashMap<>();
            defaults.put("Akkun_7", ChatFormatting.BLUE);
            defaults.put("Falnix", ChatFormatting.DARK_PURPLE);
            defaults.put("RaphaAile", ChatFormatting.RED);
            defaults.put("woohtyti", ChatFormatting.YELLOW);
            defaults.put("bats01", ChatFormatting.GREEN);
            defaults.put("Dev", ChatFormatting.BLUE);
            writeToDisk(server, defaults);
            return defaults;
        }

        Map<String, ChatFormatting> result = new LinkedHashMap<>();
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int separator = trimmed.indexOf(':');
                if (separator < 0) {
                    continue;
                }
                String name = trimmed.substring(0, separator).trim();
                String value = trimmed.substring(separator + 1).trim();
                if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                    value = value.substring(1, value.length() - 1);
                }
                if (value.length() == 2 && value.charAt(0) == ChatFormatting.PREFIX_CODE) {
                    ChatFormatting color = ChatFormatting.getByCode(value.charAt(1));
                    if (color != null && !name.isEmpty()) {
                        result.put(name, color);
                    }
                }
            }
        } catch (IOException e) {
            NewmeriaCore.LOGGER.error("Failed to read {}", file, e);
        }
        return result;
    }

    private static void writeToDisk(MinecraftServer server, Map<String, ChatFormatting> data) {
        Path file = file(server);
        try {
            Files.createDirectories(file.getParent());
            StringBuilder builder = new StringBuilder("# NewmeriaCore player name colors - one \"PlayerName: \\\"§code\\\"\" pair per line\n");
            for (Map.Entry<String, ChatFormatting> entry : data.entrySet()) {
                builder.append(entry.getKey()).append(": \"").append(entry.getValue()).append("\"\n");
            }
            Files.writeString(file, builder.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            NewmeriaCore.LOGGER.error("Failed to write {}", file, e);
        }
    }
}
