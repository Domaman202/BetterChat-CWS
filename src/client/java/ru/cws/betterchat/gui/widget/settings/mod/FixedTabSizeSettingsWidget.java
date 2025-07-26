package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class FixedTabSizeSettingsWidget extends ChatWidget {
    public FixedTabSizeSettingsWidget() {
        super(0, 0, 0, 20, null, Text.of("Переключение фиксированного размера вкладок категорий"));
        if (BetterChatMod.FIXED_TAB_SIZE) {
            this.setMessage(Text.of("Фикс. размер (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        } else {
            this.setMessage(Text.of("Фикс. размер (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        }
    }

    @Override
    public void onPress() {
        BetterChatMod.FIXED_TAB_SIZE = !BetterChatMod.FIXED_TAB_SIZE;
        BetterChatMod.autosave();
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
    }
}
