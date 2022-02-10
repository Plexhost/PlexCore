package dk.plexhost.core.utils;

import dk.plexhost.core.PlexPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerUtils {

    /**
     *
     * Match an {@link OfflinePlayer}.
     *
     * @param paramString the name of the player to search for
     * @return the {@link OfflinePlayer} offline player if found
     */
    public static OfflinePlayer matchOfflinePlayer(String paramString) {
        OfflinePlayer player;
        try {
            UUID uuid = UUID.fromString(paramString);
            player = Bukkit.getOfflinePlayer(uuid);
            if (player.hasPlayedBefore() || player.isOnline()) return player;
        } catch (IllegalArgumentException ignored) {}
        player = Bukkit.getServer().getPlayerExact(paramString);
        if (player != null) return player;
        player = Bukkit.getServer().getOfflinePlayer(paramString);
        if (player.hasPlayedBefore()) return player;
        player = Bukkit.getServer().getPlayer(paramString);
        return player;
    }

    /**
     *
     * Match an {@link OfflinePlayer} asynchronous from their name.
     *
     * @param paramString name of the {@link OfflinePlayer}
     * @param paramBoolean whether to accept if the offline player is offline (set to true if you dont know)
     * @param paramConsumer the consumer to run when an {@link OfflinePlayer} is either found or not.
     */
    public static void matchOfflinePlayerAsync(String paramString, boolean paramBoolean, Consumer<OfflinePlayer> paramConsumer){
        Bukkit.getScheduler().runTaskAsynchronously(PlexPlugin.getInstance() , () -> {
            final OfflinePlayer offlinePlayer = matchOfflinePlayer(paramString);
            if(offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                paramConsumer.accept(null);
                return;
            }
            if(!offlinePlayer.isOnline())
                if(paramBoolean) paramConsumer.accept(offlinePlayer);
                else paramConsumer.accept(null);
            paramConsumer.accept(offlinePlayer);
        });
    }
}
