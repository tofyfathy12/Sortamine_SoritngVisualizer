package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class SelectionSort implements SortingStrategy {
    private static SelectionSort instance;

    private long comparisons = 0;
    private long interchanges = 0;

    private SelectionSort() {
    }

    public static SelectionSort getInstance() {
        if (instance == null) {
            instance = new SelectionSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;

        int[] data = Arrays.copyOf(originalData, originalData.length);
        List<SortingEvent> sortingEvents = new ArrayList<>();
        for (int i = 0; i < data.length - 1; i++) {
            int minIndex = i;
            sortingEvents.add(new MinSelectionEvent(i));
            for (int j = i + 1; j < data.length; j++) {
                sortingEvents.add(new CompareEvent(j, i));
                this.comparisons++;
                if (data[j] < data[minIndex]) {
                    minIndex = j;
                    sortingEvents.add(new MinSelectionEvent(j));
                }
            }
            if (i != minIndex) {
                int temp = data[i];
                data[i] = data[minIndex];
                data[minIndex] = temp;
                sortingEvents.add(new SwapEvent(i, minIndex));
                this.interchanges++;
            }
        }
        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Selection Sort";
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getInterchanges() {
        return interchanges;
    }
}
