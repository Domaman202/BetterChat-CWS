package ru.cws.betterchat.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.cws.betterchat.util.CategoryHelper;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private GameProfile profile;

    @ModifyVariable(method = "sendChatMessage", at = @At("HEAD"), argsOnly = true)
    public String sendChatMessage(String content) {
        if (MinecraftClient.getInstance().isIntegratedServerRunning())
            CategoryHelper.tryAcceptSelfToAll(this.profile.getName(), content);
        return CategoryHelper.formatToSend(content);
    }
}
