package com.cgvsu.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import com.cgvsu.rasterization.*;
import javafx.scene.paint.Color;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        Rasterization.drawEllipse(canvas.getGraphicsContext2D(), 100, 100, 60, 80, Color.RED);

        Rasterization.fillEllipse(canvas.getGraphicsContext2D(), 400, 100, 80, 60, Color.RED);


        Rasterization.fillEllipseGradient(canvas.getGraphicsContext2D(), 300, 300, 100, 60,
                new Color[]{Color.RED, Color.BLUEVIOLET, Color.AQUA},
                new float[]{0.2f,0.5f, 0.8f});
    }

}