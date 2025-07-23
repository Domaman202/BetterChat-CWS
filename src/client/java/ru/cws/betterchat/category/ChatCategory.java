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
    public int cachedPrefixHash;
    public Pattern cachedPrefixPattern;
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
            if (this.showInCommon) {
                BetterChatMod.COMMON_CATEGORY.acceptFromOther(this, message);
            }
        }
    }

    public Pattern pattern() {
        if (this.pattern == null)
            return null;
        if (this.pattern.hashCode() != this.cachedHash)
            this.cachedPattern = Pattern.compile(this.pattern);
        return this.cachedPattern;
    }

    public void tryAcceptSelected(Text message) {
        var msg = checkReplaceAccepting(message, this.prefixPattern(), this.replacePattern ? "" : null);
        if (msg != null) {
            this.accept(msg);
            if (this.showInCommon) {
                BetterChatMod.COMMON_CATEGORY.acceptSelectedFromOther(this, message);
            }
        }
    }

    public Pattern prefixPattern() {
        if (this.prefix == null)
            return null;
        if (this.prefix.hashCode() != this.cachedPrefixHash)
            this.cachedPrefixPattern = Pattern.compile("^" + Pattern.quote(this.prefix));
        return this.cachedPrefixPattern;
    }

    public void accept(Text message) {
        if (messages.size() == 100)
            this.messages.removeLast();
        this.messages.addFirst(message);
    }

    public static @Nullable Text checkReplaceAccepting(Text message, @Nullable Pattern regex, @Nullable String replace) {
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
                var matcher = regex == null ? null : regex.matcher(text);
                if (regex == null || matcher.find()) {
                    return regex == null || replace == null ? message : Text.literal(matcher.replaceAll(replace)).setStyle(message.getStyle());
                }
            }
            case "translatable" -> {
                var translatable =  (TranslatableTextContent) content;
                switch (translatable.getKey()) {
                    case "chat.type.text" -> {
                        var text = translatable.getArg(1).getString();
                        var matcher = regex == null ? null : regex.matcher(text);
                        if (regex == null || matcher.find()) {
                            if (replace == null)
                                return message;
                            var args = translatable.getArgs();
                            var newArgs = (Object[]) Array.newInstance(args.getClass().componentType(), args.length);
                            System.arraycopy(args, 0, newArgs, 0, args.length);
                            return Text.literal("§r§7<§3§o" + ((Text) args[0]).getString() + "§r§7> §r§f" + (regex == null ? text : matcher.replaceAll(replace)));
                        }
                    }
                    case "command.unknown.command" -> {
                        var matcher = regex == null ? null : regex.matcher("Неизвестная команда");
                        if (regex == null || matcher.find()) {
                            return regex == null || replace == null ? message : Text.literal(matcher.replaceAll(replace)).setStyle(message.getStyle());
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
