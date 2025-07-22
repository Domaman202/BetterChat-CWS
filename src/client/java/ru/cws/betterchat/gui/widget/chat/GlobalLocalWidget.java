package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.CommonChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class GlobalLocalWidget extends ChatWidget {
    private final CommonChatCategory category;

    public GlobalLocalWidget(int x, int y) {
        super(x, y, 20, 20, null, Text.of("Перевод чата в локальный / глобальный режим"));
        this.category = (CommonChatCategory) BetterChatMod.CATEGORIES.stream().filter(it -> it instanceof CommonChatCategory).findFirst().orElseThrow();
        this.update();
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x60FF4040);
    }

    @Override
    public void onPress() {
        this.category.globalLocal = !this.category.globalLocal;
        this.update();
        BetterChatMod.tryCommand(this.category.globalLocal ? "gc" : "lc");
    }

    public void update() {
        this.setMessage(Text.of(this.category == BetterChatMod.SELECTED_CATEGORY ? this.category.globalLocal ? "[G]" : "[L]" : "X"));
    }
}
