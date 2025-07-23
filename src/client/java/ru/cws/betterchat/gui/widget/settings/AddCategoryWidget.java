package ru.cws.betterchat.gui.widget.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.AbstractSettingsScreen;
import ru.cws.betterchat.screen.CategorySettingsScreen;

public class AddCategoryWidget extends ChatWidget {
    private final AbstractSettingsScreen screen;

    public AddCategoryWidget(AbstractSettingsScreen screen) {
        super(0, 0, 30, 20, Text.of("[+]"), Text.of("Добавить новую категорию"));
        this.screen = screen;
        this.flexRender.setBaseColor(0x60106010);
        this.flexRender.setHoverColor(0x6060FF60);
    }

    @Override
    public void onPress() {
        var category = new ChatCategory("Новая категория", "Описание новой категории", null, null, null, true, true);
        BetterChatMod.CATEGORIES.add(category);
        BetterChatMod.autosave();
        var tab = new CategorySettingsWidget(category, this.screen);
        for (CategorySettingsWidget other : this.screen.tabs)
            other.selected = false;
        tab.selected = true;
        this.screen.tabs.add(tab);
        tab.setX(this.screen.width / 2 - this.screen.tabsLength);
        tab.setY(this.height / 2 - 95);
        this.screen.addDrawableChild(tab);
        this.screen.tabsOffset += tab.getWidth() + 1;
        MinecraftClient.getInstance().setScreen(new CategorySettingsScreen(category, this.screen.tabs, this.screen.tabListPosition + 1));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return ChatWidget.DEFAULT_NARRATION_SUPPLIER.createNarrationMessage(super::getNarrationMessage);
    }
}
