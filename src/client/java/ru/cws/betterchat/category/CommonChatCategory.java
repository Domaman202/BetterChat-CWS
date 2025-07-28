package ru.cws.betterchat.category;

import net.minecraft.text.Text;

public class CommonChatCategory extends GroovyBindChatCategory {
    public CommonChatCategory() {
        super("common", "Общий", "Общий чат");
    }

    public void acceptFromOther(ChatCategory other, String prefix, String sender, String content) {
        this.accept(Text.literal("§r§7§l[§r§6" + other.name + "§r§7§l] §r§f§o" + ChatCategory.createStringMessage(prefix, sender, content)));
    }

    public void acceptFromOther(String message) {
        this.accept(Text.literal(message));
    }
}
