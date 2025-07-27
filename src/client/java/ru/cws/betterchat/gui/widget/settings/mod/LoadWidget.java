package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class LoadWidget extends ChatWidget {
    public LoadWidget(int x, int y, int w) {
        super(x, y, w, 20, Text.of("Загрузить"), Text.of("Загрузить конфигурацию"));
        this.flexRender.setBaseColor(0x60606010);
        this.flexRender.setHoverColor(0x60FFFF10);
    }

    @Override
    public void onPress() {
        BetterChatMod.load();
    }
}
