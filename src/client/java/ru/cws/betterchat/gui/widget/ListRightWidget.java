package ru.cws.betterchat.gui.widget;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.cws.betterchat.util.ITabListenScreen;

public abstract class ListRightWidget extends ChatWidget {
    private final ITabListenScreen screen;
    private final boolean chatSettings;

    public ListRightWidget(int x, int y, ITabListenScreen screen, boolean chatSettings, int width) {
        super(x, y, width, 20, Text.of("[▶]"), Text.of("Просмотр следующих категорий"));
        this.screen = screen;
        this.chatSettings = chatSettings;
        this.flexRender.setBaseColor(chatSettings ? 0x60606060 : 0x60101060);
        this.flexRender.setHoverColor(0x606060FF);
    }

    public static ListRightWidget create(int x, int y, ITabListenScreen screen, boolean chatSettings) {
//        return BetterChatMod.NO_HEAVY_TEXTURES ? new Texted(screen, chatSettings) : new Textured(screen, chatSettings);
        return new Texted(x, y, screen, chatSettings); // Стрелки стрёмные были
    }

    @Override
    public void onPress() {
        var pos = this.screen.BetterChat$getTabListPosition();
        if (pos < this.screen.BetterChat$getTabListMaxPosition()) {
            this.screen.BetterChat$setTabListPosition(pos + 1);
            this.screen.BetterCombat$recalcTabsList();
        }
    }

    public static class Textured extends ListRightWidget {
        private static final Identifier TEXTURE = Identifier.of("betterchat", "textures/gui/right.png");

        public Textured(int x, int y, ITabListenScreen screen, boolean chatSettings) {
            super(x, y, screen, chatSettings, 20);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            // Рендерим текстуру
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX() + 2, this.getY() + 2, 0f, 0f, this.width - 4, this.height - 4, 16, 16);
            // Рендерим рамку
            this.flexRender.render(context, mouseX, mouseY);
        }
    }

    public static class Texted extends ListRightWidget {
        public Texted(int x, int y, ITabListenScreen screen, boolean chatSettings) {
            super(x, y, screen, chatSettings, 30);
        }
    }
}
