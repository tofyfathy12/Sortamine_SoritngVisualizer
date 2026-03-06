package com.app.sortamine.algorithms;

import com.app.sortamine.events.CompareEvent;
import com.app.sortamine.events.SortingEvent;
import com.app.sortamine.events.SwapEvent;
import com.app.sortamine.events.SortingEventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSort implements SortingStrategy {

    private long comparisons = 0;
    private long interchanges = 0;

    private static BubbleSort instance;

    private BubbleSort() {
    }

    public static BubbleSort getInstance() {
        if (instance == null) {
            instance = new BubbleSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        int[] data = Arrays.copyOf(originalData, originalData.length);
        this.comparisons = 0;
        this.interchanges = 0;
        List<SortingEvent> sortingEvents = new ArrayList<>();
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                this.comparisons++;
                sortingEvents.add(new CompareEvent(j, j + 1));
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;

                    sortingEvents.add(new SwapEvent(j, j + 1));
                    this.interchanges++;
                }
            }
        }
        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Bubble Sort";
    }

    @Override
    public long getComparisons() {
        return this.comparisons;
    }

    @Override
    public long getInterchanges() {
        return this.interchanges;
    }
}
