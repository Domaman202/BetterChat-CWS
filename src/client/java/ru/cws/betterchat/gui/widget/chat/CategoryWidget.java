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

    public CategoryWidget(ChatCategory category) {
        super(0, 0, category.name.length() * 10, 20, Text.of(category.name), Text.of(category.description));
        this.category = category;
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x806060F0);
    }

    public void updateActive() {
        this.active = this.category != BetterChatMod.SELECTED_CATEGORY;
    }

    @Override
    public void onPress() {
        BetterChatMod.SELECTED_CATEGORY = this.category;
        this.category.onOpen();
        this.active = false;
        for (CategoryWidget tab : BetterChatMod.CHAT_SCREEN.BetterChat$tabs())
            tab.updateActive();
        BetterChatMod.CHAT_SCREEN.BetterChat$globalLocalWidget().update();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, ColorHelper.withAlpha(this.alpha, this.active ? Colors.WHITE : Colors.LIGHT_GRAY));
    }
}
