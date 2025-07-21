package ru.cws.betterchat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.util.IChatScreen;

import java.util.ArrayList;
import java.util.List;

public class BetterChatMod implements ClientModInitializer {
    public static final List<ChatCategory> CATEGORIES = new ArrayList<>();
    public static ChatCategory SELECTED_CATEGORY;
    public static IChatScreen CHAT_SCREEN = null;
    public static boolean GLOBAL_CHAT = true;
    public static boolean NO_THROW = true;

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            GLOBAL_CHAT = true;
            tryCommand(client, "gc");
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            for (ChatCategory category : CATEGORIES) {
                category.messages.clear();
            }
        });

        CATEGORIES.add(new AllChatCategory());
        CATEGORIES.add(new ChatCategory("Общий", "Общий игровой чат", "common", null, null, true));
        CATEGORIES.add(new ChatCategory("Рынок", "Торговый игровой чат", "trade", null, null, true));
        CATEGORIES.add(new ChatCategory("Поддержка", "Чат технической поддержки", "support", null, null, false));
    }

    public static void tryCommand(MinecraftClient client, String command) {
        try {
            client.player.networkHandler.sendChatCommand(command);
        } catch (Exception ignored) {
        }
    }
}
