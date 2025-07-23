package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class GlobalLocalWidget extends ChatWidget {
    public GlobalLocalWidget(int x, int y) {
        super(x, y, 20, 20, null, Text.of("Перевод чата в локальный / глобальный режим"));
        this.update();
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x60FF4040);
    }

    @Override
    public void onPress() {
        if (BetterChatMod.SELECTED_CATEGORY == BetterChatMod.COMMON_CATEGORY) {
            BetterChatMod.GLOBAL_LOCAL = !BetterChatMod.GLOBAL_LOCAL;
            this.update();
            BetterChatMod.tryCommand(BetterChatMod.GLOBAL_LOCAL ? "gc" : "lc");
        }
    }

    public void update() {
        this.setMessage(Text.of(BetterChatMod.SELECTED_CATEGORY == BetterChatMod.COMMON_CATEGORY ? BetterChatMod.GLOBAL_LOCAL ? "[G]" : "[L]" : "X"));
    }
}
