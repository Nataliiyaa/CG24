package com.cgvsu.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawRectangle(
            final GraphicsContext graphicsContext,
            final int x, final int y,
            final int width, final int height,
            final Color color)
    {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        for (int row = y; row < y + height; ++row)
            for (int col = x; col < x + width; ++col)
                pixelWriter.setColor(col, row, color);
    }

    public static void drawEllipse(final GraphicsContext graphicsContext, int x0, int y0, int a, int b, Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int x = 0;
        int y = b;
        int error = 0;

        while (b * b * x < a * a * y) {
            drawingEllipse(pixelWriter, x0, y0, x, y, color);
            error = b * b * (x + 1) * (x + 1) + a * a * y * (y - 1) - a * a * b * b;
            x = x + 1;
            if (error >= 0) {
                y = y - 1;
            }
        }
        while (y >= 0) {
            drawingEllipse(pixelWriter, x0, y0, x, y, color);
            error = b * b * x * (x + 1) + a * a * (y - 1) * (y - 1) - a * a * b * b;
            if (error < 0) {
                x = x + 1;
            }
            y = y - 1;
        }
    }

    private static void drawingEllipse(PixelWriter pixelWriter, int x0, int y0, int x, int y, Color color) {
        pixelWriter.setColor(x0 + x, y0 + y, color);
        pixelWriter.setColor(x0 - x, y0 + y, color);
        pixelWriter.setColor(x0 + x, y0 - y, color);
        pixelWriter.setColor(x0 - x, y0 - y, color);
    }

    private static void fillingEllipse(PixelWriter pixelWriter, int x0, int y0, int x, int y, Color color) {
        for (int i = x0 - x; i <= x0 + x; i++) {
            pixelWriter.setColor(i, y0 + y, color);
            pixelWriter.setColor(i, y0 - y, color);
        }
    }

    public static void fillEllipse(final GraphicsContext graphicsContext, int x0, int y0, int a, int b, Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int x = 0;
        int y = b;
        int error = 0;

        while (b * b * x < a * a * y) {
            fillingEllipse(pixelWriter, x0, y0, x, y, color);
            error = b * b * (x + 1) * (x + 1) + a * a * y * (y - 1) - a * a * b * b;
            x = x + 1;
            if (error >= 0) {
                y = y - 1;
            }
        }
        while (y >= 0) {
            fillingEllipse(pixelWriter, x0, y0, x, y, color);
            error = b * b * x * (x + 1) + a * a * (y - 1) * (y - 1) - a * a * b * b;
            if (error < 0) {
                x = x + 1;
            }
            y = y - 1;
        }
    }

    private static void fillingEllipse2V(PixelWriter pixelWriter, int x0, int y0, int x, int y, int a, int b, Color[] colors, float[] interval) {

        for (int i = x0 - x; i <= x0 + x; i++) {
            double dist = Math.sqrt(Math.pow(i - x0, 2) + Math.pow(y, 2));
            double max = (a * b) / Math.sqrt(a * a * Math.pow(y / dist, 2) + b * b * Math.pow((i - x0) / dist, 2));
            double fraction = dist / max;

            // Проверяем интервал, в который попадает fraction, и используем соответствующие цвета
            Color color = colors[colors.length - 1]; // Цвет по умолчанию, если fraction выходит за пределы


            for (int j = 1; j < interval.length; j++) {
                if (fraction <= interval[j]) {
                    // Проверка на равные интервалы, чтобы избежать переходов
                    if (interval[j] == interval[j - 1]) {
                        color = colors[j - 1];
                    } else {
                        // Расчет нормализованной доли для текущего интервала
                        double distInInterval = fraction - interval[j - 1];
                        double maxInInterval = interval[j] - interval[j - 1];
                        double normalizedFraction = distInInterval / maxInInterval;
                        color = interpolateColor(colors[j - 1], colors[j], normalizedFraction);
                    }
                    break;
                }
            }


            pixelWriter.setColor(i, y0 + y, color);
            pixelWriter.setColor(i, y0 - y, color);
        }
    }


    public static void fillEllipse(final GraphicsContext graphicsContext, int x0, int y0, int a, int b, Color[] colors, float[] interval) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int x = 0;
        int y = b;
        int error = 0;

        while (b * b * x < a * a * y) {
            fillingEllipse2V(pixelWriter, x0, y0, x, y, a, b, colors, interval);
            error = b * b * (x + 1) * (x + 1) + a * a * y * (y - 1) - a * a * b * b;
            x = x + 1;
            if (error >= 0) {
                y = y - 1;
            }
        }
        while (y >= 0) {
            fillingEllipse2V(pixelWriter, x0, y0, x, y, a, b, colors, interval);
            error = b * b * x * (x + 1) + a * a * (y - 1) * (y - 1) - a * a * b * b;
            if (error < 0) {
                x = x + 1;
            }
            y = y - 1;
        }
        pixelWriter.setColor(x0, y0, colors[0]);
    }

    // Метод для линейной интерполяции между двумя цветами с заданием параметра удаленности
    private static Color interpolateColor(Color startColor, Color endColor, double fraction) {
        double red = startColor.getRed() + (endColor.getRed() - startColor.getRed()) * fraction;
        double green = startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * fraction;
        double blue = startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * fraction;
        double opacity = startColor.getOpacity() + (endColor.getOpacity() - startColor.getOpacity()) * fraction;

        red = Math.max(0, Math.min(1, red));
        green = Math.max(0, Math.min(1, green));
        blue = Math.max(0, Math.min(1, blue));
        return new Color(red, green, blue, opacity);
    }
}
