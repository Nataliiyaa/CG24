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


    //  рисование контура эллипса
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


    //  рисование симметричных точек
    private static void drawingEllipse(PixelWriter pixelWriter, int x0, int y0, int x, int y, Color color) {
        pixelWriter.setColor(x0 + x, y0 + y, color);
        pixelWriter.setColor(x0 - x, y0 + y, color);
        pixelWriter.setColor(x0 + x, y0 - y, color);
        pixelWriter.setColor(x0 - x, y0 - y, color);
    }


    //  заливка горизонтальной линии цветом между симметричными точками
    private static void fillingEllipse(PixelWriter pixelWriter, int x0, int y0, int x, int y, Color color) {
        for (int i = x0 - x; i <= x0 + x; i++) {
            pixelWriter.setColor(i, y0 + y, color);
            pixelWriter.setColor(i, y0 - y, color);
        }
    }


    //  заливка эллипса однородным цветом
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


    //  заливка эллипса градиентом, использую массив цветов и массив интервалов
    //  (длина массива цветов на один больше длины массива интервалов)
    private static void fillingEllipseGradient(PixelWriter pixelWriter, int x0, int y0, int x, int y, int a, int b, Color[] colors, float[] interval) {

        for (int i = x0 - x; i <= x0 + x; i++) {
            double dist = Math.sqrt(Math.pow(i - x0, 2) + Math.pow(y, 2));  // дистанция от текущего пикселя до центра
            double max = (a * b) / Math.sqrt(a * a * Math.pow(y / dist, 2) + b * b * Math.pow((i - x0) / dist, 2));  // макс дист от центра до края эллипса в направлении текущей точки
            double share = dist / max;  // доля расстояния

            // Проверяем интервал, в который попадает share, и используем соответствующие цвета
            Color color = colors[colors.length - 1]; // Цвет по умолчанию, если share выходит за пределы

            for (int j = 1; j < interval.length; j++) {
                if (share <= interval[j]) {
                    // Проверка на равные интервалы, чтобы избежать переходов и деления на 0
                    if (interval[j] == interval[j - 1]) {
                        color = colors[j - 1];
                    } else {
                        // Расчет нормализованной доли для текущего интервала
                        double distInInterval = share - interval[j - 1];
                        double maxInInterval = interval[j] - interval[j - 1];
                        double normalizedShare = distInInterval / maxInInterval;
                        color = Interpolation.interpolateColor(colors[j - 1], colors[j], normalizedShare);
                    }
                    break;
                }
            }


            pixelWriter.setColor(i, y0 + y, color);
            pixelWriter.setColor(i, y0 - y, color);
        }
    }


    // рисование одной четверти эллипса
    public static void fillEllipseGradient(final GraphicsContext graphicsContext, int x0, int y0, int a, int b, Color[] colors, float[] interval) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int x = 0;
        int y = b;
        int error = 0;

        while (b * b * x < a * a * y) {
            fillingEllipseGradient(pixelWriter, x0, y0, x, y, a, b, colors, interval);
            error = b * b * (x + 1) * (x + 1) + a * a * y * (y - 1) - a * a * b * b;
            x = x + 1;
            if (error >= 0) {
                y = y - 1;
            }
        }
        while (y >= 0) {
            fillingEllipseGradient(pixelWriter, x0, y0, x, y, a, b, colors, interval);
            error = b * b * x * (x + 1) + a * a * (y - 1) * (y - 1) - a * a * b * b;
            if (error < 0) {
                x = x + 1;
            }
            y = y - 1;
        }
        pixelWriter.setColor(x0, y0, colors[0]);
    }

}
