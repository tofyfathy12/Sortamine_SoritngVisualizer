package com.app.sortamine.utils;

import com.app.sortamine.events.*;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.CountDownLatch;

public class AnimationVisitor implements SortingEventVisitor {

    private final Rectangle[] bars;
    private final int[] arr;
    private final Pane topPane;
    private final int maxVal;
    private volatile int speedValue;
    private int comparisons = 0;
    private int interchanges = 0;

    // Two-tier fields (set to null when not in two-tier mode)
    private Rectangle[] bottomBars;
    private Pane bottomPane;
    private boolean useTwoTier;

    public AnimationVisitor(Rectangle[] bars, int[] arr, Pane topPane, int maxVal, int speedValue) {
        this.bars = bars;
        this.arr = arr;
        this.topPane = topPane;
        this.maxVal = maxVal;
        this.speedValue = speedValue;
    }

    public void enableTwoTier(Rectangle[] bottomBars, Pane bottomPane) {
        this.bottomBars = bottomBars;
        this.bottomPane = bottomPane;
        this.useTwoTier = true;
    }

    public void setSpeedValue(int speedValue) {
        this.speedValue = speedValue;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getInterchanges() {
        return interchanges;
    }

    @Override
    public void visit(SwapEvent swap) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Rectangle rec1 = bars[swap.indexA];
            Rectangle rec2 = bars[swap.indexB];
            bars[swap.indexA] = rec2;
            bars[swap.indexB] = rec1;

            int temp = arr[swap.indexA];
            arr[swap.indexA] = arr[swap.indexB];
            arr[swap.indexB] = temp;

            Animation anim = SortingAnimator.createSwapAnimation(
                    rec1, rec2, swap.indexA, swap.indexB,
                    topPane.getWidth(), arr.length, speedValue);
            anim.setOnFinished(e -> latch.countDown());
            anim.play();
        });
        latch.await();
        interchanges++;
    }

    @Override
    public void visit(CompareEvent compare) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Animation anim = SortingAnimator.createColorFlashAnimation(
                    bars[compare.indexA], bars[compare.indexB],
                    Color.YELLOW, Color.STEELBLUE, speedValue);
            anim.setOnFinished(e -> latch.countDown());
            anim.play();
        });
        latch.await();
        comparisons++;
    }

    @Override
    public void visit(OverwriteEvent overwrite) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        if (useTwoTier) {
            Platform.runLater(() -> {
                bottomBars[overwrite.targetIndex].setFill(Color.STEELBLUE);
                bottomBars[overwrite.targetIndex].setOpacity(1.0);
                Animation anim = SortingAnimator.createHeightChangeAnimation(
                        bottomBars[overwrite.targetIndex], overwrite.newValue,
                        maxVal, bottomPane.getHeight(), speedValue);
                anim.setOnFinished(e -> latch.countDown());
                anim.play();
            });
        } else {
            Platform.runLater(() -> {
                arr[overwrite.targetIndex] = overwrite.newValue;
                bars[overwrite.targetIndex].setFill(Color.STEELBLUE);
                bars[overwrite.targetIndex].setOpacity(1.0);
                bars[overwrite.targetIndex].setUserData("OVERWRITTEN");
                Animation anim = SortingAnimator.createHeightChangeAnimation(
                        bars[overwrite.targetIndex], overwrite.newValue,
                        maxVal, topPane.getHeight(), speedValue);
                anim.setOnFinished(e -> latch.countDown());
                anim.play();
            });
        }
        latch.await();
        interchanges++;
    }

    @Override
    public void visit(MinSelectionEvent minSelection) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Animation anim = SortingAnimator.createSingleColorFlashAnimation(
                    bars[minSelection.minIndex],
                    Color.LIMEGREEN, Color.STEELBLUE, speedValue);
            anim.setOnFinished(e -> latch.countDown());
            anim.play();
        });
        latch.await();
    }

    @Override
    public void visit(MaxSelectionEvent maxSelection) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Animation anim = SortingAnimator.createSingleColorFlashAnimation(
                    bars[maxSelection.maxIndex],
                    Color.LIMEGREEN, Color.STEELBLUE, speedValue);
            anim.setOnFinished(e -> latch.countDown());
            anim.play();
        });
        latch.await();
    }

    @Override
    public void visit(ConsumeEvent consume) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            if (useTwoTier || !"OVERWRITTEN".equals(bars[consume.index].getUserData())) {
                Animation anim = SortingAnimator.createConsumeAnimation(
                        bars[consume.index], speedValue);
                anim.setOnFinished(e -> latch.countDown());
                anim.play();
            } else {
                latch.countDown();
            }
        });
        latch.await();
    }

    @Override
    public void visit(BlockEvent block) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Animation anim = SortingAnimator.createBlockColorAnimation(
                    bars, block.startIndex, block.endIndex, block.color, speedValue);
            anim.setOnFinished(e -> latch.countDown());
            anim.play();
        });
        latch.await();
    }
}
