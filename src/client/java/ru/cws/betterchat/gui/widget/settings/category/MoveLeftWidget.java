package ru.cws.betterchat.gui.widget.settings.category;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.gui.widget.ListLeftWidget;
import ru.cws.betterchat.screen.CategorySettingsScreen;

public abstract class MoveLeftWidget extends ChatWidget {
    private final CategorySettingsScreen screen;

    public MoveLeftWidget(int x, int y, CategorySettingsScreen screen, int width) {
        super(x, y, width, 20, Text.of("[◀]"), Text.of("Сдвинуть категорию левее"));
        this.screen = screen;
        this.flexRender.setBaseColor(0x60101060);
        this.flexRender.setHoverColor(0x606060FF);
    }

    public static MoveLeftWidget create(int x, int y, CategorySettingsScreen screen) {
//        return BetterChatMod.NO_HEAVY_TEXTURES ? new Texted(x, y, screen) : new Textured(x, y, screen);
        return new Texted(x, y, screen); // Стрелки стрёмные были
    }

    public static int width() {
//        return BetterChatMod.NO_HEAVY_TEXTURES ? Texted.WIDTH : Textured.WIDTH;
        return Texted.WIDTH; // Стрелки стрёмные были
    }

    @Override
    public void onPress() {
        var index = BetterChatMod.CATEGORIES.indexOf(this.screen.category);
        if (index <= 1)
            return;
        BetterChatMod.CATEGORIES.remove(index);
        BetterChatMod.CATEGORIES.add(index - 1, screen.category);
        BetterChatMod.autosave();
        ListLeftWidget.onPress(this.screen);
        this.screen.BetterChat$recalcTabsList();
    }

    public static class Textured extends MoveLeftWidget {
        public static int WIDTH = 20;
        private static final Identifier TEXTURE = Identifier.of("betterchat", "textures/gui/left.png");

        public Textured(int x, int y, CategorySettingsScreen screen) {
            super(x, y, screen, WIDTH);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            // Рендерим текстуру
            context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX() + 2, this.getY() + 2, 0f, 0f, this.width - 4, this.height - 4, 16, 16);
            // Рендерим рамку
            this.flexRender.render(context, mouseX, mouseY);
        }
    }

    public static class Texted extends MoveLeftWidget {
        public static int WIDTH = 30;

        public Texted(int x, int y, CategorySettingsScreen screen) {
            super(x, y, screen, WIDTH);
        }
    }
}
