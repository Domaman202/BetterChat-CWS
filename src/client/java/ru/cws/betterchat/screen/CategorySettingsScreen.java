package ru.cws.betterchat.screen;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.*;
import ru.cws.betterchat.gui.widget.settings.category.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CategorySettingsScreen extends AbstractSettingsScreen {
    public final ChatCategory category;

    public CategorySettingsScreen(ChatCategory category, ChatCategory selected, int tabListPosition) {
        super(Text.of("Настройки категории"), selected);
        this.category = category;
        this.tabListPosition = tabListPosition;
    }

    @Override
    public void BetterChat$recalcTabsList() {
        super.BetterChat$recalcTabsList();
        // Общие настройки
        this.addTextField(
                this.category.name,
                "Название категории",
                this::emptyToText,
                it -> {
                    this.category.name = it;
                    BetterChatMod.autosave();
                    this.BetterChat$recalcTabsList();
                }
        );
        this.addTextField(
                this.category.description,
                "Описание категории",
                this::emptyToText,
                it -> {
                    this.category.description = it;
                    BetterChatMod.autosave();
                }
        );
        // Настройки зависимые от категории
        if (this.category == BetterChatMod.ALL_CATEGORY) {
            this.addSettingsWidget(new AllChatVanillaSettingsWidget());
        } else {
            // Настройки зависимые от категории
            if (this.category != BetterChatMod.COMMON_CATEGORY) {
                this.addSettingsWidget(new DeleteCategoryWidget(this));
            }
            // Стрелки
            var xc = this.getXC();
            var y = this.getYC() - 53 + this.settingsOffset;
            var left = MoveLeftWidget.create(xc - 2 - MoveLeftWidget.width(), y, this);
            this.addDrawableChild(left);
            this.widgets.add(left);
            var right = MoveRightWidget.create(xc + 2, y, this);
            this.addDrawableChild(right);
            this.widgets.add(right);
            this.settingsOffset += 21;
        }
    }

    protected void addTextField(String initial, String description, Supplier<String> ifEmpty, Consumer<String> changedListener) {
        var field = new CenteredTextFieldWidget(this.textRenderer, this.width / 2, 20, Text.of("Название"));
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
