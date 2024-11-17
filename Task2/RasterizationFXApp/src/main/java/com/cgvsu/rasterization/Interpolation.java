package com.cgvsu.rasterization;

import javafx.scene.paint.Color;

public class Interpolation {

    /*
     * Выполняет линейную интерполяцию между двумя цветами.
     *
     * @param startColor Начальный цвет.
     * @param endColor   Конечный цвет.
     * @param share   Доля интерполяции (от 0.0 до 1.0).
     * @return Интерполированный цвет.
     */
    public static Color interpolateColor(Color startColor, Color endColor, double share) {
        double red = startColor.getRed() + (endColor.getRed() - startColor.getRed()) * share;
        double green = startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * share;
        double blue = startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * share;
        double opacity = startColor.getOpacity() + (endColor.getOpacity() - startColor.getOpacity()) * share;

        red = Math.max(0, Math.min(1, red));
        green = Math.max(0, Math.min(1, green));
        blue = Math.max(0, Math.min(1, blue));
        return new Color(red, green, blue, opacity);
    }
}
