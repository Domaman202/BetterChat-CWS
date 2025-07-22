package ru.cws.betterchat.util;

import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;

import java.util.List;

public record ConfigHelper(
        boolean no_throw,
        boolean no_flex,
        boolean autosave,
        List<CategoryHelper> categories
)  {
    public static ConfigHelper fromMod() {
        return new ConfigHelper(
                BetterChatMod.NO_THROW,
                BetterChatMod.NO_FLEX,
                BetterChatMod.AUTOSAVE,
                BetterChatMod.CATEGORIES.stream().map(CategoryHelper::fromCategory).toList()
        );
    }

    public void toMod() {
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
            String category,
            String command,
            String prefix,
            boolean global_local
    ) {
        public static CategoryHelper fromCategory(ChatCategory category) {
            return new CategoryHelper(
                    category instanceof AllChatCategory ? "all" : null,
                    category.name,
                    category.description,
                    category.category,
                    category.command,
                    category.prefix,
                    category.gl
            );
        }

        public ChatCategory toCategory() {
            if (this.special != null && this.special.equals("all"))
                return new AllChatCategory();
            return new ChatCategory(
                    this.name,
                    this.description,
                    this.category,
                    this.command,
                    this.prefix,
                    this.global_local
            );
        }
    }
}
