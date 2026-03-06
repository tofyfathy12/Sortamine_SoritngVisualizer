package com.app.sortamine.models;

import javafx.beans.property.*;

public class ComparisonResult {
    private final SimpleStringProperty algorithmName;
    private final SimpleIntegerProperty arraySize;
    private final SimpleStringProperty arrayType;
    private final SimpleIntegerProperty numberOfRuns;
    private final SimpleDoubleProperty averageRuntime;
    private final SimpleDoubleProperty minRuntime;
    private final SimpleDoubleProperty maxRuntime;
    private final SimpleLongProperty comparisons;
    private final SimpleLongProperty interchanges;

    public ComparisonResult(String algorithmName, int arraySize, String arrayType,
            int numberOfRuns, double averageRuntime, double minRuntime,
            double maxRuntime, long comparisons, long interchanges) {
        this.algorithmName = new SimpleStringProperty(algorithmName);
        this.arraySize = new SimpleIntegerProperty(arraySize);
        this.arrayType = new SimpleStringProperty(arrayType);
        this.numberOfRuns = new SimpleIntegerProperty(numberOfRuns);
        this.averageRuntime = new SimpleDoubleProperty(averageRuntime);
        this.minRuntime = new SimpleDoubleProperty(minRuntime);
        this.maxRuntime = new SimpleDoubleProperty(maxRuntime);
        this.comparisons = new SimpleLongProperty(comparisons);
        this.interchanges = new SimpleLongProperty(interchanges);
    }

    public String getAlgorithmName() {
        return algorithmName.get();
    }

    public SimpleStringProperty algorithmNameProperty() {
        return algorithmName;
    }

    public int getArraySize() {
        return arraySize.get();
    }

    public SimpleIntegerProperty arraySizeProperty() {
        return arraySize;
    }

    public String getArrayType() {
        return arrayType.get();
    }

    public SimpleStringProperty arrayTypeProperty() {
        return arrayType;
    }

    public int getNumberOfRuns() {
        return numberOfRuns.get();
    }

    public SimpleIntegerProperty numberOfRunsProperty() {
        return numberOfRuns;
    }

    public double getAverageRuntime() {
        return averageRuntime.get();
    }

    public SimpleDoubleProperty averageRuntimeProperty() {
        return averageRuntime;
    }

    public double getMinRuntime() {
        return minRuntime.get();
    }

    public SimpleDoubleProperty minRuntimeProperty() {
        return minRuntime;
    }

    public double getMaxRuntime() {
        return maxRuntime.get();
    }

    public SimpleDoubleProperty maxRuntimeProperty() {
        return maxRuntime;
    }

    public long getComparisons() {
        return comparisons.get();
    }

    public SimpleLongProperty comparisonsProperty() {
        return comparisons;
    }

    public long getInterchanges() {
        return interchanges.get();
    }

    public SimpleLongProperty interchangesProperty() {
        return interchanges;
    }
}
