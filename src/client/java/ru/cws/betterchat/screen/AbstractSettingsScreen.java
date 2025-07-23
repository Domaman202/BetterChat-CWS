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
import ru.cws.betterchat.gui.widget.ListLeftWidget;
import ru.cws.betterchat.gui.widget.ListRightWidget;
import ru.cws.betterchat.gui.widget.settings.AddCategoryWidget;
import ru.cws.betterchat.gui.widget.settings.CategorySettingsWidget;
import ru.cws.betterchat.util.ITabListenScreen;

import java.util.ArrayList;
import java.util.List;

public class AbstractSettingsScreen extends Screen implements ITabListenScreen {
    public List<CategorySettingsWidget> tabs;
    protected AddCategoryWidget addCategoryWidget;
    protected ListLeftWidget listLeftWidget;
    protected ListRightWidget listRightWidget;
    public int tabsLength;
    public int tabsOffset;
    public int settingsOffset;
    public int tabListPosition;

    public AbstractSettingsScreen(Text title) {
        super(title);
    }

    @Override
    public void BetterCombat$setTabListPosition(int position) {
        this.tabListPosition = position;
    }

    @Override
    public int BetterCombat$getTabListPosition() {
        return this.tabListPosition;
    }

    @Override
    public void BetterCombat$recalcTabsList() {
        if (this.addCategoryWidget != null)
            this.remove(this.addCategoryWidget);
        if (this.listLeftWidget != null)
            this.remove(this.listLeftWidget);
        if (this.listRightWidget != null)
            this.remove(this.listRightWidget);
        //
        ChatCategory selected = null;
        if (this.tabs != null) {
            selected = this.tabs.stream().filter(it -> it.selected).map(it -> it.category).findFirst().orElse(null);
            this.tabs.forEach(this::remove);
            this.tabs.clear();
        } else {
            this.tabs = new ArrayList<>();
        }
        //
        for (int i = 0; i < Math.min(BetterChatMod.CATEGORIES.size() - this.tabListPosition, BetterChatMod.SETTINGS_VIEW_TABS_COUNT); i++) {
            var category = BetterChatMod.CATEGORIES.get(this.tabListPosition + i);
            var tab = new CategorySettingsWidget(category, this);
            if (category == selected)
                tab.selected = true;
            this.tabs.add(tab);
        }
        //
        this.listLeftWidget = ListLeftWidget.create(this, false);
        this.listRightWidget = ListRightWidget.create(this, false);
        this.addCategoryWidget = new AddCategoryWidget(this);
        //
        var length = 0;
        for (var tab : this.tabs)
            length += tab.getWidth() + 1;
        length += listLeftWidget.getWidth() + 1;
        length += listRightWidget.getWidth() + 1;
        length += addCategoryWidget.getWidth() + 1;
        var offset = this.width / 2 - length;
        //
        this.listLeftWidget.setX(offset + length / 2);
        this.listLeftWidget.setY(this.height / 2 - 95);
        this.addDrawableChild(this.listLeftWidget);
        offset += this.listLeftWidget.getWidth() + 1;
        //
        for (var tab : this.tabs) {
            tab.setX(offset + length / 2);
            tab.setY(this.height / 2 - 95);
            this.addDrawableChild(tab);
            offset += tab.getWidth() + 1;
        }
        //
        this.addCategoryWidget.setX(offset + length / 2);
        this.addCategoryWidget.setY(this.height / 2 - 95);
        this.addDrawableChild(this.addCategoryWidget);
        offset += this.addCategoryWidget.getWidth() + 1;
        this.listRightWidget.setX(offset + length / 2);
        this.listRightWidget.setY(this.height / 2 - 95);
        this.addDrawableChild(this.listRightWidget);
        offset += this.listRightWidget.getWidth();
        //
        this.tabsLength = Math.max(120, Math.min(248, length));
        this.tabsOffset = offset;
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
        var x = this.width / 2;
        var y = this.height / 2;
        var xs = x - 249;
        var xe = x + 248;
        var ys = Math.max(4, y - 75);
        var ye = Math.max(4, y - 50 + this.settingsOffset);
        System.out.println("x: " + x + ", y: " + y + ", xs: " + xs + ", ys: " + ys + ", xe: " + xe + ", ye: " + ye);
        // Отрисовка фона
        context.fill(xs, ys, xe, ye, 0x60606060);
        // Отрисовка заголовка
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, x, ys + 4, Colors.WHITE);
        // Родительская отрисовка
        super.render(context, mouseX, mouseY, deltaTicks);
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
