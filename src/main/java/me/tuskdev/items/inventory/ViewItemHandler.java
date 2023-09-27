package me.tuskdev.items.inventory;

@FunctionalInterface
public interface ViewItemHandler {

    void handle(ViewSlotContext context);

}
