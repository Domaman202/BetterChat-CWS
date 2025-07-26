package ru.cws.betterchat.gui.widget.settings;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.AbstractSettingsScreen;

public class AddCategoryWidget extends ChatWidget {
    private final AbstractSettingsScreen screen;

    public AddCategoryWidget(int x, int y, AbstractSettingsScreen screen) {
        super(x, y, 30, 20, Text.of("[+]"), Text.of("Добавить новую категорию"));
        this.screen = screen;
        this.flexRender.setBaseColor(0x60106010);
        this.flexRender.setHoverColor(0x6060FF60);
    }

    @Override
    public void onPress() {
        var category = new ChatCategory("Новая категория", "Описание новой категории", null, null, null, true, true);
        BetterChatMod.CATEGORIES.add(category);
        BetterChatMod.autosave();
        this.screen.BetterCombat$recalcTabsList();
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
