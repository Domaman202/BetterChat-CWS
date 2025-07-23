package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class AllChatVanillaSettingsWidget extends ChatWidget {
    public AllChatVanillaSettingsWidget() {
        super(0, 0, 200, 20, null, Text.of("Переключение режима ванильного чата"));
        this.update();
    }

    @Override
    public void onPress() {
        BetterChatMod.ALL_CHAT_VANILLA = !BetterChatMod.ALL_CHAT_VANILLA;
        BetterChatMod.autosave();
        this.update();
    }

    protected void update() {
        if (BetterChatMod.ALL_CHAT_VANILLA) {
            this.setMessage(Text.of("Ванильный чат (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        } else {
            this.setMessage(Text.of("Ванильный чат (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        }
    }
}
