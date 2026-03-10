package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertionSort implements SortingStrategy {

    private long comparisons = 0;
    private long interchanges = 0;

    private static InsertionSort instance;

    private InsertionSort() {
    }

    public static InsertionSort getInstance() {
        if (instance == null) {
            instance = new InsertionSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;

        int[] data = Arrays.copyOf(originalData, originalData.length);
        List<SortingEvent> sortingEvents = new ArrayList<>();
        for (int i = 1; i < data.length; i++) {
            int key = data[i];
            int j = i - 1;
            while (j >= 0 && data[j] > key) {
                comparisons++;
                sortingEvents.add(new CompareEvent(j, j + 1));
                data[j + 1] = data[j];
                sortingEvents.add(new SwapEvent(j, j + 1));
                interchanges++;
                j--;
            }
            if (j >= 0) {
                comparisons++;
                sortingEvents.add(new CompareEvent(j, j + 1)); // when the while check fails
            }
            data[j + 1] = key;
        }
        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Insertion Sort";
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getInterchanges() {
        return interchanges;
    }
}
