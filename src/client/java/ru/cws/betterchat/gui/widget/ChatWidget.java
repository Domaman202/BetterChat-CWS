package ru.cws.betterchat.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import ru.cws.betterchat.util.IFlexRender;
import ru.cws.betterchat.util.ISized;

import java.util.function.Supplier;

public abstract class ChatWidget extends PressableWidget implements ISized {
    public static final int GRID_X_SIZE = 10;
    public static final int GRID_Y_SIZE = 20;
    public static final ButtonWidget.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    protected final IFlexRender flexRender;

    public ChatWidget(int i, int j, int k, int l, Text text, Text tooltip) {
        super(i, j, k, l, text);
        this.setTooltip(Tooltip.of(tooltip));
        this.flexRender = IFlexRender.create(this, GRID_X_SIZE, GRID_Y_SIZE);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Рендерим рамку
        this.flexRender.render(context, mouseX, mouseY);
        // Рендерим текст
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, ColorHelper.withAlpha(this.alpha, this.active ? Colors.WHITE : Colors.LIGHT_GRAY));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return DEFAULT_NARRATION_SUPPLIER.createNarrationMessage(super::getNarrationMessage);
    }

    @Override
    public int BetterChat$getX() {
        return this.getX();
    }

    @Override
    public int BetterChat$getY() {
        return this.getY();
    }

    @Override
    public int BetterChat$getWidth() {
        return this.getWidth();
    }

    @Override
    public int BetterChat$getHeight() {
        return this.getHeight();
    }
}
