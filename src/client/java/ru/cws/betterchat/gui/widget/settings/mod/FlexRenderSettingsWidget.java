package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class FlexRenderSettingsWidget extends ChatWidget {
    public FlexRenderSettingsWidget() {
        super(0, 0, 160, 20, null, Text.of("Переключить красивую отрисовку"));
        if (BetterChatMod.NO_FLEX) {
            this.setMessage(Text.of("FlexRender (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        } else {
            this.setMessage(Text.of("FlexRender (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        }
    }

    @Override
    public void onPress() {
        BetterChatMod.NO_FLEX = !BetterChatMod.NO_FLEX;
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
    }
}
