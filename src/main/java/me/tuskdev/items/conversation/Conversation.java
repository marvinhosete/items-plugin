package me.tuskdev.items.conversation;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class Conversation {

    private final Player target;
    private final ConversationType type;
    private String query;
    private boolean completed = false;
    private Consumer<ConversationResponse> callback;

    public Conversation(Player target, ConversationType type) {
        this.target = target;
        this.type = type;
    }

    void complete(Object object) {
        this.callback.accept(new ConversationResponse(object));
        this.completed = true;
    }

    public void send() {
        if (ConversationListener.isRegistered(target.getUniqueId())) {
            completed = true;
            return;
        }

        ConversationListener.register(this);

        target.sendMessage("");
        target.sendMessage("§e" + (query != null ? query : "§cPergunta não especificada."));

        ConversationUtil.CONVERSATION_MESSAGE.get(type).send(target);

        target.sendMessage("§7Você possui um minuto para responder.");
        target.sendMessage("");
        target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
    }

    public Player getTarget() {
        return target;
    }

    public ConversationType getType() {
        return type;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public Consumer<ConversationResponse> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<ConversationResponse> callback) {
        this.callback = callback;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}