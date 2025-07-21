package ru.cws.betterchat.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.cws.betterchat.BetterChatMod;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), argsOnly = true)
    public String sendChatMessage(String content) {
        return BetterChatMod.SELECTED_CATEGORY.formatToSend(content);
    }
}
