package ru.cws.betterchat.gui.widget;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.cws.betterchat.util.ITabListenScreen;

public abstract class ListLeftWidget extends ChatWidget {
    private final ITabListenScreen screen;

    public ListLeftWidget(ITabListenScreen screen, boolean chatSettings, int width) {
        super(0, 0, width, 20, Text.of("[◀]"), Text.of("Просмотр предыдущих категорий"));
        this.screen = screen;
        this.flexRender.setBaseColor(chatSettings ? 0x60606060 : 0x60101060);
        this.flexRender.setHoverColor(0x606060FF);
    }

    public static ListLeftWidget create(ITabListenScreen screen, boolean chatSettings) {
//        return BetterChatMod.NO_HEAVY_TEXTURES ? new Texted(screen, chatSettings) : new Textured(screen, chatSettings);
        return new Texted(screen, chatSettings); // Стрелки стрёмные были
    }

    @Override
    public void onPress() {
        var pos = this.screen.BetterCombat$getTabListPosition();
        if (pos > 0) {
            this.screen.BetterCombat$setTabListPosition(pos - 1);
            this.screen.BetterCombat$recalcTabsList();
        }
    }

    public static class Textured extends ListLeftWidget {
        private static final Identifier TEXTURE = Identifier.of("betterchat", "textures/gui/left.png");

        public Textured(ITabListenScreen screen, boolean chatSettings) {
            super(screen, chatSettings, 20);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            // Рендерим текстуру
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX() + 2, this.getY() + 2, 0f, 0f, this.width - 4, this.height - 4, 16, 16);
            // Рендерим рамку
            this.flexRender.render(context, mouseX, mouseY);
        }
    }

    public static class Texted extends ListLeftWidget {
        public Texted(ITabListenScreen screen, boolean chatSettings) {
            super(screen, chatSettings, 30);
        }
    }
}
