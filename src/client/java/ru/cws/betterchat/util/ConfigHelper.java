package ru.cws.betterchat.util;

import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.category.CommonChatCategory;

import java.util.List;

public record ConfigHelper(
        boolean all_chat_vanilla,
        boolean global_local,
        boolean fixed_tab_size,
        boolean no_flex,
        boolean no_heave_textures,
        boolean no_throw,
        boolean autosave,
        List<CategoryHelper> categories
)  {
    public static ConfigHelper fromMod() {
        return new ConfigHelper(
                BetterChatMod.ALL_CHAT_VANILLA,
                BetterChatMod.GLOBAL_LOCAL,
                BetterChatMod.FIXED_TAB_SIZE,
                BetterChatMod.NO_FLEX,
                BetterChatMod.NO_HEAVY_TEXTURES,
                BetterChatMod.NO_THROW,
                BetterChatMod.AUTOSAVE,
                BetterChatMod.CATEGORIES.stream().map(CategoryHelper::fromCategory).toList()
        );
    }

    public void toMod() {
        BetterChatMod.ALL_CHAT_VANILLA = this.all_chat_vanilla;
        BetterChatMod.GLOBAL_LOCAL = this.global_local;
        BetterChatMod.FIXED_TAB_SIZE = this.fixed_tab_size;
        BetterChatMod.NO_FLEX = this.no_flex;
        BetterChatMod.NO_HEAVY_TEXTURES = this.no_heave_textures;
        BetterChatMod.NO_THROW = this.no_throw;
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
