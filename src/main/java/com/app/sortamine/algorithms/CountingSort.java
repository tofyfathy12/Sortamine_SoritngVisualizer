package com.app.sortamine.algorithms;

import com.app.sortamine.events.MaxSelectionEvent;
import com.app.sortamine.events.MinSelectionEvent;
import com.app.sortamine.events.OverwriteEvent;
import com.app.sortamine.events.SortingEvent;

import java.util.ArrayList;
import java.util.List;

public class CountingSort implements  SortingStrategy {

    private long comparisons = 0;
    private long interchanges = 0;

    private static CountingSort instance;

    private CountingSort() {}

    public static CountingSort getInstance() {
        if (instance == null) {
            instance = new CountingSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        comparisons = 0;
        interchanges = 0;
        List<SortingEvent> sortingEvents = new ArrayList<>();

        int max = originalData[0];
        sortingEvents.add(new MaxSelectionEvent(0));
        int min = originalData[0];
        sortingEvents.add(new MinSelectionEvent(0));
        for (int i = 1; i < originalData.length; i++) {
            if (max < originalData[i]) {
                max = originalData[i];
                sortingEvents.add(new MaxSelectionEvent(i));
            }
            if (min > originalData[i]) {
                min = originalData[i];
                sortingEvents.add(new MinSelectionEvent(i));
            }
        }

        int[] freq = new int[max - min + 1];
        for (int originalDatum : originalData) {
            freq[originalDatum - min]++;
        }
        for (int i = 1; i < freq.length; i++) {
            freq[i] += freq[i - 1];
        }

        for (int i = originalData.length - 1; i >= 0; i--) {
            sortingEvents.add(new OverwriteEvent(--freq[originalData[i] - min], originalData[i]));
        }
        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Counting Sort";
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public long getInterchanges() {
        return interchanges;
    }
}
