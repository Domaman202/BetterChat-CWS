package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.CategorySettingsScreen;

public class GlobalLocalSettingsWidget extends ChatWidget {
    private final CategorySettingsScreen screen;

    public GlobalLocalSettingsWidget(CategorySettingsScreen screen) {
        super(0, 0, 170, 20, getName(screen.category), Text.of("Возможность смены глобального / локального чата"));
        this.screen = screen;
        this.flexRender.setHoverColor(0x80808080);
    }

    @Override
    public void onPress() {
        this.screen.category.gl = !this.screen.category.gl;
        this.setMessage(getName(this.screen.category));
    }

    protected static Text getName(ChatCategory category) {
        return Text.of(category.gl ? "Global / Local" : "Global");
    }
}
