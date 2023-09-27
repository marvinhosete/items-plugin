package me.tuskdev.items.view;

import com.google.common.collect.ImmutableMap;
import me.tuskdev.items.config.RarityListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.conversation.Conversation;
import me.tuskdev.items.conversation.ConversationType;
import me.tuskdev.items.factory.CustomItemFactory;
import me.tuskdev.items.inventory.View;
import me.tuskdev.items.inventory.ViewContext;
import me.tuskdev.items.util.Configuration;
import me.tuskdev.items.util.ItemBuilder;
import me.tuskdev.items.util.UltimateFancy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

public class CustomItemView extends View {

    private final RarityListManager rarityListManager;
    private final Configuration itemsConfiguration;

    public CustomItemView(RarityListManager rarityListManager, Configuration itemsConfiguration) {
        super(3, "Editor");

        setCancelOnClick(true);

        slot(13, ItemBuilder.builder(Material.DIAMOND_HOE).name("§aFerramentas").lore("§7Clique para editar os atributos", "§7da categoria de ferramentas.").build()).onClick(handler -> handler.open(ToolsAttributesView.class, true));
        slot(14, ItemBuilder.builder(Material.DIAMOND_SWORD).name("§aArmas").lore("§7Clique para editar os atributos", "§7da categoria de armas.").build()).onClick(handler -> handler.open(ArmorsAttributesView.class, true));
        slot(15, ItemBuilder.builder(Material.DIAMOND_CHESTPLATE).name("§aArmaduras").lore("§7Clique para editar os atributos", "§7da categoria de armaduras.").build()).onClick(handler -> handler.open(WeaponsAttributesView.class, true));

        this.rarityListManager = rarityListManager;
        this.itemsConfiguration = itemsConfiguration;
    }

    @Override
    protected void onRender(ViewContext context) {
        String id = context.get("id");
        CustomItem customItem = context.get("item");

        context.slot(11, ItemBuilder.builder(CustomItemFactory.build(null, customItem, rarityListManager)).addLore(999, "").addLore(999, "§7Botão esquerdo: §fAlterar nome").addLore(999, "§7Botão direito: §fAlterar raridade").build()).onClick(handler -> {
            ClickType clickType = handler.getClickOrigin().getClick();
            if (!clickType.isLeftClick() && !clickType.isRightClick()) return;

            handler.closeNow();

            Conversation conversation = new Conversation(handler.getPlayer(), ConversationType.TEXT);
            conversation.setQuery(clickType.isLeftClick() ? "§eDigite o nome do item:" : "§eDigite a raridade do item:");
            conversation.setCallback(response -> {
                String value = response.getResponse();
                if (value == null) {
                    open(handler.getPlayer(), ImmutableMap.of("id", id, "item", customItem));
                    return;
                }

                if (clickType.isRightClick()) {
                    if (rarityListManager.getRarity(value) == null) {
                        handler.getPlayer().sendMessage("§cA raridade informada não existe.");
                        return;
                    }
                }

                customItem.setName(value);
                itemsConfiguration.set("items." + id, customItem);
                itemsConfiguration.save();
                handler.getPlayer().sendMessage(clickType.isLeftClick() ? "§aNome alterado com sucesso." : "§aRaridade alterada com sucesso.");

                open(handler.getPlayer(), ImmutableMap.of("id", id, "item", customItem));
            });
            conversation.send();
        });
    }
}
