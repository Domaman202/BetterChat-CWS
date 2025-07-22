package ru.cws.betterchat.category;

import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.Nullable;
import ru.cws.betterchat.BetterChatMod;

import java.util.Objects;

public class AllChatCategory extends ChatCategory {
    public AllChatCategory() {
        super("Все", "Все чаты", null, null, null, false, false);
    }

    @Override
    public void tryAccept(Text message) {
        if (BetterChatMod.ALL_CHAT_DEFAULT) {
            this.accept(message);
            return;
        }

        var content = getAcceptingContent(message);
        if (content == null) {
            this.accept(message);
            return;
        }

        var nonFounded = true;
        for (var category : BetterChatMod.CATEGORIES) {
            if (category instanceof AllChatCategory)
                continue;
            var matcher = category.pattern().matcher(content.content);
            if (matcher.find()) {
                nonFounded = false;
                var text = new StringBuilder();
                text.append("§6~ Новое сообщение\n");
                text.append("§3- Отправитель: §a").append(Objects.requireNonNullElse(content.sender, "Система")).append("\n");
                text.append("§3- Категория:   §b ").append(category.name).append("\n");
                text.append("§3- Содержание:  §c").append(category.replacePattern ? matcher.replaceAll("") : content.content);
                this.accept(Text.of(text.toString()));
            }
        }
        if (nonFounded && !content.content.isEmpty()) {
            var text = new StringBuilder();
            text.append("§6~ Новое сообщение\n");
            text.append("§3- Отправитель: §a").append(Objects.requireNonNullElse(content.sender, "Система")).append("\n");
            text.append("§3- Категория:   §b§o Отсутсвует§r\n");
            text.append("§3- Содержание:  §c").append(content.content);
            this.accept(Text.of(text.toString()));
        }
    }

    private static @Nullable AcceptingContent getAcceptingContent(Text message) {
        var content = message.getContent();
        switch (content.getType().id()) {
            case "text" -> {
                var text = ((PlainTextContent) content).string();
                if (text.isEmpty()) {
                    var sibling = message.getSiblings().getFirst();
                    if (sibling == null)
                        return null;
                    return getAcceptingContent(sibling);
                }
                return new AcceptingContent(text, null);
            }
            case "translatable" -> {
                var translatable =  (TranslatableTextContent) content;
                switch (translatable.getKey()) {
                    case "chat.type.text" -> {
                        return new AcceptingContent(translatable.getArg(1).getString(), translatable.getArg(0).getString());
                    }
                    case "command.unknown.command" -> {
                        return new AcceptingContent("Неизвестная команда", null);
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
    }

    private record AcceptingContent(String content, String sender) {
    }
}
