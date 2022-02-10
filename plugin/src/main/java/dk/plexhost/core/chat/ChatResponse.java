package dk.plexhost.core.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public final class ChatResponse {

    /**
     * Create a consumer which is called on chat.
     * Usage:

     ChatResponse.builder()
         .messages(String)
         .onComplete((Player, String) -> {
            if (message.equalsIgnoreCase("!cancel")) {
                whoAnswered.sendMessage(String);
                return;
            }
            # Check the response
        })
        .build()
        .addPlayer(player)
     */

    private final static WeakHashMap<UUID, ChatResponse> asks = new WeakHashMap<>();

    static {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatResponse.class);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                ChatResponse question;
                if ((question = asks.get(event.getPlayer().getUniqueId())) != null) {
                    event.setCancelled(true);

                    final BiConsumer<Player, String> consumer = question.getOnComplete();

                    final String message = event.getMessage();
                    final Player player = event.getPlayer();

                    asks.remove(player.getUniqueId());
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        consumer.accept(player, message);
                    });
                }
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                asks.remove(event.getPlayer().getUniqueId());
            }

            @EventHandler
            public void onGuiOpen(InventoryOpenEvent event) {
                asks.remove(event.getPlayer().getUniqueId());
            }
        }, plugin);
    }

    private final String[] messages;
    private final BiConsumer<Player, String> onComplete;

    private ChatResponse(Builder builder) {
        messages = builder.messages;
        onComplete = builder.onComplete;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addPlayer(Player player) {
        asks.put(player.getUniqueId(), this);
        player.sendMessage(messages);
    }

    public BiConsumer<Player, String> getOnComplete() {
        return onComplete;
    }

    public String[] getMessages() {
        return messages;
    }

    public static final class Builder {

        private String[] messages;
        private BiConsumer<Player, String> onComplete;

        private Builder() {
        }

        public Builder messages(String... messages) {
            this.messages = messages;
            return this;
        }

        public Builder messages(List<String> messages) {
            this.messages = messages.toArray(new String[0]);
            return this;
        }

        public Builder onComplete(BiConsumer<Player, String> onComplete) {
            this.onComplete = onComplete;
            return this;
        }

        public ChatResponse build() {
            return new ChatResponse(this);
        }

    }
}