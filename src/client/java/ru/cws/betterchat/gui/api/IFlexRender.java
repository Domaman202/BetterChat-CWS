package ru.cws.betterchat.gui.api;

import net.minecraft.client.gui.DrawContext;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.FlexRender;
import ru.cws.betterchat.gui.NoFlexRender;

public interface IFlexRender {
    void setBaseColor(int baseColor);
    void setHoverColor(int hoverColor);

    void render(DrawContext context, int mouseX, int mouseY);

    static IFlexRender create(ISized sized, int gridXSize, int gridYSize) {
        return BetterChatMod.NO_FLEX ? new NoFlexRender(sized) : new FlexRender(sized, gridXSize, gridYSize);
    }
}
