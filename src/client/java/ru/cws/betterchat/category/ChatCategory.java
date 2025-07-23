package ru.cws.betterchat.category;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.collection.ArrayListDeque;
import org.jetbrains.annotations.Nullable;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.util.IChatHud;

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
        var content = getAcceptingContent(message);

        if (regex == null) {
            return literal(content.sender(), content.content());
        }

        var matcher = regex.matcher(content.content());
        if (matcher.find()) {
            return literal(content.sender(), replace == null ? content.content() : matcher.replaceAll(replace));
        }

        return null;
    }

    public static Text literal(String sender, String replaced) {
        return Text.literal("§r§7<" + (sender == null ? "§4§oSystem" : ("§3§o" + sender)) + "§r§7> §r§f" + replaced);

    }

    public static AcceptingContent getAcceptingContent(Text message) {
        var ordered = message.asOrderedText();
        var builder = new StringBuilder();
        ordered.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        var text = builder.toString();
        var matcher = BetterChatMod.USER_SENDER_PATTERN.matcher(text);
        if (matcher.find()) {
            var sender = matcher.group(0);
            return new AcceptingContent(matcher.replaceAll("").trim(), sender.substring(1, sender.length() - 1));
        }
        return new AcceptingContent(text, null);
    }

    public record AcceptingContent(String content, String sender) {
    }
}
