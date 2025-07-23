package ru.cws.betterchat.gui.widget.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.AbstractSettingsScreen;
import ru.cws.betterchat.screen.CategorySettingsScreen;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class CategorySettingsWidget extends ChatWidget {
    private AbstractSettingsScreen screen;
    public final ChatCategory category;
    public boolean selected;

    public CategorySettingsWidget(ChatCategory category, AbstractSettingsScreen screen) {
        super(0, 0, BetterChatMod.FIXED_TAB_SIZE ? BetterChatMod.SETTINGS_VIEW_TAB_SIZE : Math.min(80, category.name.length() * 10), 20, Text.of(category.name), Text.of(category.description));
        this.category = category;
        this.screen = screen;
        this.selected = false;
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x80808080);
    }

    @Override
    public void onPress() {
        if (this.selected) {
            this.selected = false;
            MinecraftClient.getInstance().setScreen(new ModSettingsScreen(this.screen.tabListPosition));
        } else {
            for (CategorySettingsWidget tab : this.screen.tabs)
                tab.selected = false;
            this.selected = true;
            var newScreen = new CategorySettingsScreen(this.category, this.screen.tabs, this.screen.tabListPosition);
            MinecraftClient.getInstance().setScreen(newScreen);
            this.screen = newScreen;
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Рендерим рамку
        this.flexRender.render(context, mouseX, mouseY);
        // Рендерим текст
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, ColorHelper.withAlpha(this.alpha, this.selected ? Colors.LIGHT_GRAY : Colors.WHITE));
    }
}