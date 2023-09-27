package me.tuskdev.items.conversation;

public class ConversationResponse {

    private final Object response;

    public ConversationResponse(Object response) {
        this.response = response;
    }

    public <T> T getResponse() {
        return (T) this.response;
    }

}
