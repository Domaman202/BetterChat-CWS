package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class CategoryFormattingSettingsWidget extends ChatWidget {
    public CategoryFormattingSettingsWidget() {
        super(0, 0, 200, 20, null, Text.of("Переключение режима форматирования категорий"));
        this.update();
    }

    @Override
    public void onPress() {
        BetterChatMod.CATEGORY_FORMATTING = !BetterChatMod.CATEGORY_FORMATTING;
        BetterChatMod.autosave();
        this.update();
    }

    protected void update() {
        if (BetterChatMod.CATEGORY_FORMATTING) {
            this.setMessage(Text.of("Подпись категорий (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        } else {
            this.setMessage(Text.of("Подпись категорий (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        }
    }
}
