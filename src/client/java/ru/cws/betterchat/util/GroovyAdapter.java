package ru.cws.betterchat.util;

import groovy.lang.Closure;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.category.GroovyBindChatCategory;

import java.util.List;

public class GroovyAdapter {
    public static final GroovyAdapter INSTANCE = new GroovyAdapter();

    public void commonCategoryAccept(ChatCategory other, String prefix, String sender, String content) {
        BetterChatMod.COMMON_CATEGORY.acceptFromOther(other, prefix, sender, content);
    }

    public String getCategoryName(ChatCategory category) {
        return category.name;
    }

    public String getCategoryDescription(ChatCategory category) {
        return category.description;
    }

    public void setCategoryOnOpen(ChatCategory category, Closure<Void> closure) {
        if (category instanceof GroovyBindChatCategory) {
            ((GroovyBindChatCategory) category).onOpen = closure;
        } else {
            throw new RuntimeException("Unsupported category type: " + category.getClass());
        }
    }

    public void setCategoryFormatToSend(ChatCategory category, Closure<String> closure) {
        if (category instanceof GroovyBindChatCategory) {
            ((GroovyBindChatCategory) category).formatToSend = closure;
        } else {
            throw new RuntimeException("Unsupported category type: " + category.getClass());
        }
    }

    public void setCategoryTryAccept(ChatCategory category, Closure<Boolean> closure) {
        if (category instanceof GroovyBindChatCategory) {
            ((GroovyBindChatCategory) category).tryAccept = closure;
        } else {
            throw new RuntimeException("Unsupported category type: " + category.getClass());
        }
    }

    public void categoryAccept(ChatCategory category, Text message) {
        category.accept(message);
    }

    public List<ChatCategory> getAllCategories() {
        return BetterChatMod.CATEGORIES;
    }

    public ChatCategory getSelectedCategory() {
        return BetterChatMod.SELECTED_CATEGORY;
    }

    public void setSelectedCategory(ChatCategory category) {
        BetterChatMod.SELECTED_CATEGORY = category;
    }

    public ChatCategory getCommonCategory() {
        return BetterChatMod.COMMON_CATEGORY;
    }

    public ChatCategory getAllCategory() {
        return BetterChatMod.ALL_CATEGORY;
    }

    public ChatCategory getOrCreateCategory(String name, String description) {
        return BetterChatMod.CATEGORIES.stream().filter(it -> it.name.equals(name)).findFirst().orElseGet(() -> {
            var category = new GroovyBindChatCategory(name, description);
            BetterChatMod.CATEGORIES.add(category);
            BetterChatMod.autosave();
            return category;
        });
    }

    public ChatCategory getCategory(String name) {
        return BetterChatMod.CATEGORIES.stream().filter(it -> it.name.equals(name)).findFirst().orElseThrow(() -> new RuntimeException("No such category: " + name));
    }

    public String getPlayerName() {
        return MinecraftClient.getInstance().player.getGameProfile().getName();
    }

    public Text createMessage(String prefix, String sender, String content) {
        return ChatCategory.createMessage(prefix, sender, content);
    }

    public void executeCommand(String command) {
        BetterChatMod.tryCommand(command);
    }
}
