package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class AutosaveSettingsWidget extends ChatWidget {
    public AutosaveSettingsWidget() {
        super(0, 0, 0, 20, null, Text.of("Переключение автосохранения конфигурации"));
        this.update();
    }

    @Override
    public void onPress() {
        BetterChatMod.AUTOSAVE = !BetterChatMod.AUTOSAVE;
        BetterChatMod.autosave();
        this.update();
    }

    protected void update() {
        if (BetterChatMod.AUTOSAVE) {
            this.setMessage(Text.of("Автосохранение (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        } else {
            this.setMessage(Text.of("Автосохранение (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        }
    }
}
