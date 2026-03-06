package com.app.sortamine.algorithms;

import com.app.sortamine.events.CompareEvent;
import com.app.sortamine.events.SortingEvent;
import com.app.sortamine.events.SwapEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuickSort implements SortingStrategy {
    private static QuickSort instance;

    private long comparisons = 0;
    private long interchanges = 0;
    List<SortingEvent> sortingEvents = new ArrayList<>();

    private QuickSort() {
    }

    public static QuickSort getInstance() {
        if (instance == null) {
            instance = new QuickSort();
        }
        return instance;
    }

    public void sort(int[] arr, int start, int end) {
        if (end - start <= 1) {
            return;
        }
        int pivotIndex = start + new Random().nextInt(end - start);

        if (start != pivotIndex) {
            int temp = arr[start];
            arr[start] = arr[pivotIndex];
            arr[pivotIndex] = temp;
            sortingEvents.add(new SwapEvent(start, pivotIndex));
            interchanges++;
        }

        pivotIndex = start;
        int small_i = start, big_j = start + 1;
        while (big_j < end) {
            comparisons++;
            sortingEvents.add(new CompareEvent(big_j, pivotIndex));
            if (arr[big_j] < arr[pivotIndex]) {
                small_i++;
                if (small_i != big_j) {
                    int temp = arr[small_i];
                    arr[small_i] = arr[big_j];
                    arr[big_j] = temp;
                    sortingEvents.add(new SwapEvent(small_i, big_j));
                    interchanges++;
                }
            }
            big_j++;
        }
        if (small_i != pivotIndex) {
            int temp = arr[small_i];
            arr[small_i] = arr[pivotIndex];
            arr[pivotIndex] = temp;
            sortingEvents.add(new SwapEvent(small_i, pivotIndex));
            interchanges++;
        }

        pivotIndex = small_i;

        sort(arr, start, pivotIndex);
        sort(arr, pivotIndex + 1, end);
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;
        this.sortingEvents.clear();

        int[] data = Arrays.copyOf(originalData, originalData.length);
        sort(data, 0, data.length);
        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Quick Sort";
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getInterchanges() {
        return interchanges;
    }
}
