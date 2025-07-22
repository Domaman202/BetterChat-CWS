package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class SettingsWidget extends ChatWidget {
    public SettingsWidget(int x, int y) {
        super(x, y, 20, 20, Text.of("[S]"), Text.of("Меню настроек"));
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x60FF4040);
    }

    @Override
    public void onPress() {
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
    }
}
