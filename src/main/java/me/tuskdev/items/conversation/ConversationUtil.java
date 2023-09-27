package me.tuskdev.items.conversation;

import com.google.common.collect.ImmutableMap;
import me.tuskdev.items.util.UltimateFancy;

import java.util.Map;

public class ConversationUtil {

    static final Map<ConversationType, UltimateFancy> CONVERSATION_MESSAGE = ImmutableMap.of(
            ConversationType.BOOLEAN, new UltimateFancy("§7Clique ").next().text("§a§lAQUI").hoverShowText("§aConcordar").clickRunCmd("conversation true").next().text(" §r§7para concordar e ").next().text("§c§lAQUI").hoverShowText("§cNegar").clickRunCmd("conversation false").next().text(" §r§7para negar."),
            ConversationType.TEXT, new UltimateFancy("§7Responda no chat ou clique ").next().text("§c§lAQUI").clickRunCmd("cancelar").next().text(" §r§7para cancelar.")
    );

}
