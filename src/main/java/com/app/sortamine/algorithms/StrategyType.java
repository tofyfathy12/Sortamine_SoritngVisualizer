package com.app.sortamine.algorithms;

public enum StrategyType {
    BUBBLE_SORT("Bubble Sort", BubbleSort.getInstance()),
    HEAP_SORT("Heap Sort", HeapSort.getInstance()),
    INSERTION_SORT("Insertion Sort", InsertionSort.getInstance()),
    MERGE_SORT("Merge Sort", MergeSort.getInstance()),
    QUICK_SORT("Quick Sort", QuickSort.getInstance()),
    SELECTION_SORT("Selection Sort", SelectionSort.getInstance());

    private final String displayName;
    private final SortingStrategy strategy;

    StrategyType(String displayName, SortingStrategy strategy) {
        this.displayName = displayName;
        this.strategy = strategy;
    }

    public SortingStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
