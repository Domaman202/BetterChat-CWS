package ru.cws.betterchat.category;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.collection.ArrayListDeque;
import org.jetbrains.annotations.Nullable;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.util.IChatHud;

import java.lang.reflect.Array;
import java.util.regex.Pattern;

public class ChatCategory {
    public String name;
    public String description;
    //
    public String command;
    public String prefix;
    public String pattern;
    public boolean replacePattern;
    public boolean showInCommon;
    //
    public int cachedHash;
    public Pattern cachedPattern;
    public ArrayListDeque<Text> messages;

    public ChatCategory(String name, String description, String command, String prefix, String pattern, boolean replacePattern, boolean showInCommon) {
        this.name = name;
        this.description = description;
        this.command = command;
        this.prefix = prefix;
        this.pattern = pattern;
        this.replacePattern = replacePattern;
        this.showInCommon = showInCommon;
        this.cachedHash = 0;
        this.cachedPattern = null;
        this.messages = new ArrayListDeque<>(100);
    }

    public void onOpen() {
        this.refreshMessages();
        if (this.command != null) {
            BetterChatMod.tryCommand(command);
        }
    }

    public void refreshMessages() {
        ((IChatHud) MinecraftClient.getInstance().inGameHud.getChatHud()).BetterChat$setMessages(this.messages);
    }

    public String formatToSend(String message) {
        return this.prefix == null ? message : this.prefix + message;
    }

    public void tryAccept(Text message) {
        var msg = checkReplaceAccepting(message, this.pattern(), this.replacePattern ? "" : null);
        if (msg != null) {
            this.accept(msg);
        }
    }

    public Pattern pattern() {
        if (this.pattern.hashCode() != this.cachedHash)
            this.cachedPattern = Pattern.compile(this.pattern);
        return this.cachedPattern;
    }

    public void accept(Text message) {
        if (messages.size() == 100)
            this.messages.removeLast();
        this.messages.addFirst(message);
    }

    protected static @Nullable Text checkReplaceAccepting(Text message, Pattern regex, @Nullable String replace) {
        var content = message.getContent();
        switch (content.getType().id()) {
            case "text" -> {
                var text = ((PlainTextContent) content).string();
                if (text.isEmpty()) {
                    var sibling = message.getSiblings().getFirst();
                    if (sibling == null)
                        return null;
                    return checkReplaceAccepting(sibling, regex, replace);
                }
                var matcher = regex.matcher(text);
                if (matcher.find()) {
                    return replace == null ? message : Text.literal(matcher.replaceAll(replace)).setStyle(message.getStyle());
                }
            }
            case "translatable" -> {
                var translatable =  (TranslatableTextContent) content;
                switch (translatable.getKey()) {
                    case "chat.type.text" -> {
                        var text = translatable.getArg(1).getString();
                        var matcher = regex.matcher(text);
                        if (matcher.find()) {
                            if (replace == null)
                                return message;
                            var args = translatable.getArgs();
                            var newArgs = (Object[]) Array.newInstance(args.getClass().componentType(), args.length);
                            System.arraycopy(args, 0, newArgs, 0, args.length);
                            newArgs[1] = Text.literal(matcher.replaceAll(replace)).setStyle(((Text) args[1]).getStyle());
                            return Text.translatable("chat.type.text", newArgs);
                        }
                    }
                    case "command.unknown.command" -> {
                        var matcher = regex.matcher("Неизвестная команда");
                        if (matcher.find()) {
                            return replace == null ? message : Text.literal(matcher.replaceAll(replace)).setStyle(message.getStyle());
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
