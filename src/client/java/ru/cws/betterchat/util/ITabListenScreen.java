package ru.cws.betterchat.util;

public interface ITabListenScreen {
    void BetterChat$setTabListPosition(int position);
    int BetterChat$getTabListPosition();
    int BetterChat$getTabListMaxPosition();
    void BetterChat$recalcTabsList();
}
