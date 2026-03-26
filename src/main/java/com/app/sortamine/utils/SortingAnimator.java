package com.app.sortamine.utils;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SortingAnimator {

    public static long getDurationMillis(int speedValue) {
        return 5000 / (speedValue * 5L);
    }

    public static double slotXPosition(double paneWidth, int arrayLength, int index) {
        double spacing = 1.0;
        double barWidth = (paneWidth / arrayLength) - spacing;
        return index * (barWidth + spacing);
    }

    public static ParallelTransition createSwapAnimation(Rectangle rec1, Rectangle rec2,
            int targetIndexA, int targetIndexB, double paneWidth, int arrayLength, int speedValue) {
        double destForRec1 = slotXPosition(paneWidth, arrayLength, targetIndexB);
        double destForRec2 = slotXPosition(paneWidth, arrayLength, targetIndexA);

        Duration duration = Duration.millis(getDurationMillis(speedValue));
        TranslateTransition trans1 = new TranslateTransition(duration, rec1);
        TranslateTransition trans2 = new TranslateTransition(duration, rec2);

        trans1.setToX(destForRec1 - rec1.getX());
        trans2.setToX(destForRec2 - rec2.getX());

        return new ParallelTransition(trans1, trans2);
    }

    public static Timeline createHeightChangeAnimation(Rectangle bar, int newValue,
            int maxVal, double containerHeight, int speedValue) {
        double heightRatio = containerHeight / (double) maxVal;
        double newHeight = newValue * heightRatio;
        double newY = containerHeight - newHeight;

        Duration duration = Duration.millis(getDurationMillis(speedValue));
        return new Timeline(
                new KeyFrame(duration,
                        new KeyValue(bar.heightProperty(), newHeight),
                        new KeyValue(bar.yProperty(), newY)));
    }

    public static ParallelTransition createColorFlashAnimation(Rectangle rec1, Rectangle rec2,
            Color flashColor, Color originalColor, int speedValue) {
        Duration duration = Duration.millis(getDurationMillis(speedValue) / 2.0);

        FillTransition fill1On = new FillTransition(duration, rec1, originalColor, flashColor);
        FillTransition fill2On = new FillTransition(duration, rec2, originalColor, flashColor);
        FillTransition fill1Off = new FillTransition(duration, rec1, flashColor, originalColor);
        FillTransition fill2Off = new FillTransition(duration, rec2, flashColor, originalColor);

        SequentialTransition seq1 = new SequentialTransition(fill1On, fill1Off);
        SequentialTransition seq2 = new SequentialTransition(fill2On, fill2Off);

        return new ParallelTransition(seq1, seq2);
    }

    public static SequentialTransition createSingleColorFlashAnimation(Rectangle rec1,
            Color flashColor, Color originalColor, int speedValue) {
        Duration duration = Duration.millis(getDurationMillis(speedValue) / 2.0);

        FillTransition fillOn = new FillTransition(duration, rec1, originalColor, flashColor);
        FillTransition fillOff = new FillTransition(duration, rec1, flashColor, originalColor);

        return new SequentialTransition(fillOn, fillOff);
    }

    public static ParallelTransition createConsumeAnimation(Rectangle bar, int speedValue) {
        Duration duration = Duration.millis(getDurationMillis(speedValue));
        
        FillTransition fillTransition = new FillTransition(duration, bar, (Color) bar.getFill(), Color.STEELBLUE);
        FadeTransition fadeTransition = new FadeTransition(duration, bar);
        fadeTransition.setToValue(0.3);
        
        return new ParallelTransition(fillTransition, fadeTransition);
    }

    public static ParallelTransition createBlockColorAnimation(Rectangle[] bars,
            int startIndex, int endIndex, Color targetColor, int speedValue) {
        Duration duration = Duration.millis(getDurationMillis(speedValue));
        ParallelTransition parallelTransition = new ParallelTransition();
        for (int i = startIndex; i < endIndex; i++) {
            Rectangle bar = bars[i];
            Color originalColor = (Color) bar.getFill();
            FillTransition fillTransition = new FillTransition(duration, bar, originalColor, targetColor);
            parallelTransition.getChildren().add(fillTransition);
        }
        return parallelTransition;
    }
}
