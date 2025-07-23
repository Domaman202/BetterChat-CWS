package ru.cws.betterchat.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.category.ChatCategory;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private GameProfile profile;

    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), argsOnly = true)
    public String sendChatMessage(String content) {
        var formatted = "<" + this.profile.getName() + "> " + content;
        for (ChatCategory category : BetterChatMod.CATEGORIES)
            if (category != BetterChatMod.SELECTED_CATEGORY)
                category.tryAccept(Text.literal(formatted));
        BetterChatMod.SELECTED_CATEGORY.tryAcceptSelected(formatted);
        BetterChatMod.SELECTED_CATEGORY.refreshMessages();
        return BetterChatMod.SELECTED_CATEGORY.formatToSend(content);
    }
}
