package ru.cws.betterchat.screen;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.*;
import ru.cws.betterchat.gui.widget.settings.CategorySettingsWidget;
import ru.cws.betterchat.gui.widget.settings.category.AllChatVanillaSettingsWidget;
import ru.cws.betterchat.gui.widget.settings.category.DeleteCategoryWidget;
import ru.cws.betterchat.gui.widget.settings.category.ReplacePatternSettingsWidget;
import ru.cws.betterchat.gui.widget.settings.category.ShowInCommonSettingsWidget;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CategorySettingsScreen extends AbstractSettingsScreen {
    public final ChatCategory category;

    public CategorySettingsScreen(ChatCategory category, List<CategorySettingsWidget> tabs, int tabListPosition) {
        super(Text.of("Настройки категории"));
        this.category = category;
        this.tabs = tabs;
        this.tabListPosition = tabListPosition;
    }

    @Override
    protected void init() {
        super.init();
        //
        addTextField(
                this.category.name,
                "Название категории",
                this::emptyToText,
                it -> {
                    this.category.name = it;
                    BetterChatMod.autosave();
                    this.BetterCombat$recalcTabsList();
                }
        );
        addTextField(
                this.category.description,
                "Описание категории",
                this::emptyToText,
                it -> {
                    this.category.description = it;
                    BetterChatMod.autosave();
                }
        );
        //
        if (this.category == BetterChatMod.ALL_CATEGORY) {
            addSettingsWidget(new AllChatVanillaSettingsWidget());
        } else {
            if (this.category != BetterChatMod.COMMON_CATEGORY) {
                addTextField(
                        this.category.command,
                        "Команда (Выполняется при переключении на категорию)",
                        this::emptyToNull,
                        it -> {
                            this.category.command = it;
                            BetterChatMod.autosave();
                        }
                );
            }
            addTextField(
                    this.category.prefix,
                    "Префикс (Добавляется в начале сообщения при его отправке",
                    this::emptyToNull,
                    it -> {
                        this.category.prefix = it;
                        BetterChatMod.autosave();
                    }
            );
            addTextField(
                    this.category.pattern,
                    "Шаблон (Регулярное выражение для фильтрации принимаемых сообщений)",
                    this::emptyToNull,
                    it -> {
                        this.category.pattern = it;
                        BetterChatMod.autosave();
                    }
            );
            if (this.category != BetterChatMod.COMMON_CATEGORY) {
                this.addSettingsWidget(new ReplacePatternSettingsWidget(this));
                this.addSettingsWidget(new ShowInCommonSettingsWidget(this));
                this.addSettingsWidget(new DeleteCategoryWidget(this));
            }
        }
    }

    protected void addTextField(String initial, String description, Supplier<String> ifEmpty, Consumer<String> changedListener) {
        var field = new CenteredTextFieldWidget(this.textRenderer, this.tabsLength, 20, Text.of("Название"));
        field.setFocusUnlocked(true);
        field.setEditableColor(Colors.YELLOW);
        field.setUneditableColor(Colors.WHITE);
        field.setDrawsBackground(false);
        field.setTooltip(Tooltip.of(Text.of(description)));
        field.setMaxLength(256);
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
