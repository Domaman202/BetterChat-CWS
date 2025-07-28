package ru.cws.betterchat.util;

import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;

import java.util.List;

public record ConfigHelper(
        boolean all_chat_vanilla,
        boolean category_formatting,
        boolean global_local,
        String local_chat_prefix,
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
                BetterChatMod.CATEGORY_FORMATTING,
                BetterChatMod.GLOBAL_LOCAL,
                BetterChatMod.LOCAL_CHAT_PREFIX,
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
        BetterChatMod.CATEGORY_FORMATTING = this.category_formatting;
        BetterChatMod.GLOBAL_LOCAL = this.global_local;
        BetterChatMod.LOCAL_CHAT_PREFIX = this.local_chat_prefix;
        BetterChatMod.FIXED_TAB_SIZE = this.fixed_tab_size;
        BetterChatMod.NO_FLEX = this.no_flex;
        BetterChatMod.NO_HEAVY_TEXTURES = this.no_heave_textures;
        BetterChatMod.NO_THROW = this.no_throw;
        BetterChatMod.AUTOSAVE = this.autosave;
        this.categories.forEach(CategoryHelper::toCategory);
    }

    public record CategoryHelper(
            String id,
            String name,
            String description
    ) {
        public static CategoryHelper fromCategory(ChatCategory category) {
            return new CategoryHelper(
                    category.id,
                    category.name,
                    category.description
            );
        }

        public void toCategory() {
            BetterChatMod.CATEGORIES.stream().filter(it -> it.id.equals(this.id)).findFirst().ifPresent(it -> {
                it.name = this.name;
                it.description = this.description;
            });
        }
    }
}
