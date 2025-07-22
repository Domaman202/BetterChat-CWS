package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.CategorySettingsScreen;

public class ShowInCommonSettingsWidget extends ChatWidget {
    private final CategorySettingsScreen screen;

    public ShowInCommonSettingsWidget(CategorySettingsScreen screen) {
        super(0, 0, 180, 20, null, Text.of("Переключение дублирования сообщений в общем чате"));
        this.screen = screen;
        this.update();
    }

    @Override
    public void onPress() {
        this.screen.category.showInCommon = !this.screen.category.showInCommon;
        this.update();
    }

    protected void update() {
        if (this.screen.category.showInCommon) {
            this.setMessage(Text.of("Дубилование (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        } else {
            this.setMessage(Text.of("Дубилование (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        }
    }
}