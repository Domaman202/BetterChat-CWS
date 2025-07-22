package ru.cws.betterchat.util;

import ru.cws.betterchat.gui.widget.chat.CategoryWidget;
import ru.cws.betterchat.gui.widget.chat.GlobalLocalWidget;

import java.util.List;

public interface IChatScreen {
    List<CategoryWidget> BetterChat$tabs();
    GlobalLocalWidget BetterChat$globalLocalWidget();
}
