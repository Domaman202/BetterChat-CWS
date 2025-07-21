package ru.cws.betterchat.gui.widget;

import net.minecraft.text.Text;

public class SettingsWidget extends ChatWidget {
    public SettingsWidget(int x, int y) {
        super(x, y, 20, 20, Text.of("[S]"), Text.of("Меню настроек"));
    }

    @Override
    public void onPress() {
        // todo: open settings screen
    }
}
