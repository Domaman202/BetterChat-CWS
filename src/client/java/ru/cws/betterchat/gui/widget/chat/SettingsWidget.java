package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class SettingsWidget extends ChatWidget {
    private static final Identifier TEXTURE = Identifier.of("betterchat", "textures/gui/settings.png");

    public SettingsWidget(int x, int y) {
        super(x, y, 20, 20, null, Text.of("Меню настроек"));
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x60FF4040);
    }

    @Override
    public void onPress() {
        MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Рендерим текстуру
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX() + 2, this.getY() + 2, 0f, 0f, this.width - 4, this.height - 4, 16, 16);
        // Рендерим рамку
        this.flexRender.render(context, mouseX, mouseY);
    }
}
