package ru.cws.betterchat.mixin;

import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
    /**
     * @author DomamaN202
     * @reason Z
     */
    @Overwrite
    public void onGameMessage(Text message, boolean overlay) {
        for (ChatCategory category : BetterChatMod.CATEGORIES) {
            category.tryAccept(message);
        }
    }
}
