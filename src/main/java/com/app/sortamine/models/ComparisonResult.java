package com.app.sortamine.models;

import javafx.beans.property.*;

public class ComparisonResult {
    private final SimpleStringProperty algorithmName;
    private final SimpleIntegerProperty arraySize;
    private final SimpleStringProperty arrayType;
    private final SimpleIntegerProperty numberOfRuns;
    private final SimpleLongProperty averageRuntime;
    private final SimpleLongProperty minRuntime;
    private final SimpleLongProperty maxRuntime;
    private final SimpleIntegerProperty comparisons;
    private final SimpleIntegerProperty interchanges;

    public ComparisonResult(String algorithmName, int arraySize, String arrayType,
            int numberOfRuns, long averageRuntime, long minRuntime,
            long maxRuntime, int comparisons, int interchanges) {
        this.algorithmName = new SimpleStringProperty(algorithmName);
        this.arraySize = new SimpleIntegerProperty(arraySize);
        this.arrayType = new SimpleStringProperty(arrayType);
        this.numberOfRuns = new SimpleIntegerProperty(numberOfRuns);
        this.averageRuntime = new SimpleLongProperty(averageRuntime);
        this.minRuntime = new SimpleLongProperty(minRuntime);
        this.maxRuntime = new SimpleLongProperty(maxRuntime);
        this.comparisons = new SimpleIntegerProperty(comparisons);
        this.interchanges = new SimpleIntegerProperty(interchanges);
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

    public long getAverageRuntime() {
        return averageRuntime.get();
    }

    public SimpleLongProperty averageRuntimeProperty() {
        return averageRuntime;
    }

    public long getMinRuntime() {
        return minRuntime.get();
    }

    public SimpleLongProperty minRuntimeProperty() {
        return minRuntime;
    }

    public long getMaxRuntime() {
        return maxRuntime.get();
    }

    public SimpleLongProperty maxRuntimeProperty() {
        return maxRuntime;
    }

    public int getComparisons() {
        return comparisons.get();
    }

    public SimpleIntegerProperty comparisonsProperty() {
        return comparisons;
    }

    public int getInterchanges() {
        return interchanges.get();
    }

    public SimpleIntegerProperty interchangesProperty() {
        return interchanges;
    }
}
