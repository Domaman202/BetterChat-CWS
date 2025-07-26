package ru.cws.betterchat.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.collection.ArrayListDeque;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.util.CategoryHelper;
import ru.cws.betterchat.util.IChatHud;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin implements IChatHud {
    @Shadow @Final
    private List<ChatHudLine> messages;
    @Shadow
    protected abstract void refresh();
    @Shadow @Final
    private MinecraftClient client;

    @Override
    public void BetterChat$setMessages(ArrayListDeque<Text> messages) {
        this.messages.clear();
        int creationTick = this.client.inGameHud.getTicks();
        for (Text message : messages)
            this.messages.addLast(new ChatHudLine(creationTick, message, null, this.client.isConnectedToLocalServer() ? MessageIndicator.singlePlayer() : MessageIndicator.system()));
        this.refresh();
    }

    @Redirect(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V"))
    public void addMessage(ChatHud instance, ChatHudLine message) {
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("TAIL"))
    public void addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (BetterChatMod.SELECTED_CATEGORY == null)
            return;
        BetterChatMod.SELECTED_CATEGORY.refreshMessages();
    }

    /**
     * @author DomamaN202
     * @reason Z
     */
    @Overwrite
    private void addMessage(ChatHudLine message) {
        CategoryHelper.tryAcceptToAll(message.content());
    }
}
