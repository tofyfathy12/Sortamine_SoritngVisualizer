package com.app.sortamine;

import com.app.sortamine.algorithms.*;
import com.app.sortamine.events.*;
import com.app.sortamine.utils.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class Controller implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private ChoiceBox<ArrayType> ArrayTypeChoiceBox;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane CenterPane;
    @FXML
    private Slider SpeedSlider;
    @FXML
    private Label SpeedValueLabel;
    private volatile int speedValue;

    private ArrayGenerator arrayGenerator;

    private volatile int[] arr;
    private volatile Rectangle[] bars;
    private volatile boolean isSorting = false;

    @FXML
    private CheckBox VisualizeCheckBox;
    private boolean visualizeMode;

    @FXML
    private ListView<StrategyType> AlgorithmListView;
    private SortingStrategy sortingStrategy;
    @FXML
    private Label AlgorithmLabel;
    @FXML
    private Label RunTimeValueLabel;
    @FXML
    private Label ComparisonsValueLabel;
    @FXML
    private Label InterchangesValueLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChoiceBox(ArrayTypeChoiceBox);
        initializeSlider(SpeedSlider);
        initializeListView(AlgorithmListView);
        VisualizeCheckBox.setSelected(true);
        visualizeMode = true;
    }

    private void initializeChoiceBox(ChoiceBox<ArrayType> choiceBox) {
        ArrayTypeChoiceBox.getItems().addAll(ArrayType.values());
        ArrayTypeChoiceBox.getSelectionModel().select(0);
    }

    private void initializeSlider(Slider slider) {
        SpeedValueLabel.setText(Integer.toString((int) slider.getValue()));
        speedValue = (int) slider.getValue();

        SpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                speedValue = (int) slider.getValue();
                SpeedValueLabel.setText(Integer.toString(speedValue));
            }
        });
    }

    private void initializeListView(ListView<StrategyType> listView) {
        listView.getItems().addAll(StrategyType.values());
        listView.getSelectionModel().select(0);
        sortingStrategy = listView.getSelectionModel().getSelectedItem().getStrategy();
        AlgorithmLabel.setText(sortingStrategy.getAlgorithmName());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            sortingStrategy = newVal.getStrategy();
            AlgorithmLabel.setText(sortingStrategy.getAlgorithmName());
        });
    }

    @FXML
    public void change(ActionEvent event) {
        visualizeMode = VisualizeCheckBox.isSelected();
    }

    @FXML
    public void Generate(ActionEvent event) {
        if (isSorting)
            return;
        this.arrayGenerator = ArrayTypeChoiceBox.getValue().getGenerator();
        Random random = new Random();
        int size = 20 + random.nextInt(101);
        this.arr = arrayGenerator.generate(size, CenterPane.getHeight());
        this.bars = BarGenerator.generateBars(this.arr, CenterPane);
    }

    @FXML
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Sortamine");
        alert.setHeaderText("Why are you closing my nigga?");
        alert.setContentText("You are such a nigga if you close my sortamine !!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            isSorting = false;
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    public void stop(ActionEvent event) {
        isSorting = false;
    }

    @FXML
    public void visualizeSort(ActionEvent event) {
        if (!visualizeMode || isSorting || sortingStrategy == null || arr == null)
            return;
        isSorting = true;

        List<SortingEvent> sortingEvents = sortingStrategy.generateSortHistory(arr);

        new Thread(() -> {
            int comparisons = 0;
            int interchanges = 0;
            long time = System.nanoTime();
            try {
                for (SortingEvent sortingEvent : sortingEvents) {

                    if (!isSorting)
                        break;

                    if (sortingEvent instanceof SwapEvent swap) {
                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            Rectangle rec1 = bars[swap.indexA];
                            Rectangle rec2 = bars[swap.indexB];

                            bars[swap.indexA] = rec2;
                            bars[swap.indexB] = rec1;

                            int temp = arr[swap.indexA];
                            arr[swap.indexA] = arr[swap.indexB];
                            arr[swap.indexB] = temp;

                            Animation anim = createSwapAnimation(rec1, rec2, swap.indexA, swap.indexB);
                            anim.setOnFinished(e -> latch.countDown());
                            anim.play();
                        });
                        latch.await();

                        interchanges++;

                    } else if (sortingEvent instanceof CompareEvent compare) {
                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            Animation anim = createColorFlashAnimation(
                                    bars[compare.indexA], bars[compare.indexB],
                                    Color.YELLOW, Color.STEELBLUE);
                            anim.setOnFinished(e -> latch.countDown());
                            anim.play();
                        });
                        latch.await();
                        comparisons++;

                    } else if (sortingEvent instanceof OverwriteEvent overwrite) {
                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            arr[overwrite.targetIndex] = overwrite.newValue;

                            Animation anim = createHeightChangeAnimation(
                                    bars[overwrite.targetIndex], overwrite.newValue);
                            anim.setOnFinished(e -> latch.countDown());
                            anim.play();
                        });
                        latch.await();
                        interchanges++;

                    } else if (sortingEvent instanceof MinSelectionEvent minSelection) {
                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            Animation anim = createSingleColorFlashAnimation(
                                    bars[minSelection.minIndex],
                                    Color.LIMEGREEN, Color.STEELBLUE);
                            anim.setOnFinished(e -> latch.countDown());
                            anim.play();
                        });
                        latch.await();
                    }

                    final long elapsed = System.nanoTime() - time;
                    final int currentComparisons = comparisons;
                    final int currentInterchanges = interchanges;

                    Platform.runLater(() -> {
                        RunTimeValueLabel.setText(Long.toString(elapsed / 1_000_000_000L));
                        ComparisonsValueLabel.setText(Long.toString(currentComparisons));
                        InterchangesValueLabel.setText(Long.toString(currentInterchanges) + " s");
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                isSorting = false;
            }
        }).start();
    }

    private long getDurationMillis() {
        return 5000 / (speedValue * 10L);
    }

    private double slotXPosition(int index) {
        double spacing = 1.0;
        double barWidth = (CenterPane.getWidth() / arr.length) - spacing;
        return index * (barWidth + spacing);
    }

    private ParallelTransition createSwapAnimation(Rectangle rec1, Rectangle rec2, int targetIndexA, int targetIndexB) {
        double destForRec1 = slotXPosition(targetIndexB);
        double destForRec2 = slotXPosition(targetIndexA);

        Duration duration = Duration.millis(getDurationMillis());
        TranslateTransition trans1 = new TranslateTransition(duration, rec1);
        TranslateTransition trans2 = new TranslateTransition(duration, rec2);

        trans1.setToX(destForRec1 - rec1.getX());
        trans2.setToX(destForRec2 - rec2.getX());

        return new ParallelTransition(trans1, trans2);
    }

    private Timeline createHeightChangeAnimation(Rectangle bar, int newValue) {
        double containerHeight = CenterPane.getHeight();
        int maxVal = 0;
        for (int v : arr) {
            if (v > maxVal)
                maxVal = v;
        }
        double heightRatio = containerHeight / (double) maxVal;

        double newHeight = newValue * heightRatio;
        double newY = containerHeight - newHeight;

        Duration duration = Duration.millis(getDurationMillis());
        return new Timeline(
                new KeyFrame(duration,
                        new KeyValue(bar.heightProperty(), newHeight),
                        new KeyValue(bar.yProperty(), newY)));
    }

    private ParallelTransition createColorFlashAnimation(Rectangle rec1, Rectangle rec2,
            Color flashColor, Color originalColor) {
        Duration duration = Duration.millis(getDurationMillis() / 2.0);

        FillTransition fill1On = new FillTransition(duration, rec1, originalColor, flashColor);
        FillTransition fill2On = new FillTransition(duration, rec2, originalColor, flashColor);
        FillTransition fill1Off = new FillTransition(duration, rec1, flashColor, originalColor);
        FillTransition fill2Off = new FillTransition(duration, rec2, flashColor, originalColor);

        SequentialTransition seq1 = new SequentialTransition(fill1On, fill1Off);
        SequentialTransition seq2 = new SequentialTransition(fill2On, fill2Off);

        return new ParallelTransition(seq1, seq2);
    }

    private SequentialTransition createSingleColorFlashAnimation(Rectangle rec1,
            Color flashColor, Color originalColor) {
        Duration duration = Duration.millis(getDurationMillis() / 2.0);

        FillTransition fillOn = new FillTransition(duration, rec1, originalColor, flashColor);
        FillTransition fillOff = new FillTransition(duration, rec1, flashColor, originalColor);

        return new SequentialTransition(fillOn, fillOff);
    }
}
