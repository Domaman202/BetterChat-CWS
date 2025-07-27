package ru.cws.betterchat.gui.api;

public interface ITabListenScreen {
    void BetterChat$setTabListPosition(int position);
    int BetterChat$getTabListPosition();
    int BetterChat$getTabListMaxPosition();
    void BetterChat$recalcTabsList();
}
