package dk.plexhost.core.title;

import org.bukkit.entity.Player;

import java.util.Optional;

public class TitleAPI {

    static InternalTitleAPI internalApi = InternalAPIMapping.create();

    @Deprecated
    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, message, null);
    }

    @Deprecated
    public static void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, null, message);
    }

    @Deprecated
    public static void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
    }

    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {


        InternalTitleAPI internalApi = getInternalApi().orElseThrow(() -> new RuntimeException("No internal API, Unsupported version?"));


        internalApi.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void clearTitle(Player player) {

        InternalTitleAPI internalApi = getInternalApi().orElseThrow(() -> new RuntimeException("No internal API, Unsupported version?"));
        internalApi.clearTitle(player);
    }

    static Optional<InternalTitleAPI> getInternalApi() {
        return Optional.ofNullable(internalApi);
    }


}