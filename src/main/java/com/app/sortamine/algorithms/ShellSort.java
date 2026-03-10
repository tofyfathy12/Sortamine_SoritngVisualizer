package com.app.sortamine.algorithms;

import com.app.sortamine.events.CompareEvent;
import com.app.sortamine.events.SortingEvent;
import com.app.sortamine.events.SwapEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellSort implements SortingStrategy {

    private long comparisons = 0;
    private long interchanges = 0;

    private static ShellSort instance;

    private ShellSort() {
    }

    public static ShellSort getInstance() {
        if (instance == null) {
            instance = new ShellSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;
        List<SortingEvent> sortingEvents = new ArrayList<>();

        int length = originalData.length;

        int[] copy = Arrays.copyOf(originalData, length);

        int gap = (length >> 1);
        while (gap > 0) {
            for (int i = gap; i < length; i++) {
                int key = copy[i];
                int j = i - gap;
                while (j >= 0 && copy[j] > key) {
                    this.comparisons++;
                    sortingEvents.add(new CompareEvent(j, j + gap));

                    copy[j + gap] = copy[j];

                    sortingEvents.add(new SwapEvent(j + gap, j));
                    this.interchanges++;

                    j -= gap;
                }
                if (j >= 0) {
                    this.comparisons++;
                    sortingEvents.add(new CompareEvent(j, j + gap));
                }
                copy[j + gap] = key;
            }
            gap = (gap >> 1);
        }

        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Shell Sort";
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
