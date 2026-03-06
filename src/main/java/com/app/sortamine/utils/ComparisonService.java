package com.app.sortamine.utils;

import com.app.sortamine.algorithms.SortingStrategy;
import com.app.sortamine.algorithms.StrategyType;
import com.app.sortamine.models.ComparisonResult;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.function.Consumer;

public class ComparisonService {
    public static TableView<ComparisonResult> createComparisonTable(ObservableList<ComparisonResult> data) {
        TableView<ComparisonResult> table = new TableView<>();
        table.setItems(data);

        TableColumn<ComparisonResult, String> algorithmCol = new TableColumn<>("Algorithm");
        algorithmCol.setCellValueFactory(new PropertyValueFactory<>("algorithmName"));

        TableColumn<ComparisonResult, Integer> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("arraySize"));

        TableColumn<ComparisonResult, String> typeCol = new TableColumn<>("Array Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("arrayType"));

        TableColumn<ComparisonResult, Integer> runsCol = new TableColumn<>("Runs");
        runsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfRuns"));

        TableColumn<ComparisonResult, Long> avgCol = new TableColumn<>("Avg Runtime (ms)");
        avgCol.setCellValueFactory(new PropertyValueFactory<>("averageRuntime"));

        TableColumn<ComparisonResult, Long> minCol = new TableColumn<>("Min Runtime (ms)");
        minCol.setCellValueFactory(new PropertyValueFactory<>("minRuntime"));

        TableColumn<ComparisonResult, Long> maxCol = new TableColumn<>("Max Runtime (ms)");
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxRuntime"));

        TableColumn<ComparisonResult, Integer> compCol = new TableColumn<>("Comparisons");
        compCol.setCellValueFactory(new PropertyValueFactory<>("comparisons"));

        TableColumn<ComparisonResult, Integer> interCol = new TableColumn<>("Interchanges");
        interCol.setCellValueFactory(new PropertyValueFactory<>("interchanges"));

        table.getColumns().addAll(algorithmCol, sizeCol, typeCol, runsCol,
                avgCol, minCol, maxCol, compCol, interCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    public static void runComparison(int arraySize, int numberOfRuns, ArrayType arrayType,
            ProgressIndicator progressIndicator,
            ObservableList<ComparisonResult> results, Consumer<Boolean> onSortingStateChange,
            java.util.function.BooleanSupplier isSortingCheck) {

        String arrayTypeName = arrayType.toString();
        StrategyType[] allAlgorithms = StrategyType.values();
        int totalSteps = allAlgorithms.length * numberOfRuns;

        new Thread(() -> {
            try {
                int stepsDone = 0;

                for (StrategyType strategyType : allAlgorithms) {
                    if (!isSortingCheck.getAsBoolean())
                        break;

                    SortingStrategy strategy = strategyType.getStrategy();

                    long totalRuntime = 0;
                    long minRuntime = Long.MAX_VALUE;
                    long maxRuntime = Long.MIN_VALUE;
                    int comparisons = 0;
                    int interchanges = 0;

                    for (int run = 0; run < numberOfRuns; run++) {
                        if (!isSortingCheck.getAsBoolean())
                            break;

                        int[] array = arrayType.getGenerator().generate(arraySize, arraySize);
                        long startTime = System.nanoTime();
                        strategy.generateSortHistory(array);
                        long endTime = System.nanoTime();

                        long runtime = endTime - startTime;
                        totalRuntime += runtime;
                        minRuntime = Math.min(minRuntime, runtime);
                        maxRuntime = Math.max(maxRuntime, runtime);

                        comparisons = strategy.getComparisons();
                        interchanges = strategy.getInterchanges();

                        stepsDone++;
                        final int currentStep = stepsDone;
                        Platform.runLater(() -> progressIndicator.setProgress((double) currentStep / totalSteps));
                    }

                    long avgRuntime = totalRuntime / numberOfRuns;

                    ComparisonResult result = new ComparisonResult(
                            strategy.getAlgorithmName(),
                            arraySize,
                            arrayTypeName,
                            numberOfRuns,
                            ((double) Math.round(((double) avgRuntime * 1000 / 1_000_000)) / 1000),
                            ((double) Math.round(((double) minRuntime * 1000 / 1_000_000)) / 1000),
                            ((double) Math.round(((double) maxRuntime * 1000 / 1_000_000)) / 1000),
                            comparisons,
                            interchanges);

                    Platform.runLater(() -> results.add(result));
                }

                Platform.runLater(() -> showComparisonChart(results));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                onSortingStateChange.accept(false);
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    public static void showComparisonChart(ObservableList<ComparisonResult> comparisonResults) {
        if (comparisonResults.isEmpty())
            return;

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Sorting Algorithm");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Average Runtime (ms)");

        BarChart<String, Number> runtimeChart = new BarChart<>(xAxis, yAxis);
        runtimeChart.setTitle("Sorting Algorithm Comparison");

        XYChart.Series<String, Number> runtimeSeries = new XYChart.Series<>();
        runtimeSeries.setName("Avg Runtime (ms)");

        XYChart.Series<String, Number> compSeries = new XYChart.Series<>();
        compSeries.setName("Comparisons (÷1000)");

        XYChart.Series<String, Number> interSeries = new XYChart.Series<>();
        interSeries.setName("Interchanges (÷1000)");

        int startIndex = Math.max(0, comparisonResults.size() - StrategyType.values().length);
        for (int i = startIndex; i < comparisonResults.size(); i++) {
            ComparisonResult r = comparisonResults.get(i);
            runtimeSeries.getData().add(new XYChart.Data<>(r.getAlgorithmName(), r.getAverageRuntime()));
            compSeries.getData().add(new XYChart.Data<>(r.getAlgorithmName(), r.getComparisons() / 1000.0));
            interSeries.getData().add(new XYChart.Data<>(r.getAlgorithmName(), r.getInterchanges() / 1000.0));
        }

        runtimeChart.getData().addAll(runtimeSeries, compSeries, interSeries);
        runtimeChart.setAnimated(true);

        Stage chartStage = new Stage();
        chartStage.setTitle("Sortamine — Comparison Chart");
        chartStage.setScene(new Scene(runtimeChart, 900, 500));
        chartStage.show();
    }
}
