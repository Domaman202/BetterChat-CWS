package ru.cws.betterchat.gui;

import net.minecraft.client.gui.DrawContext;
import ru.cws.betterchat.gui.api.IFlexRender;
import ru.cws.betterchat.gui.api.ISized;

public class NoFlexRender implements IFlexRender {
    // Цвета (ARGB формат)
    public int BASE_COLOR = 0x60606060;
    public int HOVER_COLOR = 0x80FF0000;
    // Родительский объект
    private final ISized sized;

    public NoFlexRender(ISized sized) {
        this.sized = sized;
    }

    @Override
    public void setBaseColor(int baseColor) {
        this.BASE_COLOR = baseColor;
    }

    @Override
    public void setHoverColor(int hoverColor) {
        this.HOVER_COLOR = hoverColor;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        context.fill(this.sized.BetterChat$getX(), this.sized.BetterChat$getY(), this.sized.BetterChat$getX() + this.sized.BetterChat$getWidth(), this.sized.BetterChat$getY() + this.sized.BetterChat$getHeight(), BASE_COLOR);
    }
}
