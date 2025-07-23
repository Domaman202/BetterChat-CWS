package ru.cws.betterchat.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;

public class CommonChatCategory extends ChatCategory {
    public CommonChatCategory() {
        super("Общий", "Общий чат", null, "[common]", "^\\[common]", true, true);
    }

    @Override
    public void onOpen() {
        this.refreshMessages();
        BetterChatMod.tryCommand(BetterChatMod.GLOBAL_LOCAL ? "gc" : "lc");
    }

    @Override
    public void tryAccept(Text message) {
        var msg = checkReplaceAccepting(message, this.pattern(), this.replacePattern ? "" : null);
        if (msg != null) {
            this.acceptFromOther(this, message);
        }
    }

    @Override
    public void tryAcceptSelected(Text message) {
        var msg = checkReplaceAccepting(message, this.prefixPattern(), this.replacePattern ? "" : null);
        if (msg != null) {
            this.acceptSelectedFromOther(this, message);
        }
    }

    public void acceptFromOther(ChatCategory other, Text message) {
        var msg = checkReplaceAccepting(message, other.pattern(), other.replacePattern ? "" : null);
        if (msg != null) {
            this.accept(Text.literal("§r§7§l[§r§6" + other.name + "§r§7§l] §r§f§o").append(msg));
        }
    }

    public void acceptSelectedFromOther(ChatCategory other, Text message) {
        var msg = checkReplaceAccepting(message, other.prefixPattern(), this.replacePattern ? "" : null);
        if (msg != null) {
            this.accept(Text.literal("§r§7§l[§r§6" + other.name + "§r§7§l] §r§f§o").append(msg));
        }
    }
}
