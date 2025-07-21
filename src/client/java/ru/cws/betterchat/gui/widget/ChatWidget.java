package ru.cws.betterchat.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;

import java.util.function.Supplier;

public abstract class ChatWidget extends PressableWidget {
    private static final ButtonWidget.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    // Цвета (ARGB формат)
    private static final int BASE_COLOR = 0x60606060;
    private static final int HOVER_COLOR = 0x80FF0000;
    // Радиус влияния курсора
    private static final float INFLUENCE_RADIUS = 100.0f; // 75.0f
    private static final float INFLUENCE_RADIUS_SQ = INFLUENCE_RADIUS * INFLUENCE_RADIUS;
    // Интенсивность эффекта
    private static final float MAX_INTENSITY = 1.0f; // 0.75f
    // Размер сетки
    private static final int GRID_X_SIZE = 10;
    private static final int GRID_Y_SIZE = 20;
    // Кэш для расчета интенсивности
    private final float[] intensityCache;
    private boolean cacheValid = false;
    private int lastMouseX = -1000;
    private int lastMouseY = -1000;

    public ChatWidget(int i, int j, int k, int l, Text text, Text tooltip) {
        super(i, j, k, l, text);
        this.setTooltip(Tooltip.of(tooltip));
        this.intensityCache = new float[this.width * this.height];
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // Рендерим базовую область
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, BASE_COLOR);

        // Проверяем необходимость обновления кэша
        if (Math.abs(mouseX - lastMouseX) > 2 || Math.abs(mouseY - lastMouseY) > 2) {
            updateIntensityCache(mouseX, mouseY);
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            cacheValid = true;
        }

        // Рендерим радиальный эффект
        if (cacheValid) {
            renderCachedEffect(context);
        }

        // Рендерим текст
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, ColorHelper.withAlpha(this.alpha, this.active ? Colors.WHITE : Colors.LIGHT_GRAY));
    }

    private void updateIntensityCache(int mouseX, int mouseY) {
        final float GRID_STEP_X = this.width / (float) GRID_X_SIZE;
        final float GRID_STEP_Y = this.height / (float)GRID_Y_SIZE;

        int index = 0;
        for (int gy = 0; gy < GRID_Y_SIZE; gy++) {
            float y = this.getY() + gy * GRID_STEP_Y;
            for (int gx = 0; gx < GRID_X_SIZE; gx++) {
                float x = this.getX() + gx * GRID_STEP_X;

                float dx = x - mouseX;
                float dy = y - mouseY;
                float distSq = dx*dx + dy*dy;

                float intensity = 0;
                if (distSq <= INFLUENCE_RADIUS_SQ) {
                    float dist = (float)Math.sqrt(distSq);
                    intensity = 1.0f - Math.min(dist / INFLUENCE_RADIUS, 1.0f);
                    intensity = (float) Math.pow(intensity, 0.5);
                    intensity *= MAX_INTENSITY;
                }

                intensityCache[index++] = Math.max(0, Math.min(1, intensity));
            }
        }
    }

    private void renderCachedEffect(DrawContext context) {
        final int CELL_WIDTH = this.width / GRID_X_SIZE;
        final int CELL_HEIGHT = this.height / GRID_Y_SIZE;

        int index = 0;
        for (int gy = 0; gy < GRID_Y_SIZE; gy++) {
            int y = this.getY() + gy * CELL_HEIGHT;
            for (int gx = 0; gx < GRID_X_SIZE; gx++) {
                int x = this.getX() + gx * CELL_WIDTH;

                float intensity = intensityCache[index++];
                if (intensity > 0.01f) {
                    int color = blendColors(BASE_COLOR, HOVER_COLOR, intensity);
                    context.fill(x, y, x + CELL_WIDTH, y + CELL_HEIGHT, color);
                }
            }
        }
    }

    private int blendColors(int color1, int color2, float ratio) {
        if (ratio <= 0.0f) return color1;
        if (ratio >= 1.0f) return color2;

        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int)(a1 + (a2 - a1) * ratio);
        int r = (int)(r1 + (r2 - r1) * ratio);
        int g = (int)(g1 + (g2 - g1) * ratio);
        int b = (int)(b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return DEFAULT_NARRATION_SUPPLIER.createNarrationMessage(super::getNarrationMessage);
    }
}
