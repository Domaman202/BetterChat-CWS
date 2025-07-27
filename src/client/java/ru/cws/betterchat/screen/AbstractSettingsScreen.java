package ru.cws.betterchat.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.gui.widget.ListLeftWidget;
import ru.cws.betterchat.gui.widget.ListRightWidget;
import ru.cws.betterchat.gui.widget.chat.CategoryWidget;
import ru.cws.betterchat.gui.widget.settings.AddCategoryWidget;
import ru.cws.betterchat.gui.widget.settings.CategorySettingsWidget;
import ru.cws.betterchat.util.ITabListenScreen;

import java.util.ArrayList;
import java.util.List;

public class AbstractSettingsScreen extends Screen implements ITabListenScreen {
    public List<CategorySettingsWidget> tabs;
    protected List<ClickableWidget> widgets;
    public ChatCategory selected;
    public int settingsOffset;
    public int tabListPosition;

    public AbstractSettingsScreen(Text title, ChatCategory selected) {
        super(title);
        this.selected = selected;
    }

    @Override
    public void BetterChat$setTabListPosition(int position) {
        this.tabListPosition = position;
    }

    @Override
    public int BetterChat$getTabListPosition() {
        return this.tabListPosition;
    }

    @Override
    public int BetterChat$getTabListMaxPosition() {
        return BetterChatMod.CATEGORIES.size() - this.tabs.size();
    }

    @Override
    public void BetterCombat$recalcTabsList() {
        if (this.tabs != null) {
            this.tabs.forEach(this::remove);
            this.tabs.clear();
        } else this.tabs = new ArrayList<>();
        if (this.widgets != null) {
            this.widgets.forEach(this::remove);
            this.widgets.clear();
        } else this.widgets = new ArrayList<>();
        //
        this.settingsOffset = 0;
        var offset = this.getXS();
        var y = this.getYC() - 95;
        var listLeft = ListLeftWidget.create(offset, y, this, false);
        this.widgets.add(listLeft);
        this.addDrawableChild(listLeft);
        offset += listLeft.getWidth() + 1;
        var listRight = ListRightWidget.create(offset, y, this, false);
        this.widgets.add(listRight);
        this.addDrawableChild(listRight);
        offset += listRight.getWidth() + 1;
        var addCategory = new AddCategoryWidget(offset, y, this);
        this.widgets.add(addCategory);
        this.addDrawableChild(addCategory);
        offset += addCategory.getWidth() + 1;
        // -- Добавляем вкладки -- //
        // Добавляем вкладки
        var freeSpace = this.getXE() - offset;
        for (int i = 0; i < BetterChatMod.CATEGORIES.size() - this.tabListPosition; i++) {
            var category = BetterChatMod.CATEGORIES.get(this.tabListPosition + i);
            if (category == BetterChatMod.ALL_CATEGORY)
                continue;
            var width = CategoryWidget.getWidth(category);
            if (freeSpace - width < 0)
                break;
            var tab = new CategorySettingsWidget(0, y, category, this);
            this.tabs.add(tab);
            this.addDrawableChild(tab);
            freeSpace -= width + 1;
        }
        // Расширяем вкладки с конца
        cycle: for (var reverse = this.tabs.reversed();;) {
            for (var tab : reverse) {
                if (freeSpace < 0)
                    break cycle;
                tab.setWidth(tab.getWidth() + 1);
                freeSpace--;
            }
        }
        // Выполняем перерасчёт
        for (var tab : this.tabs) {
            tab.setX(offset);
            offset += tab.getWidth() + 1;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.BetterCombat$recalcTabsList();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.BetterCombat$recalcTabsList();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Вычисляем координаты
        var y = this.getYC();
        var ys = Math.max(4, y - 75);
        var ye = Math.max(4, y - 50 + this.settingsOffset);
        // Отрисовка фона
        context.fill(this.getXS(), ys, this.getXE(), ye, 0x60606060);
        // Отрисовка заголовка
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.getXC(), ys + 4, Colors.WHITE);
        // Родительская отрисовка
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    protected int getXC() {
        return this.width / 2;
    }

    protected int getXS() {
        return this.getXC() / 2;
    }

    protected int getXE() {
        return this.getXC() / 2 + this.getXC();
    }

    protected int getYC() {
        return this.height / 2;
    }

    @Override
    public void close() {
        this.client.setScreen(new ChatScreen(""));
    }

    protected <T extends ClickableWidget> void addSettingsWidget(T widget) {
        widget.setWidth(this.getXC() - 5);
        widget.setX(this.getXC() - widget.getWidth() / 2);
        widget.setY(this.getYC() - 53 + this.settingsOffset);
        this.addDrawableChild(widget);
        this.widgets.add(widget);
        this.settingsOffset += widget.getHeight() + 1;
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }
}
