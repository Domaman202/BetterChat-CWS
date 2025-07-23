package ru.cws.betterchat.util;

import net.minecraft.client.gui.DrawContext;

public class FlexRender implements IFlexRender {
    // Цвета (ARGB формат)
    public int BASE_COLOR = 0x60606060;
    public int HOVER_COLOR = 0x80FF0000;
    // Радиус влияния курсора
    private static final float INFLUENCE_RADIUS = 100.0f;
    private static final float INFLUENCE_RADIUS_SQ = INFLUENCE_RADIUS * INFLUENCE_RADIUS;
    // Интенсивность эффекта
    private static final float MAX_INTENSITY = 1.0f;
    // Размер сетки
    private final int GRID_X_SIZE;
    private final int GRID_Y_SIZE;
    // Кэш для расчета интенсивности
    private final float[] intensityCache;
    private boolean cacheValid = false;
    private int lastMouseX = -1000;
    private int lastMouseY = -1000;
    // Родительский объект
    private final ISized sized;

    public FlexRender(ISized sized, int gridXSize, int gridYSize) {
        this.GRID_X_SIZE = gridXSize;
        this.GRID_Y_SIZE = gridYSize;
        this.sized = sized;
        this.intensityCache = new float[GRID_X_SIZE * GRID_Y_SIZE];
    }

    @Override
    public void setBaseColor(int baseColor) {
        this.BASE_COLOR = baseColor;
    }

    @Override
    public void setHoverColor(int hoverColor) {
        this.HOVER_COLOR = hoverColor;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        // Рендерим базовую область
        context.fill(this.sized.getX(), this.sized.getY(), this.sized.getX() + this.sized.getWidth(), this.sized.getY() + this.sized.getHeight(), BASE_COLOR);

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
    }

    private void updateIntensityCache(int mouseX, int mouseY) {
        final float GRID_STEP_X = this.sized.getWidth() / (float) GRID_X_SIZE;
        final float GRID_STEP_Y = this.sized.getHeight() / (float) GRID_Y_SIZE;

        int index = 0;
        for (int gy = 0; gy < GRID_Y_SIZE; gy++) {
            float y = this.sized.getY() + gy * GRID_STEP_Y;
            for (int gx = 0; gx < GRID_X_SIZE; gx++) {
                float x = this.sized.getX() + gx * GRID_STEP_X;

                float dx = x - mouseX;
                float dy = y - mouseY;
                float distSq = dx * dx + dy * dy;

                float intensity = 0;
                if (distSq <= INFLUENCE_RADIUS_SQ) {
                    float dist = (float) Math.sqrt(distSq);
                    intensity = 1.0f - Math.min(dist / INFLUENCE_RADIUS, 1.0f);
                    intensity = (float) Math.pow(intensity, 0.5);
                    intensity *= MAX_INTENSITY;
                }

                intensityCache[index++] = Math.max(0, Math.min(1, intensity));
            }
        }
    }

    private void renderCachedEffect(DrawContext context) {
        // Базовый размер клетки (целочисленное деление)
        final int baseCellWidth = this.sized.getWidth() / GRID_X_SIZE;
        final int baseCellHeight = this.sized.getHeight() / GRID_Y_SIZE;

        // Остатки для последней клетки
        final int widthRemainder = this.sized.getWidth() % GRID_X_SIZE;
        final int heightRemainder = this.sized.getHeight() % GRID_Y_SIZE;

        int index = 0;
        for (int gy = 0; gy < GRID_Y_SIZE; gy++) {
            int cellHeight = baseCellHeight;
            // Добавляем остаток к последней клетке по Y
            if (gy == GRID_Y_SIZE - 1 && heightRemainder > 0) {
                cellHeight += heightRemainder;
            }

            int yStart = this.sized.getY() + gy * baseCellHeight;
            int yEnd = yStart + cellHeight;

            for (int gx = 0; gx < GRID_X_SIZE; gx++) {
                int cellWidth = baseCellWidth;
                // Добавляем остаток к последней клетке по X
                if (gx == GRID_X_SIZE - 1 && widthRemainder > 0) {
                    cellWidth += widthRemainder;
                }

                int xStart = this.sized.getX() + gx * baseCellWidth;
                int xEnd = xStart + cellWidth;

                float intensity = intensityCache[index++];
                if (intensity > 0.01f) {
                    int color = blendColors(BASE_COLOR, HOVER_COLOR, intensity);
                    context.fill(xStart, yStart, xEnd, yEnd, color);
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

        int a = (int) (a1 + (a2 - a1) * ratio);
        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}