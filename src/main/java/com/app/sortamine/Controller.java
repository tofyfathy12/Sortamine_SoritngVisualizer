package com.app.sortamine;

import com.app.sortamine.algorithms.*;
import com.app.sortamine.events.*;
import com.app.sortamine.models.ComparisonResult;
import com.app.sortamine.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private ChoiceBox<ArrayType> ArrayTypeChoiceBox;
    @FXML
    private ChoiceBox<File> filesChoiceBox;
    @FXML
    private Label selectedFileValueLabel;
    @FXML
    private Label selectedFileLabel;
    @FXML
    private Button AddFileButton;
    @FXML
    private VBox CenterVBox;
    @FXML
    private Pane TopPane;
    @FXML
    private Pane BottomPane;
    @FXML
    private Slider SpeedSlider;
    @FXML
    private Label SpeedValueLabel;
    @FXML
    private Button GenerateButton;
    @FXML
    private Button SortButton;
    @FXML
    private Button StopButton;
    @FXML
    private Spinner<Integer> SizeSpinner;
    @FXML
    private Spinner<Integer> NumberOfRunsSpinner;
    @FXML
    private CheckBox VisualizeCheckBox;
    @FXML
    private ListView<StrategyType> AlgorithmListView;
    @FXML
    private Label AlgorithmLabel;
    @FXML
    private Label ChosenAlgorithmLabel;
    @FXML
    private Label RunTimeValueLabel;
    @FXML
    private Label ComparisonsValueLabel;
    @FXML
    private Label InterchangesValueLabel;
    @FXML
    private ProgressIndicator SortingProgressIndicator;
    @FXML
    private Label NaryLabel;
    @FXML
    private Spinner<Integer> NarySpinner;

    private volatile int speedValue;
    private volatile int[] arr;
    private volatile Rectangle[] topBars;
    private volatile Rectangle[] bottomBars;
    private volatile boolean isSorting = false;

    private SpinnerValueFactory<Integer> visualizerSpinnerValueFactory;
    private SpinnerValueFactory<Integer> comparisonSpinnerValueFactory;

    private boolean visualizeMode;
    private SortingStrategy sortingStrategy;

    private int[] fileArray = null;
    private String fileName = null;
    private final List<File> files = new ArrayList<>();

    private TableView<ComparisonResult> resultsTable;
    private final ObservableList<ComparisonResult> comparisonResults = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeArrayTypeChoiceBox();
        initializeSlider();
        initializeListView();
        VisualizeCheckBox.setSelected(true);
        visualizeMode = true;
        initializeSpinners();
        resultsTable = ComparisonService.createComparisonTable(comparisonResults);
        initializeResizeListeners();
        initializeFilesChoiceBox();
        hideTwoTier();
    }

    private void hideTwoTier() {
        BottomPane.setVisible(false);
        BottomPane.setManaged(false);
        BottomPane.getChildren().clear();
        TopPane.prefHeightProperty().bind(CenterVBox.heightProperty());
    }

    private void showTwoTier() {
        BottomPane.setVisible(true);
        BottomPane.setManaged(true);
        TopPane.prefHeightProperty().bind(CenterVBox.heightProperty().divide(2));
        BottomPane.prefHeightProperty().bind(CenterVBox.heightProperty().divide(2));
    }

    private boolean isOutOfPlaceAlgorithm() {
        return sortingStrategy instanceof MergeSort || sortingStrategy instanceof BlockSort;
    }

    private void initializeArrayTypeChoiceBox() {
        ArrayTypeChoiceBox.getItems().addAll(ArrayType.values());
        ArrayTypeChoiceBox.getSelectionModel().select(0);
    }

    private void initializeFilesChoiceBox() {
        filesChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    fileArray = FileService.loadArrayFromFile(newVal);
                    fileName = newVal.getName();

                    this.arr = Arrays.copyOf(fileArray, fileArray.length);

                    if (visualizeMode && arr.length <= 100) {
                        rebuildBothBars();
                    }
                    this.selectedFileValueLabel.setText(fileName + "\n(" + fileArray.length + " elements)");
                } catch (IOException | NumberFormatException e) {
                    this.selectedFileValueLabel.setText("Error: " + e.getMessage());
                }
            }
        });
    }

    private void initializeSlider() {
        SpeedValueLabel.setText(Integer.toString((int) SpeedSlider.getValue()));
        speedValue = (int) SpeedSlider.getValue();

        SpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                speedValue = (int) SpeedSlider.getValue();
                SpeedValueLabel.setText(Integer.toString(speedValue));
            }
        });
    }

    private void initializeListView() {
        AlgorithmListView.getItems().addAll(StrategyType.values());
        AlgorithmListView.getSelectionModel().select(0);
        sortingStrategy = AlgorithmListView.getSelectionModel().getSelectedItem().getStrategy();
        AlgorithmLabel.setText(sortingStrategy.getAlgorithmName());

        AlgorithmListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
//                System.out.println("Spinner value: " + NarySpinner.getValue()); //debugging
                sortingStrategy = (newVal == StrategyType.N_ARY_HEAP_SORT) ? newVal.getStrategy(NarySpinner.getValue()) : newVal.getStrategy();
                AlgorithmLabel.setText(sortingStrategy.getAlgorithmName());
                NaryLabel.setVisible(newVal == StrategyType.N_ARY_HEAP_SORT);
                NarySpinner.setVisible(newVal == StrategyType.N_ARY_HEAP_SORT);

                if (visualizeMode) {
                    if (isOutOfPlaceAlgorithm()) showTwoTier();
                    else hideTwoTier();
                }
            }
        });
    }

    private void initializeSpinners() {
        visualizerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 50);
        comparisonSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10_000, 5000);
        setSpinner();
        NumberOfRunsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));

        NarySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10, 3));
        NarySpinner.setVisible(this.sortingStrategy instanceof NaryHeapSort);
        NarySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (this.sortingStrategy instanceof NaryHeapSort) {
                    this.sortingStrategy = new NaryHeapSort(newVal);
                }
            }
        });
        NaryLabel.setVisible(this.sortingStrategy instanceof NaryHeapSort);

    }

    private void initializeResizeListeners() {
        TopPane.widthProperty().addListener((obs, oldVal, newVal) -> rebuildBothBars());
        TopPane.heightProperty().addListener((obs, oldVal, newVal) -> rebuildBothBars());
        BottomPane.heightProperty().addListener((obs, oldVal, newVal) -> rebuildBothBars());
    }

    private void rebuildBothBars() {
        if (!visualizeMode || arr == null || isSorting)
            return;
        topBars = BarGenerator.generateBars(arr, TopPane);
        if (isOutOfPlaceAlgorithm()) {
            bottomBars = BarGenerator.generateEmptyBars(arr.length, 0, BottomPane);
        }
    }

    private void setSpinner() {
        SizeSpinner.setValueFactory(visualizeMode ? visualizerSpinnerValueFactory : comparisonSpinnerValueFactory);
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void visualizeCheckBoxChange(ActionEvent event) {
        visualizeMode = VisualizeCheckBox.isSelected();
        setSpinner();

        GenerateButton.setDisable(!visualizeMode);
        AddFileButton.setDisable(!visualizeMode);
        filesChoiceBox.setDisable(!visualizeMode);
        selectedFileLabel.setVisible(visualizeMode);
        selectedFileValueLabel.setVisible(visualizeMode);
        ChosenAlgorithmLabel.setVisible(visualizeMode);
        AlgorithmLabel.setVisible(visualizeMode);
        SpeedSlider.setDisable(!visualizeMode);
        AlgorithmListView.setDisable(!visualizeMode);
        StopButton.setDisable(!visualizeMode);
        NumberOfRunsSpinner.setDisable(visualizeMode);

        if (visualizeMode) {
            SortButton.setText("Sort");
            if (isOutOfPlaceAlgorithm()) showTwoTier();
            else hideTwoTier();
            TopPane.getChildren().remove(resultsTable);
            rebuildBothBars();
        } else {
            SortButton.setText("Compare");
            hideTwoTier();
            TopPane.getChildren().clear();
            resultsTable.prefWidthProperty().bind(CenterVBox.widthProperty());
            resultsTable.prefHeightProperty().bind(CenterVBox.heightProperty());
            TopPane.getChildren().add(resultsTable);
        }
    }

    @FXML
    public void Generate(ActionEvent event) {
        if (isSorting)
            return;

        filesChoiceBox.getSelectionModel().clearSelection();
        selectedFileValueLabel.setText("");
        fileArray = null;
        fileName = null;

        ArrayGenerator arrayGenerator = ArrayTypeChoiceBox.getValue().getGenerator();
        int size = SizeSpinner.getValue();

        if (visualizeMode) {
            this.arr = arrayGenerator.generate(size, TopPane.getHeight());
            rebuildBothBars();
        }
    }

    @FXML
    public void loadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Array File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = (Stage) TopPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                files.add(file);
                filesChoiceBox.getItems().add(file);
                filesChoiceBox.getSelectionModel().select(files.size() - 1);
                fileArray = FileService.loadArrayFromFile(file);
                fileName = file.getName();
                this.arr = Arrays.copyOf(fileArray, fileArray.length);

                if (visualizeMode && arr.length <= 100) {
                    this.topBars = BarGenerator.generateBars(this.arr, TopPane);
                }
                selectedFileValueLabel.setText(fileName + "\n(" + fileArray.length + " elements)");
            } catch (IOException | NumberFormatException e) {
                selectedFileValueLabel.setText("Error: " + e.getMessage());
                fileArray = null;
                fileName = null;
            }
        }
    }

    @FXML
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Closing Sortamine !!");
        alert.setHeaderText("Why are you closing :C ?");
        alert.setContentText("You are such a meanie if you close my sortamine >:C !!");

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
    public void sort(ActionEvent event) {
        if (visualizeMode) {
            visualizeSort();
        } else {
            comparisonSort();
        }
    }

    private void visualizeSort() {
        if (isSorting || sortingStrategy == null || arr == null)
            return;
        isSorting = true;

        List<SortingEvent> sortingEvents = sortingStrategy.generateSortHistory(arr);

        int maxVal = 0;
        for (int v : arr) {
            if (v > maxVal)
                maxVal = v;
        }

        AnimationVisitor visitor = new AnimationVisitor(topBars, arr, TopPane, maxVal, speedValue);

        if (isOutOfPlaceAlgorithm()) {
            if (bottomBars == null || bottomBars.length != arr.length) {
                bottomBars = BarGenerator.generateEmptyBars(arr.length, maxVal, BottomPane);
            }
            visitor.enableTwoTier(bottomBars, BottomPane);
        }

        new Thread(() -> {
            int progressCounter = 0;
            long time = System.nanoTime();
            try {
                for (SortingEvent sortingEvent : sortingEvents) {
                    if (!isSorting)
                        break;

                    visitor.setSpeedValue(speedValue);
                    sortingEvent.accept(visitor);

                    progressCounter++;
                    final long elapsed = System.nanoTime() - time;
                    final int currentComparisons = visitor.getComparisons();
                    final int currentInterchanges = visitor.getInterchanges();
                    int currentProgress = progressCounter;
                    Platform.runLater(() -> {
                        BigDecimal runTime = new BigDecimal(String.format("%.3f", (double) elapsed / 1_000_000_000L));
                        RunTimeValueLabel.setText(runTime.doubleValue() + " s");
                        ComparisonsValueLabel.setText(Long.toString(currentComparisons));
                        InterchangesValueLabel.setText(Long.toString(currentInterchanges));
                        SortingProgressIndicator.setProgress((double) currentProgress / sortingEvents.size());
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                isSorting = false;
            }
        }).start();
    }

    private void comparisonSort() {
        if (isSorting)
            return;
        isSorting = true;

        ComparisonService.runComparison(
                SizeSpinner.getValue(),
                NumberOfRunsSpinner.getValue(),
                NarySpinner.getValue(),
                ArrayTypeChoiceBox.getValue(),
                SortingProgressIndicator,
                comparisonResults,
                running -> isSorting = running,
                () -> isSorting);
    }
}