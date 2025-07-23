package ru.cws.betterchat.util;

import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.category.CommonChatCategory;

import java.util.List;

public record ConfigHelper(
        boolean all_chat_default,
        boolean global_local,
        boolean no_throw,
        boolean no_flex,
        boolean autosave,
        List<CategoryHelper> categories
)  {
    public static ConfigHelper fromMod() {
        return new ConfigHelper(
                BetterChatMod.ALL_CHAT_DEFAULT,
                BetterChatMod.GLOBAL_LOCAL,
                BetterChatMod.NO_THROW,
                BetterChatMod.NO_FLEX,
                BetterChatMod.AUTOSAVE,
                BetterChatMod.CATEGORIES.stream().map(CategoryHelper::fromCategory).toList()
        );
    }

    public void toMod() {
        BetterChatMod.ALL_CHAT_DEFAULT = this.all_chat_default;
        BetterChatMod.GLOBAL_LOCAL = this.global_local;
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
                    category == BetterChatMod.ALL_CATEGORY ? "all" : category == BetterChatMod.COMMON_CATEGORY ? "common" : null,
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
                case "all" -> {
                    var category = new AllChatCategory();
                    BetterChatMod.ALL_CATEGORY = category;
                    yield category;
                }
                case "common" -> {
                    var category = new CommonChatCategory();
                    BetterChatMod.COMMON_CATEGORY = category;
                    yield category;
                }
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
