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

    public boolean tryAccept(Text message) {
        var msg = checkReplaceAccepting(message, this.pattern(), this.replacePattern ? "" : null);
        if (msg == null)
            return false;
        this.accept(msg);
        if (this.showInCommon)
            BetterChatMod.COMMON_CATEGORY.acceptFromOther(this, message);
        return true;
    }

    public Pattern pattern() {
        if (this.pattern == null)
            return null;
        if (this.pattern.hashCode() != this.cachedHash)
            this.cachedPattern = Pattern.compile(this.pattern);
        return this.cachedPattern;
    }

    public void tryAcceptSelected(String message) {
        var content = getAcceptingContent(message);
        var msg = literal(content);
        this.accept(msg);
        if (this.showInCommon) {
            BetterChatMod.COMMON_CATEGORY.acceptSelectedFromOther(this, msg);
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
        var string = getAcceptingString(message);

        if (regex == null) {
            return literal(getAcceptingContent(string));
        }

        var matcher = regex.matcher(string);
        if (matcher.find()) {
            return literal(getAcceptingContent(replace == null ? string : matcher.replaceAll(replace)));
        }

        var content = getAcceptingContent(string);
        var matcherC = regex.matcher(content.content());
        if (matcherC.find()) {
            return literal(replace == null ? content : new AcceptingContent(matcherC.replaceAll(replace), content.sender()));
        }

        return null;
    }

    public static Text literal(AcceptingContent content) {
        return Text.literal("§r§7<" + (content.sender() == null ? "§4§oSystem" : ("§3§o" + content.sender())) + "§r§7> §r§f" + content.content());
    }

    public static AcceptingContent getAcceptingContent(String message) {
        var vanillaSenderMatcher = BetterChatMod.VANILLA_SENDER_PATTERN.matcher(message);
        if (vanillaSenderMatcher.find()) {
            var sender = vanillaSenderMatcher.group(0);
            return new AcceptingContent(vanillaSenderMatcher.replaceAll(""), sender.substring(1, sender.length() - 2));
        }

        return new AcceptingContent(message, null);
    }

    public static String getAcceptingString(Text message) {
        var ordered = message.asOrderedText();
        var builder = new StringBuilder();
        ordered.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }

    public record AcceptingContent(String content, String sender) {
    }
}
