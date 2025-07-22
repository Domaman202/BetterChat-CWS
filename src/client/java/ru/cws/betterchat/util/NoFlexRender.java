package ru.cws.betterchat.util;

import net.minecraft.client.gui.DrawContext;

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
        context.fill(this.sized.getX(), this.sized.getY(), this.sized.getX() + this.sized.getWidth(), this.sized.getY() + this.sized.getHeight(), BASE_COLOR);
    }
}
