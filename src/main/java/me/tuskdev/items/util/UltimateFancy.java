package me.tuskdev.items.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.md_5.bungee.chat.ComponentSerializer;

public class UltimateFancy {

    private final JSONArray constructor = new JSONArray();
    private final Map<String, Boolean> lastFormats = new HashMap<>();
    private ChatColor lastColor = ChatColor.WHITE;
    private List<JSONObject> workingGroup = new ArrayList<>();
    private List<ExtraElement> pendentElements = new ArrayList<>();

    public UltimateFancy() {
    }

    /**
     * Creates a new instance of UltimateFancy with an initial text.
     *
     * @param text {@code String}
     */
    public UltimateFancy(String text) {
        text(text);
    }

    /**
     * @param text
     * @return instance of same {@link UltimateFancy}.
     */
    public UltimateFancy text(String text) {
        for (String part : text.split("(?=" + ChatColor.COLOR_CHAR + ")")) {
            JSONObject workingText = new JSONObject();

            // fix colors before
            filterColors(workingText);

            Matcher match = Pattern.compile("^" + ChatColor.COLOR_CHAR + "([0-9a-fk-or]).*$").matcher(part);
            if (match.find()) {
                lastColor = ChatColor.getByChar(match.group(1).charAt(0));
                // fix colors from latest
                filterColors(workingText);
                if (part.length() == 2)
                    continue;
            }
            // continue if empty
            if (ChatColor.stripColor(part).isEmpty()) {
                continue;
            }
            workingText.put("text", ChatColor.stripColor(part));

            // fix colors after
            filterColors(workingText);

            if (!workingText.containsKey("color")) {
                workingText.put("color", "white");
            }
            workingGroup.add(workingText);
        }
        return this;
    }

    private JSONObject filterColors(JSONObject obj) {
        for (Entry<String, Boolean> format : lastFormats.entrySet()) {
            obj.put(format.getKey(), format.getValue());
        }
        if (lastColor.isFormat()) {
            String formatStr = lastColor.name().toLowerCase();
            if (lastColor.equals(ChatColor.MAGIC)) {
                formatStr = "obfuscated";
            }
            if (lastColor.equals(ChatColor.UNDERLINE)) {
                formatStr = "underlined";
            }
            lastFormats.put(formatStr, true);
            obj.put(formatStr, true);
        }
        if (lastColor.isColor()) {
            obj.put("color", lastColor.name().toLowerCase());
        }
        if (lastColor.equals(ChatColor.RESET)) {
            obj.put("color", "white");
            for (String format : lastFormats.keySet()) {
                lastFormats.put(format, false);
                obj.put(format, false);
            }
        }
        return obj;
    }

    /**
     * Close the last text properties and start a new text block.
     *
     * @return instance of same {@link UltimateFancy}.
     */
    public UltimateFancy next() {
        if (workingGroup.size() > 0) {
            for (JSONObject obj : workingGroup) {
                if (obj.containsKey("text") && obj.get("text").toString().length() > 0) {
                    for (ExtraElement element : pendentElements) {
                        obj.put(element.getAction(), element.getJson());
                    }
                    constructor.add(obj);
                }
            }
        }
        workingGroup = new ArrayList<JSONObject>();
        pendentElements = new ArrayList<ExtraElement>();
        return this;
    }

    /**
     * Add a command to execute on click the text.
     *
     * @param cmd {@link String}
     * @return instance of same {@link UltimateFancy}.
     */
    public UltimateFancy clickRunCmd(String cmd) {
        pendentElements.add(new ExtraElement("clickEvent", parseJson("run_command", cmd)));
        return this;
    }

    /**
     * @param cmd {@link String}
     * @return instance of same {@link UltimateFancy}.
     */
    public UltimateFancy clickSuggestCmd(String cmd) {
        pendentElements.add(new ExtraElement("clickEvent", parseJson("suggest_command", cmd)));
        return this;
    }

    /**
     * URL to open on external browser when click this text.
     *
     * @param url {@link String}
     * @return instance of same {@link UltimateFancy}.
     */
    public UltimateFancy clickOpenURL(String url) {
        pendentElements.add(new ExtraElement("clickEvent", parseJson("open_url", url)));
        return this;
    }

    /**
     * Text to show on hover the mouse under this text.
     *
     * @param text {@link String}
     * @return instance of same {@link UltimateFancy}.
     */
    public UltimateFancy hoverShowText(String text) {
        pendentElements.add(new ExtraElement("hoverEvent", parseHoverText(text)));
        return this;
    }

    private JSONObject parseHoverText(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        JSONArray extraArr = addColorToArray(text);
        JSONObject objExtra = new JSONObject();
        objExtra.put("text", "");
        objExtra.put("extra", extraArr);
        JSONObject obj = new JSONObject();
        obj.put("action", "show_text");
        obj.put("value", objExtra);
        return obj;
    }

    private JSONObject parseJson(String action, String value) {
        JSONObject obj = new JSONObject();
        obj.put("action", action);
        obj.put("value", value);
        return obj;
    }

    private JSONArray addColorToArray(String text) {
        JSONArray extraArr = new JSONArray();
        ChatColor color = ChatColor.WHITE;
        for (String part : text.split("(?=" + ChatColor.COLOR_CHAR + "[0-9a-fk-or])")) {
            JSONObject objExtraTxt = new JSONObject();
            Matcher match = Pattern.compile("^" + ChatColor.COLOR_CHAR + "([0-9a-fk-or]).*$").matcher(part);
            if (match.find()) {
                color = ChatColor.getByChar(match.group(1).charAt(0));
                if (part.length() == 2)
                    continue;
            }
            objExtraTxt.put("text", ChatColor.stripColor(part));
            if (color.isColor()) {
                objExtraTxt.put("color", color.name().toLowerCase());
            }
            if (color.equals(ChatColor.RESET)) {
                objExtraTxt.put("color", "white");
            }
            if (color.isFormat()) {
                if (color.equals(ChatColor.MAGIC)) {
                    objExtraTxt.put("obfuscated", true);
                } else {
                    objExtraTxt.put(color.name().toLowerCase(), true);
                }
            }
            extraArr.add(objExtraTxt);
        }
        return extraArr;
    }

    public String toJson() {
        return "[\"\"," + constructor.toJSONString().substring(1);
    }

    public void send(Player p) {
        next();
        p.spigot().sendMessage(ComponentSerializer.parse(this.toJson()));
    }

    public void sendAll() {
        next();
        BaseComponent[] message = ComponentSerializer.parse(this.toJson());

        for (Player p : Bukkit.getOnlinePlayers()) p.spigot().sendMessage(message);
    }

    /**
     * An imutable pair of actions and {@link JSONObject} values.
     *
     * @author FabioZumbi12
     */
    public class ExtraElement {
        private String action;
        private JSONObject json;

        public ExtraElement(String action, JSONObject json) {
            this.action = action;
            this.json = json;
        }

        public String getAction() {
            return this.action;
        }

        public JSONObject getJson() {
            return this.json;
        }
    }

}