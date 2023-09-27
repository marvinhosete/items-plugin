package me.tuskdev.items.view;

import com.google.common.collect.ImmutableMap;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.conversation.Conversation;
import me.tuskdev.items.conversation.ConversationType;
import me.tuskdev.items.enums.ItemAttribute;
import me.tuskdev.items.inventory.View;
import me.tuskdev.items.inventory.ViewContext;
import me.tuskdev.items.util.Configuration;
import me.tuskdev.items.util.ItemBuilder;
import me.tuskdev.items.util.NumberUtil;
import org.bukkit.Material;

public class ArmorsAttributesView extends View {

    private final Configuration itemsConfiguration;

    public ArmorsAttributesView(Configuration itemsConfiguration) {
        super(3, "Atributos de Armas");

        setCancelOnClick(true);

        slot(0, ItemBuilder.builder(Material.ARROW).name("§aVoltar").build()).onClick(handler -> handler.open(CustomItemView.class, true));

        this.itemsConfiguration = itemsConfiguration;
    }

    @Override
    protected void onRender(ViewContext context) {
        String id = context.get("id");
        CustomItem customItem = context.get("item");

        ItemAttribute[] armorsAttributes = ItemAttribute.getArmorsAttributes();
        for (int i = 0; i < armorsAttributes.length; i++) {
            ItemAttribute attribute = armorsAttributes[i];
            int value = customItem.getAttribute(attribute);

            context.slot(i+11, ItemBuilder.builder(Material.INK_SACK).durability(value == 0 ? 8 : 10).name("§a" + attribute.getDescription()).lore("§7Valor atual: §f" + (value == 0 ? "Nenhum" : value), "", "§7Clique para alterar o valor.").build()).onClick(handler -> {
                handler.closeNow();

                Conversation conversation = new Conversation(handler.getPlayer(), ConversationType.TEXT);
                conversation.setQuery("§eDigite o valor do atributo, use 0 para remover:");
                conversation.setCallback(response -> {
                    if (response == null || response.getResponse().equals("cancelar")) {
                        open(handler.getPlayer(), ImmutableMap.of("id", id, "item", customItem));
                        return;
                    }

                    int newValue = NumberUtil.tryParseInt(response.getResponse());

                    if (newValue < 0) {
                        handler.getPlayer().sendMessage("§cO valor informado não pode ser menor que 0.");
                        return;
                    }

                    if (newValue == 0) customItem.removeAttribute(attribute);
                    else customItem.addAttribute(attribute, newValue);
                    itemsConfiguration.set("items." + id, customItem);
                    itemsConfiguration.save();

                    open(handler.getPlayer(), ImmutableMap.of("id", id, "item", customItem));
                    handler.getPlayer().sendMessage("§aValor alterado com sucesso.");
                });
                conversation.send();
            });
        }
    }
}
