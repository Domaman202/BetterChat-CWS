package ru.cws.betterchat.gui.widget;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;

public class GlobalLocalWidget extends ChatWidget {
    public GlobalLocalWidget(int x, int y) {
        super(x, y, 20, 20, Text.of(BetterChatMod.GLOBAL_CHAT ? "[G]" : "[L]"), Text.of("Перевод чата в локальный / глобальный режим"));
    }

    @Override
    public void onPress() {
        BetterChatMod.GLOBAL_CHAT = !BetterChatMod.GLOBAL_CHAT;
        this.setMessage(Text.of(BetterChatMod.GLOBAL_CHAT ? "[G]" : "[L]"));
        BetterChatMod.SELECTED_CATEGORY.onOpen(); // reopen for reset global / local state
    }
}
