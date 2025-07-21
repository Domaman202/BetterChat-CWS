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
import java.util.function.Function;

public class ChatCategory {
    public String name;
    public String description;
    public String category;
    public String command;
    public String prefix;
    public boolean gl;
    public ArrayListDeque<Text> messages;

    public ChatCategory(String name, String description, String category, String command, String prefix, boolean gl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.command = command;
        this.prefix = prefix;
        this.gl = gl;
        this.messages = new ArrayListDeque<>(100);
    }

    public void onOpen() {
        var client = MinecraftClient.getInstance();
        ((IChatHud) client.inGameHud.getChatHud()).BetterChat$setMessages(this.messages);
        BetterChatMod.tryCommand(client, Objects.requireNonNullElseGet(this.command, () -> !this.gl || BetterChatMod.GLOBAL_CHAT ? "gc" : "lc"));
    }

    public String formatToSend(String message) {
        var msg = message;
        if (prefix != null)
            msg = prefix + message;
        if (category != null)
            msg = "[" + category + "] " + message;
        return msg;
    }

    public boolean tryAcceptSend(Text message) {
        if (this.category == null) {
            this.acceptSend(message);
            return true;
        } else {
            var prefix = "[" + this.category + "] ";
            var msg = checkAndReplaceSend(message, it -> it.startsWith(prefix), it -> it.substring(prefix.length()));
            if (msg != null) {
                this.acceptSend(msg);
                return true;
            }
        }
        return false;
    }

    public void acceptSend(Text message) {
        if (messages.size() == 100)
            this.messages.removeLast();
        this.messages.addFirst(message);
    }

    protected @Nullable Text checkAndReplaceSend(Text message, Function<String, Boolean> check, Function<String, String> replace) {
        var content = message.getContent();
        switch (content.getType().id()) {
            case "text" -> {
                var text = ((PlainTextContent) content).string();
                if (text != null && check.apply(text)) {
                    return Text.literal(replace.apply(text)).setStyle(message.getStyle());
                }
            }
            case "translatable" -> {
                var translatable =  (TranslatableTextContent) content;
                switch (translatable.getKey()) {
                    case "chat.type.text" -> {
                        var text = translatable.getArg(1).getString();
                        if (text != null && check.apply(text)) {
                            translatable.getArgs()[1] = Text.literal(replace.apply(text)).setStyle(((Text) translatable.getArgs()[1]).getStyle());
                            return Text.translatable("chat.type.text", translatable.getArgs());
                        }
                    }
                    default -> {
                        if (BetterChatMod.NO_THROW)
                            return null;
                        throw new RuntimeException("Unsupported text type: " + translatable.getKey());
                    }
                }
            }
            default -> {
                if (BetterChatMod.NO_THROW)
                    return null;
                throw new RuntimeException("Unsupported text type: " + content.getType().id());
            }
        }
        return null;
    }
}
