package ru.cws.betterchat.screen;

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
import ru.cws.betterchat.gui.widget.settings.AddCategoryWidget;
import ru.cws.betterchat.gui.widget.settings.CategorySettingsWidget;

import java.util.ArrayList;
import java.util.List;

public class AbstractSettingsScreen extends Screen {
    public List<CategorySettingsWidget> tabs;
    protected AddCategoryWidget addCategory;
    public int tabsLength;
    public int tabsOffset;
    public int settingsOffset;

    public AbstractSettingsScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.recalcTabs();
    }

    protected void recalcTabs() {
        if (this.addCategory != null) {
            this.remove(this.addCategory);
        }
        //
        ChatCategory selected = null;
        if (this.tabs != null) {
            selected = this.tabs.stream().filter(it -> it.selected).map(it -> it.category).findFirst().get();
            this.tabs.forEach(this::remove);
            this.tabs.clear();
        } else {
            this.tabs = new ArrayList<>();
        }
        //
        for (ChatCategory category : BetterChatMod.CATEGORIES) {
            var tab = new CategorySettingsWidget(category, this);
            if (category == selected)
                tab.selected = true;
            this.tabs.add(tab);
        }
        //
        var length = 0;
        for (var tab : this.tabs)
            length += tab.getWidth() + 1;
        var offset = this.width / 2 - length;
        for (var tab : this.tabs) {
            tab.setX(offset + length / 2);
            tab.setY(this.height / 2 - 95);
            this.addDrawableChild(tab);
            offset += tab.getWidth() + 1;
        }
        //
        this.addCategory = new AddCategoryWidget(this);
        this.addCategory.setX(offset + length / 2);
        this.addCategory.setY(this.height / 2 - 95);
        this.addDrawableChild(this.addCategory);
        offset += this.addCategory.getWidth() + 1;
        //
        this.tabsLength = Math.max(120, length);
        this.tabsOffset = offset;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Вычисляем координаты
        var x = this.width / 2;
        var y = this.height / 2;
        var xs = this.getXStart(x);
        var xe = this.getXEnd(x);
        var ys = this.getYStart(y);
        var ye = this.getYEnd(y);
        // Отрисовка фона
        context.fill(xs, ys, xe, ye, 0x60606060);
        // Отрисовка заголовка
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, x, ys + 4, Colors.WHITE);
        // Родительская отрисовка
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    protected int getXStart(int x) {
        return Math.max(4, x - this.tabsLength);
    }

    protected int getXEnd(int x) {
        return Math.min(this.width - 4, x + this.tabsLength);
    }

    protected int getYStart(int y) {
        return Math.max(4, y - 75);
    }

    protected int getYEnd(int y) {
        return Math.max(4, y - 50 + this.settingsOffset);
    }

    @Override
    public void close() {
        this.client.setScreen(new ChatScreen(""));
    }

    protected <T extends ClickableWidget> void addSettingsWidget(T widget) {
        widget.setX(this.width / 2 - widget.getWidth() / 2);
        widget.setY(this.height / 2 - 53 + this.settingsOffset);
        this.addDrawableChild(widget);
        this.settingsOffset += widget.getHeight() + 1;
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }
}
