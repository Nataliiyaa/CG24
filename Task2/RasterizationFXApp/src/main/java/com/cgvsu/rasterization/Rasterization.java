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
            Color color)
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
}
