package ru.cws.betterchat.util;

import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.category.CommonChatCategory;

import java.util.List;

public record ConfigHelper(
        boolean all_chat_default,
        boolean no_throw,
        boolean no_flex,
        boolean autosave,
        List<CategoryHelper> categories
)  {
    public static ConfigHelper fromMod() {
        return new ConfigHelper(
                BetterChatMod.ALL_CHAT_DEFAULT,
                BetterChatMod.NO_THROW,
                BetterChatMod.NO_FLEX,
                BetterChatMod.AUTOSAVE,
                BetterChatMod.CATEGORIES.stream().map(CategoryHelper::fromCategory).toList()
        );
    }

    public void toMod() {
        BetterChatMod.ALL_CHAT_DEFAULT = this.all_chat_default;
        BetterChatMod.NO_THROW = this.no_throw;
        BetterChatMod.NO_FLEX = this.no_flex;
        BetterChatMod.AUTOSAVE = this.autosave;
        BetterChatMod.CATEGORIES.clear();
        this.categories.stream().map(CategoryHelper::toCategory).forEach(BetterChatMod.CATEGORIES::add);
    }

    public record CategoryHelper(
            String special,
            String name,
            String description,
            String command,
            String prefix,
            String pattern,
            boolean pattern_replace,
            boolean show_in_common
    ) {
        public static CategoryHelper fromCategory(ChatCategory category) {
            return new CategoryHelper(
                    category instanceof AllChatCategory? "all" : category instanceof CommonChatCategory ? "common" : null,
                    category.name,
                    category.description,
                    category.command,
                    category.prefix,
                    category.pattern,
                    category.replacePattern,
                    category.showInCommon
            );
        }

        public ChatCategory toCategory() {
            return switch (this.special) {
                case "all" -> new AllChatCategory();
                case "common" -> new CommonChatCategory();
                case null, default -> new ChatCategory(
                        this.name,
                        this.description,
                        this.command,
                        this.prefix,
                        this.pattern,
                        this.pattern_replace,
                        this.show_in_common
                );
            };
        }
    }
}
