package me.tuskdev.items.conversation;
import com.google.common.cache.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConversationListener implements Listener {

    private static final Cache<UUID, Conversation> CONVERSATIONS = CacheBuilder.newBuilder().removalListener((RemovalListener<UUID, Conversation>) removalNotification -> {
        if (removalNotification.getCause() == RemovalCause.EXPIRED) {
            Player player = Bukkit.getPlayer(removalNotification.getKey());
            if (player != null) player.sendMessage("§cSeu tempo de resposta acabou.");
        }
    }).expireAfterWrite(1, TimeUnit.MINUTES).build();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        String message = event.getMessage();

        Conversation conversation = CONVERSATIONS.getIfPresent(player.getUniqueId());
        if (conversation == null || conversation.isCompleted()) return;

        event.setCancelled(true);

        CONVERSATIONS.invalidate(player.getUniqueId());

        if (message.startsWith("conversation ")) {
            try {
                conversation.complete(Boolean.parseBoolean(message.replace("conversation ", "")));
                return;
            } catch (Exception ignored) {}
        }

        if (message.equalsIgnoreCase("cancelar")) {
            conversation.complete(null);
            return;
        }

        conversation.complete(message);
    }

    static void register(Conversation conversation) {
        CONVERSATIONS.put(conversation.getTarget().getUniqueId(), conversation);
    }

    static void remove(Conversation conversation) {
        CONVERSATIONS.invalidate(conversation.getTarget().getUniqueId());
    }

    static boolean isRegistered(UUID uuid) {
        return CONVERSATIONS.getIfPresent(uuid) != null;
    }

}
