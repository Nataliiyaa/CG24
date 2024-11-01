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

    public static void drawEllipse(
            final GraphicsContext graphicsContext,
            final int a, final int b,
            final int centerX, final int centerY,
            final Color color)
    {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int x=0;
        int y=b;
        int i=a*a+b*b-(2*b*a*a);
        int limit =0;
        int del,deld;

        while(y>=limit){
            pixelWriter.setColor(x+centerX, y+centerY, color);
            pixelWriter.setColor(-x+centerX, -y+centerY, color);
            pixelWriter.setColor(x+centerX, -y+centerY, color);
            pixelWriter.setColor(-x+centerX, y+centerY, color);

            if(i<0){
                del=2*i+2*y-1;
                if(del<=0){
                    x=x+1;
                    i=i+2*x*b*b+b*b;
                }
                else{
                    x=x+1;
                    y=y-1;
                    i=i+(2*x*b*b)-(2*y*a*a)+a*a+b*b;
                }

            }
            else{
                deld=2*(i-x)-1;
                if(deld<=0){
                    x=x+1;
                    y=y-1;
                    i=i+2*(x-y+1);
                }
                else{
                    y=y-1;
                    i=i-2*y*a*a+a*a;
                }

            }
        }

    }

    public static void fillEllipse(
            final GraphicsContext graphicsContext,
            final int a, final int b,
            final int centerX, final int centerY,
            final Color color
    )
    {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        drawEllipse(graphicsContext, a, b, centerX, centerY, color);

        // Проходимся по вертикальной оси эллипса
        for (int y = -b; y <= b; y++) {
            // Находим левую и правую границы эллипса
            int leftX = centerX - findEllipseX(centerX, a, b, y); // Инвертируем результат для левой границы
            int rightX = centerX + findEllipseX(centerX, a, b, y);

            // Заполняем пиксели между границами
            for (int x = leftX; x <= rightX; x++) {

                pixelWriter.setColor(x, centerY + y, color);
            }
        }
    }

    public static void fillEllipseGradient(
            final GraphicsContext graphicsContext,
            final int a, final int b,
            final int centerX, final int centerY
    )
    {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        drawEllipse(graphicsContext, a, b, centerX, centerY, Color.BLACK);

        // Проходимся по вертикальной оси эллипса
        for (int y = -b; y <= b; y++) {
            // Находим левую и правую границы эллипса
            int leftX = centerX - findEllipseX(centerX, a, b, y); // Инвертируем результат для левой границы
            int rightX = centerX + findEllipseX(centerX, a, b, y);

            // Заполняем пиксели между границами
            for (int x = leftX; x <= rightX; x++) {

                int red = (int) (255 * (1 - Math.abs(y) / (double) b));
                int green = (int) (255 * (Math.abs(y) / (double) b));
                int blue = 0; // Синий остается нулевым

                // Устанавливаем цвет для текущего пикселя
                Color color = Color.rgb(red, green, blue);
                pixelWriter.setColor(x, centerY + y, color);
            }
        }
    }

    private static int findEllipseX(int centerX, int rx, int ry, int y) {
        // Уравнение эллипса: (x^2 / rx^2) + (y^2 / ry^2) = 1
        double x2 = rx * rx * (1 - ((double) (y * y) / (ry * ry)));
        return (int) Math.sqrt(x2); // Возвращаем только положительное значение x
    }
}
