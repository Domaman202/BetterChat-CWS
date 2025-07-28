package ru.cws.betterchat.category;

import groovy.lang.Closure;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GroovyBindChatCategory extends ChatCategory {
    public Closure<Void> onOpen;
    public Closure<String> formatToSend;
    public Closure<Boolean> tryAccept;

    public GroovyBindChatCategory(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.onOpen.call();
    }

    @Override
    public String formatToSend(String message) {
        return this.formatToSend.call(message);
    }

    @Override
    public boolean tryAccept(Text message) {
        return this.tryAccept.call(ChatCategory.textToString(message), ChatCategory.textToFormattedeString(message), false);
    }

    @Override
    public void tryAcceptSelf(Text message) {
        this.tryAccept.call(ChatCategory.textToString(message), ChatCategory.textToFormattedeString(message), true);
    }

    @Override
    public void tryAcceptSelf(String message) {
        this.tryAccept.call(ChatCategory.createStringMessage("", MinecraftClient.getInstance().player.getGameProfile().getName(), message), message, true);
    }
}
