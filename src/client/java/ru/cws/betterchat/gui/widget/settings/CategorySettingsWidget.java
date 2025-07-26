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

    public CategorySettingsWidget(int x, int y, ChatCategory category, AbstractSettingsScreen screen) {
        super(x, y, getWidth(category), 20, Text.of(category.name), Text.of(category.description));
        this.category = category;
        this.screen = screen;
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x80808080);
    }

    public static int getWidth(ChatCategory category) {
        return BetterChatMod.FIXED_TAB_SIZE ? BetterChatMod.SETTINGS_VIEW_TAB_SIZE : (category.name.length() * 10);
    }

    @Override
    public void onPress() {
        if (this.category == this.screen.selected) {
            MinecraftClient.getInstance().setScreen(new ModSettingsScreen(this.screen.tabListPosition));
        } else {
            this.screen = new CategorySettingsScreen(this.category, this.category, this.screen.tabListPosition);
            MinecraftClient.getInstance().setScreen(this.screen);
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Рендерим рамку
        this.flexRender.render(context, mouseX, mouseY);
        // Рендерим текст
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, ColorHelper.withAlpha(this.alpha, this.category == this.screen.selected ? Colors.LIGHT_GRAY : Colors.WHITE));
    }
}