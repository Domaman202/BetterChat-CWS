package ru.cws.betterchat.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;

import java.time.Instant;
import java.util.Objects;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
    /**
     * @author DomamaN202
     * @reason Z
     */
    @Overwrite
    public void onGameMessage(Text message, boolean overlay) {
        var content = ChatCategory.getAcceptingContent(ChatCategory.getAcceptingString(message));
        if (!Objects.equals(content.sender(), MinecraftClient.getInstance().getGameProfile().getName())) {
            for (ChatCategory category : BetterChatMod.CATEGORIES)
                category.tryAccept(message);
            BetterChatMod.SELECTED_CATEGORY.refreshMessages();
        }
    }

    @Inject(method = "processChatMessageInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))
    private void processChatMessageInternalAdd(MessageType.Parameters params, SignedMessage message, Text decorated, GameProfile sender, boolean onlyShowSecureChat, Instant receptionTimestamp, CallbackInfoReturnable<Boolean> cir) {
        if (!sender.getId().equals(MinecraftClient.getInstance().player.getGameProfile().getId())) {
            for (ChatCategory category : BetterChatMod.CATEGORIES)
                category.tryAccept(decorated);
            BetterChatMod.SELECTED_CATEGORY.refreshMessages();
        }
    }

    @Redirect(method = "processChatMessageInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))
    private void processChatMessageInternalRemove(ChatHud instance, Text message, MessageSignatureData signatureData, MessageIndicator indicator) {

    }
}
