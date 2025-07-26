package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.gui.widget.ChatWidget;

public class CategoryWidget extends ChatWidget {
    public final ChatCategory category;

    public CategoryWidget(int x, int y, ChatCategory category) {
        this(x, y, getWidth(category), 20, category);
    }

    public CategoryWidget(int i, int j, int k, int l, ChatCategory category) {
        super(i, j, k, l, Text.of(category.name), Text.of(category.description));
        this.category = category;
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x806060F0);
    }

    public static int getWidth(ChatCategory category) {
        return BetterChatMod.FIXED_TAB_SIZE ? BetterChatMod.CHAT_VIEW_TAB_SIZE : (category.name.length() * 10);
    }

    public void updateActive() {
        this.active = this.category != BetterChatMod.SELECTED_CATEGORY;
    }

    @Override
    public void onPress() {
        BetterChatMod.SELECTED_CATEGORY = this.category;
        this.category.onOpen();
        this.active = false;
        for (CategoryWidget tab : BetterChatMod.CHAT_SCREEN.BetterChat$tabs()) {
            tab.updateActive();
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, ColorHelper.withAlpha(this.alpha, this.active ? Colors.WHITE : Colors.LIGHT_GRAY));
    }
}
