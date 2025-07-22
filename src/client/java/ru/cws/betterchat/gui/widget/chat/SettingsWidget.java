package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class SettingsWidget extends ChatWidget {
    public SettingsWidget(int x, int y) {
        super(x, y, 20, 20, Text.of("[S]"), Text.of("Меню настроек"));
    }

    @Override
    public void onPress() {
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
    }
}
