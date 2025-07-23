package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class HeavyTexturesSettingsWidget extends ChatWidget {
    public HeavyTexturesSettingsWidget() {
        super(0, 0, 230, 20, null, Text.of("Переключить красивые текстуры"));
        if (BetterChatMod.NO_HEAVY_TEXTURES) {
            this.setMessage(Text.of("Тяжёлые текстуры (Выкл)"));
            this.flexRender.setBaseColor(0x60601010);
            this.flexRender.setHoverColor(0x60FF1010);
        } else {
            this.setMessage(Text.of("Тяжёлые текстуры (Вкл) "));
            this.flexRender.setBaseColor(0x60106010);
            this.flexRender.setHoverColor(0x6060FF60);
        }
    }

    @Override
    public void onPress() {
        BetterChatMod.NO_HEAVY_TEXTURES = !BetterChatMod.NO_HEAVY_TEXTURES;
        BetterChatMod.autosave();
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
    }
}
