package ru.cws.betterchat.category;

import net.minecraft.text.Text;

public class AllChatCategory extends ChatCategory {
    public AllChatCategory() {
        super("Все", "Все чаты", null, null, null, true);
    }

    @Override
    public boolean tryAcceptSend(Text message) {
        try {
            if (super.tryAcceptSend(message)) {
                return true;
            }
        } catch (Exception ignored) {
        }
        this.acceptSend(message);
        return true;
    }
}
