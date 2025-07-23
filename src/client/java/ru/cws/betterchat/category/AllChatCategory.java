package ru.cws.betterchat.category;

import net.minecraft.text.*;
import ru.cws.betterchat.BetterChatMod;

import java.util.Objects;

public class AllChatCategory extends ChatCategory {
    public AllChatCategory() {
        super("Все", "Все чаты", null, null, null, false, false);
    }

    @Override
    public void tryAccept(Text message) {
        this.tryAcceptMaybeSelected(message, false);
    }

    @Override
    public void tryAcceptSelected(String message) {
        this.tryAcceptMaybeSelected(literal(getAcceptingContent(message)), true);
    }

    private void tryAcceptMaybeSelected(Text message, boolean selected) {
        if (BetterChatMod.ALL_CHAT_VANILLA) {
            this.accept(message);
            return;
        }

        var content = ChatCategory.getAcceptingContent(ChatCategory.getAcceptingString(message));

        var nonFounded = true;
        for (var category : BetterChatMod.CATEGORIES) {
            if (category == BetterChatMod.ALL_CATEGORY)
                continue;
            var pattern = selected ? category.prefixPattern() : category.pattern();
            if (pattern == null)
                continue;
            var matcher = pattern.matcher(content.content());
            if (matcher.find()) {
                nonFounded = false;
                var text = new StringBuilder();
                text.append("§6~ Новое сообщение\n");
                text.append("§3- Отправитель: §a").append(Objects.requireNonNullElse(content.sender(), "Система")).append("\n");
                text.append("§3- Категория:   §b ").append(category.name).append("\n");
                text.append("§3- Содержание:  §c").append(category.replacePattern ? matcher.replaceAll("") : content.content());
                this.accept(Text.of(text.toString()));
            }
        }

        if (nonFounded && !content.content().isEmpty()) {
            var text = new StringBuilder();
            text.append("§6~ Новое сообщение\n");
            text.append("§3- Отправитель: §a").append(Objects.requireNonNullElse(content.sender(), "Система")).append("\n");
            text.append("§3- Категория:   §b§o Отсутсвует§r\n");
            text.append("§3- Содержание:  §c").append(content.content());
            this.accept(Text.of(text.toString()));
        }
    }
}
