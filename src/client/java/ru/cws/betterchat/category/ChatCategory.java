package ru.cws.betterchat.category;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.collection.ArrayListDeque;
import org.jetbrains.annotations.Nullable;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.util.IChatHud;

import java.util.Objects;
import java.util.regex.Pattern;

public class ChatCategory {
    public String name;
    public String description;
    public String category;
    public String command;
    public String prefix;
    public boolean gl;
    //
    public ArrayListDeque<Text> messages;
    public int cacheHash = 0;
    public Pattern cachePattern = null;

    public ChatCategory(String name, String description, String category, String command, String prefix, boolean gl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.command = command;
        this.prefix = prefix;
        this.gl = gl;
        this.messages = new ArrayListDeque<>(100);
        this.cacheHash = 0;
        this.cachePattern = null;
    }

    public void onOpen() {
        this.refreshMessages();
        BetterChatMod.tryCommand(MinecraftClient.getInstance(), Objects.requireNonNullElseGet(this.command, () -> !this.gl || BetterChatMod.GLOBAL_CHAT ? "gc" : "lc"));
    }

    public void refreshMessages() {
        ((IChatHud) MinecraftClient.getInstance().inGameHud.getChatHud()).BetterChat$setMessages(this.messages);
    }

    public String formatToSend(String message) {
        var msg = message;
        if (this.prefix != null)
            msg = this.prefix + msg;
        if (this.category != null)
            msg = "[" + this.category + "] " + msg;
        return msg;
    }

    public void tryAccept(Text message) {
        var msg = checkReplaceAccepting(message, this.getPattern());
        if (msg != null) {
            this.accept(msg);
        }
    }

    public Pattern getPattern() {
        if (this.hashCode() != this.cacheHash) {
            var pattern = new StringBuilder("\\s*");
            if (this.prefix != null)
                pattern.append(this.prefix).append("\\s*");
            if (this.category != null)
                pattern.append("\\[").append(this.category).append("\\]\\s*");
            this.cachePattern = Pattern.compile(pattern.toString());
            this.cacheHash = this.hashCode();
        }
        return this.cachePattern;
    }

    public void accept(Text message) {
        if (messages.size() == 100)
            this.messages.removeLast();
        this.messages.addFirst(message);
    }

    protected static @Nullable Text checkReplaceAccepting(Text message, Pattern regex) {
        var content = message.getContent();
        switch (content.getType().id()) {
            case "text" -> {
                var text = ((PlainTextContent) content).string();
                if (text.isEmpty()) {
                    var sibling = message.getSiblings().getFirst();
                    if (sibling == null)
                        return null;
                    return checkReplaceAccepting(sibling, regex);
                }
                var matcher = regex.matcher(text);
                if (matcher.find()) {
                    return Text.literal(matcher.replaceAll("")).setStyle(message.getStyle());
                }
            }
            case "translatable" -> {
                var translatable =  (TranslatableTextContent) content;
                switch (translatable.getKey()) {
                    case "chat.type.text" -> {
                        var text = translatable.getArg(1).getString();
                        var matcher = regex.matcher(text);
                        if (matcher.find()) {
                            translatable.getArgs()[1] = Text.literal(matcher.replaceAll("")).setStyle(((Text) translatable.getArgs()[1]).getStyle());
                            return Text.translatable("chat.type.text", translatable.getArgs());
                        }
                    }
                    case "command.unknown.command" -> {
                        var matcher = regex.matcher("Неизвестная команда");
                        if (matcher.find()) {
                            return Text.literal(matcher.replaceAll("")).setStyle(message.getStyle());
                        }
                    }
                    default -> {
                        if (BetterChatMod.NO_THROW) {
                            BetterChatMod.LOGGER.trace("Fail to parse translatable conent: {}", translatable);
                            return null;
                        }
                        throw new RuntimeException("Unsupported text type: " + translatable.getKey());
                    }
                }
            }
            default -> {
                if (BetterChatMod.NO_THROW) {
                    BetterChatMod.LOGGER.trace("Fail to parse message content: {}", content);
                    return null;
                }
                throw new RuntimeException("Unsupported text type: " + content.getType().id());
            }
        }
        return null;
    }
}
