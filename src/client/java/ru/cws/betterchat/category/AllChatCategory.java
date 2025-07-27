package ru.cws.betterchat.category;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import ru.cws.betterchat.util.CategoryHelper;

public class AllChatCategory extends ChatCategory {
    public AllChatCategory() {
        super("Все", "Все чаты");
    }

    @Override
    public String formatToSend(String message) {
        return message;
    }

    @Override
    public boolean tryAccept(Text message) {
        this.accept(message);
        return true;
    }

    @Override
    public void tryAcceptSelf(Text message) {
        this.accept(Text.literal(CategoryHelper.formatToSend(ChatCategory.textToString(message))));
    }

    @Override
    public void tryAcceptSelf(String message) {
        this.accept(Text.literal(CategoryHelper.formatToSend(ChatCategory.createStringMessage("", MinecraftClient.getInstance().player.getGameProfile().getName(), message))));
    }
}
