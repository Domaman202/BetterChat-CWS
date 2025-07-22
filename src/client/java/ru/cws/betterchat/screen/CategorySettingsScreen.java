package ru.cws.betterchat.screen;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.*;
import ru.cws.betterchat.gui.widget.settings.CategorySettingsWidget;
import ru.cws.betterchat.gui.widget.settings.category.DeleteCategoryWidget;
import ru.cws.betterchat.gui.widget.settings.category.GlobalLocalSettingsWidget;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CategorySettingsScreen extends AbstractSettingsScreen {
    public final ChatCategory category;

    public CategorySettingsScreen(ChatCategory category, List<CategorySettingsWidget> tabs) {
        super(Text.of("Настройки категории"));
        this.category = category;
        this.tabs = tabs;
    }

    @Override
    protected void init() {
        super.init();
        //
        var active = !(this.category instanceof AllChatCategory);
        //
        addTextField(
                true,
                this.category.name,
                "Название категории",
                this::emptyToText,
                it -> {
                    this.category.name = it;
                    BetterChatMod.autosave();
                    this.recalcTabs();
                }
        );
        addTextField(
                true,
                this.category.description,
                "Описание категории",
                this::emptyToText,
                it -> {
                    this.category.description = it;
                    BetterChatMod.autosave();
                }
        );
        addTextField(
                active,
                this.category.category,
                "Категория (Добавляется в [] в начале сообщения при его отправке)",
                this::emptyToNull,
                it -> {
                    this.category.category = it;
                    BetterChatMod.autosave();
                }
        );
        addTextField(active,
                this.category.command,
                "Команда (Выполняется при переключении на категорию)",
                this::emptyToNull,
                it -> {
                    this.category.command = it;
                    BetterChatMod.autosave();
                }
        );
        addTextField(
                active,
                this.category.prefix,
                "Префикс (Добавляется в начале сообщения при его отправке",
                this::emptyToNull,
                it -> {
                    this.category.prefix = it;
                    BetterChatMod.autosave();
                }
        );
        //
        this.addSettingsWidget(new GlobalLocalSettingsWidget(this).setActive(active));
        this.addSettingsWidget(new DeleteCategoryWidget(this).setActive(active));
    }

    protected void addTextField(boolean active, String initial, String description, Supplier<String> ifEmpty, Consumer<String> changedListener) {
        var field = new CenteredTextFieldWidget(this.textRenderer, this.tabsLength, 20, Text.of("Название"));
        field.active = active;
        field.setFocusUnlocked(true);
        field.setEditableColor(active ? Colors.YELLOW : Colors.LIGHT_GRAY);
        field.setUneditableColor(Colors.WHITE);
        field.setDrawsBackground(false);
        field.setTooltip(Tooltip.of(Text.of(description)));
        field.setMaxLength(32);
        field.setText(initial == null || initial.isEmpty() ? "[Пусто]" : initial);
        field.setEditable(true);
        field.setChangedListener(it -> changedListener.accept(it.isEmpty() ? ifEmpty.get() : it));
        this.addSettingsWidget(field);
    }

    protected String emptyToNull() {
        return null;
    }

    protected String emptyToText() {
        return "[Пусто]";
    }
}
