package ru.cws.betterchat.util;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;

public class CategoryHelper {
    public static void tryAcceptToAll(Text message) {
        var selectedToNoDefault = false;
        for (ChatCategory category : BetterChatMod.CATEGORIES)
            if (category.tryAccept(message))
               selectedToNoDefault = true;
        if (!selectedToNoDefault)
            BetterChatMod.COMMON_CATEGORY.accept(message);
        BetterChatMod.SELECTED_CATEGORY.refreshMessages();
    }

    public static void tryAcceptSelfToAll(String player, String message) {
        var formatted = "<" + player + "> " + message;
        var literal = Text.literal(formatted);
        for (ChatCategory category : BetterChatMod.CATEGORIES)
                category.tryAcceptSelf(literal);
        BetterChatMod.SELECTED_CATEGORY.refreshMessages();
    }

    public static String formatToSend(String message) {
        return BetterChatMod.SELECTED_CATEGORY.formatToSend(message);
    }
}
