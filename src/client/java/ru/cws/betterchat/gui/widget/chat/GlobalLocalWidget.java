package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;

public abstract class GlobalLocalWidget extends ChatWidget {
    public GlobalLocalWidget(int x, int y, int width) {
        super(x, y, width, 20, null, Text.of("Перевод чата в локальный / глобальный режим"));
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x60FF4040);
    }

    public static GlobalLocalWidget create(int x, int y) {
        return BetterChatMod.NO_HEAVY_TEXTURES ? new Texted(x, y) : new Textured(x, y);
    }

    @Override
    public void onPress() {
        if (BetterChatMod.SELECTED_CATEGORY == BetterChatMod.COMMON_CATEGORY) {
            BetterChatMod.GLOBAL_LOCAL = !BetterChatMod.GLOBAL_LOCAL;
            BetterChatMod.tryCommand(BetterChatMod.GLOBAL_LOCAL ? "g" : "lc");
        }
    }

    public static class Textured extends GlobalLocalWidget {
        private static final Identifier TEXTURE_GLOBAL = Identifier.of("betterchat", "textures/gui/g.png");
        private static final Identifier TEXTURE_LOCAL = Identifier.of("betterchat", "textures/gui/l.png");

        public Textured(int x, int y) {
            super(x, y, 20);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            // Рендерим текстуру
            context.drawTexture(RenderPipelines.GUI_TEXTURED, BetterChatMod.GLOBAL_LOCAL ? TEXTURE_GLOBAL : TEXTURE_LOCAL, this.getX() + 2, this.getY() + 2, 0f, 0f, this.width - 4, this.height - 4, 16, 16);
            // Рендерим рамку
            this.flexRender.render(context, mouseX, mouseY);
        }
    }

    public static class Texted extends GlobalLocalWidget {
        public Texted(int x, int y) {
            super(x, y, 30);
            this.update();
        }

        @Override
        public void onPress() {
            super.onPress();
            this.update();
        }

        private void update() {
            this.setMessage(Text.of(BetterChatMod.GLOBAL_LOCAL ? "[G]" : "[L]"));
        }
    }
}
