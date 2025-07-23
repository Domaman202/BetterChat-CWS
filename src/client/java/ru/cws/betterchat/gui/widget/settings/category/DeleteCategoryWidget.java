package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.CategorySettingsScreen;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class DeleteCategoryWidget extends ChatWidget {
    private final CategorySettingsScreen screen;

    public DeleteCategoryWidget(CategorySettingsScreen screen) {
        super(0, 0, 170, 20, Text.of("Удалить"), Text.of("Удалить текущую категорию"));
        this.screen = screen;
        this.flexRender.setBaseColor(0x60601010);
        this.flexRender.setHoverColor(0x60FF1010);
    }

    @Override
    public void onPress() {
        BetterChatMod.CATEGORIES.remove(this.screen.category);
        BetterChatMod.autosave();
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen(this.screen.tabListPosition > 0 ? (this.screen.tabListPosition - 1) : 0));
    }
}
