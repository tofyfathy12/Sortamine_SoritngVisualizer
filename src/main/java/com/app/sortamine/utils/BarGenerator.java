package com.app.sortamine.utils;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BarGenerator {

    public static Rectangle[] generateBars(int[] data, Pane container) {
        container.getChildren().clear();

        double containerWidth = container.getWidth();
        double containerHeight = container.getHeight();

        int maxVal = 0;
        for (int val : data) {
            if (val > maxVal)
                maxVal = val;
        }

        double heightRatio = containerHeight / (double) maxVal;

        double spacing = 1.0;
        double barWidth = (containerWidth / data.length) - spacing;

        Rectangle[] visualBars = new Rectangle[data.length];

        for (int i = 0; i < data.length; i++) {
            double barHeight = data[i] * heightRatio;

            double xPos = i * (barWidth + spacing);

            double yPos = containerHeight - barHeight;

            Rectangle bar = new Rectangle(barWidth, barHeight, Color.STEELBLUE);
            bar.setX(xPos);
            bar.setY(yPos);

            visualBars[i] = bar;
            container.getChildren().add(bar);
        }

        return visualBars;
    }
}
