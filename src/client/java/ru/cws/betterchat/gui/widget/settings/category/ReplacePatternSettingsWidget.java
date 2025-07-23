package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.CategorySettingsScreen;

public class ReplacePatternSettingsWidget extends ChatWidget {
    private final CategorySettingsScreen screen;

    public ReplacePatternSettingsWidget(CategorySettingsScreen screen) {
        super(0, 0, 240, 20, null, Text.of("Переключение затирания шаблона при получении сообщения"));
        this.screen = screen;
        this.update();
    }

    @Override
    public void onPress() {
        this.screen.category.replacePattern = !this.screen.category.replacePattern;
        BetterChatMod.autosave();
        this.update();
    }

    protected void update() {
        if (this.screen.category.replacePattern) {
            this.setMessage(Text.of("Затирание шаблона (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        } else {
            this.setMessage(Text.of("Затирание шаблона (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        }
    }
}
