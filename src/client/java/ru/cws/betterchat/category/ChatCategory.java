package ru.cws.betterchat.category;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.ArrayListDeque;
import ru.cws.betterchat.gui.api.IChatHud;

public abstract class ChatCategory {
    public final String id;
    public String name;
    public String description;
    public ArrayListDeque<Text> messages;

    public ChatCategory(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.messages = new ArrayListDeque<>(100);
    }

    public void onOpen() {
        this.refreshMessages();
    }

    public void refreshMessages() {
        ((IChatHud) MinecraftClient.getInstance().inGameHud.getChatHud()).BetterChat$setMessages(this.messages);
    }

    public abstract String formatToSend(String message);

    public abstract boolean tryAccept(Text message);
    public abstract void tryAcceptSelf(Text message);
    public abstract void tryAcceptSelf(String message);

    public void accept(Text message) {
        if (messages.size() == 100)
            this.messages.removeLast();
        this.messages.addFirst(message);
    }

    public static Text createMessage(String prefix, String sender, String message) {
        return Text.literal(createStringMessage(prefix, sender, message));
    }

    public static String createStringMessage(String prefix, String sender, String message) {
        return prefix + " §r§7<" + (sender == null ? "§4§oSystem" : ("§3§o" + sender)) + "§r§7> §r§f" + message;
    }

    public static String textToString(Text message) {
        var ordered = message.asOrderedText();
        var builder = new StringBuilder();
        ordered.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }

    public static String textToFormattedeString(Text message) {
        var ordered = message.asOrderedText();
        var builder = new StringBuilder();
        final Style[] lastStyle = {Style.EMPTY};
        ordered.accept((index, style, codePoint) -> {
            if (lastStyle[0] != style) {
                builder.append("§r");
                if (style.isObfuscated())
                    builder.append("§k");
                if (style.isBold())
                    builder.append("§l");
                if (style.isStrikethrough())
                    builder.append("§m");
                if (style.isUnderlined())
                    builder.append("§n");
                if (style.isItalic())
                    builder.append("§o");
                if (style.getColor() != null)
                    builder.append(Formatting.byName(style.getColor().getName()));
                lastStyle[0] = style;
            }
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }
}
