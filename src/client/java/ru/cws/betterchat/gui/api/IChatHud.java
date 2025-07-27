package ru.cws.betterchat.gui.api;

import net.minecraft.text.Text;
import net.minecraft.util.collection.ArrayListDeque;

public interface IChatHud {
    void BetterChat$setMessages(ArrayListDeque<Text> messages);
}
