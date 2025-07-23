package ru.cws.betterchat.gui.widget.chat;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.cws.betterchat.BetterChatMod;

public abstract class AllCategoryWidget extends CategoryWidget {
    public AllCategoryWidget(int width) {
        super(0, 0, width, 20, BetterChatMod.ALL_CATEGORY);
        this.flexRender.setBaseColor(0x60606060);
        this.flexRender.setHoverColor(0x60FF4040);
    }

    public static AllCategoryWidget create() {
        return BetterChatMod.NO_HEAVY_TEXTURES ? new Texted() : new Textured();
    }

    public static class Textured extends AllCategoryWidget {
        private static final Identifier TEXTURE = Identifier.of("betterchat", "textures/gui/a.png");

        public Textured() {
            super(20);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            // Рендерим текстуру
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX() + 2, this.getY() + 2, 0f, 0f, this.width - 4, this.height - 4, 16, 16);
            // Рендерим рамку
            this.flexRender.render(context, mouseX, mouseY);
        }
    }

    public static class Texted extends AllCategoryWidget {
        public Texted() {
            super(30);
            this.setMessage(Text.of("[A]"));
        }
    }
}
