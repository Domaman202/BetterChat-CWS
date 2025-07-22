package ru.cws.betterchat.category;

import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;

public class CommonChatCategory extends ChatCategory {
    public boolean globalLocal;

    public CommonChatCategory() {
        super("Общий", "Общий чат", null, "[common]", "^\\[common]", true, true);
        this.globalLocal = true;
    }

    @Override
    public void onOpen() {
        this.refreshMessages();
        BetterChatMod.tryCommand(this.globalLocal ? "gc" : "lc");
    }

    public void tryAccept(Text message) {
        for (ChatCategory category : BetterChatMod.CATEGORIES) {
            if (!category.showInCommon)
                continue;
            var msg = checkReplaceAccepting(message, category.pattern(), "[" + category.name + "] ");
            if (msg != null) {
                this.accept(msg);
                return;
            }
        }
    }
}
