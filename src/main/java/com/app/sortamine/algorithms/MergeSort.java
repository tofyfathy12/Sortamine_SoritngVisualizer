package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort implements SortingStrategy {
    private static MergeSort instance;

    private long comparisons = 0;
    private long interchanges = 0;
    private List<SortingEvent> sortingEvents = new ArrayList<>();

    private MergeSort() {
    }

    public static MergeSort getInstance() {
        if (instance == null) {
            instance = new MergeSort();
        }
        return instance;
    }

    private void mergeSorted(int[] arr, int start1, int end1, int start2, int end2) {
        int len1 = end1 - start1, len2 = end2 - start2;
        int[] tempArr = new int[len1 + len2];

        int i = start1, j = start2;
        int k = 0;
        while (i < end1 && j < end2) {
            this.comparisons++;
            this.sortingEvents.add(new CompareEvent(i, j));
            if (arr[i] <= arr[j]) {
                tempArr[k++] = arr[i++];
            } else {
                tempArr[k++] = arr[j++];
            }
        }

        while (i < end1) {
            tempArr[k++] = arr[i++];
        }
        while (j < end2) {
            tempArr[k++] = arr[j++];
        }

        for (k = 0; k < len1 + len2; k++) {
            this.sortingEvents.add(new ConsumeEvent(start1 + k));
            arr[start1 + k] = tempArr[k];
            this.sortingEvents.add(new OverwriteEvent(start1 + k, tempArr[k]));
            this.interchanges++;
        }
    }

    private void sort(int arr[], int start, int end) {
        int length = end - start;
        if (length <= 1) {
            return;
        }

        int mid = start + (end - start) / 2;

        sort(arr, start, mid);
        sort(arr, mid, end);

        mergeSorted(arr, start, mid, mid, end);
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;
        this.sortingEvents.clear();

        int[] data = Arrays.copyOf(originalData, originalData.length);
        sort(data, 0, data.length);
        return this.sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Merge Sort";
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getInterchanges() {
        return interchanges;
    }
}
