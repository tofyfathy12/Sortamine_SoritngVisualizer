package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort implements SortingStrategy {
    private static MergeSort instance;

    private int comparisons = 0;
    private int interchanges = 0;
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

    public int getComparisons() {
        return comparisons;
    }

    public int getInterchanges() {
        return interchanges;
    }

    public static void main(String[] args) {
        MergeSort mergeSort = MergeSort.getInstance();
        int[] arr = { 5, 3, 8, 1, 9, 2, 7, 4, 6 };
        System.out.println("Original: " + Arrays.toString(arr));
        List<SortingEvent> events = mergeSort.generateSortHistory(arr);
        // Replay events on a copy to show the sorted result
        int[] sorted = Arrays.copyOf(arr, arr.length);
        for (SortingEvent e : events) {
            if (e instanceof OverwriteEvent) {
                OverwriteEvent o = (OverwriteEvent) e;
                sorted[o.targetIndex] = o.newValue;
            } else if (e instanceof SwapEvent) {
                SwapEvent s = (SwapEvent) e;
                int temp = sorted[s.indexA];
                sorted[s.indexA] = sorted[s.indexB];
                sorted[s.indexB] = temp;
            }
        }
        System.out.println("Sorted:   " + Arrays.toString(sorted));

        int compares = 0, swaps = 0, overwrites = 0;
        for (SortingEvent e : events) {
            if (e instanceof CompareEvent)
                compares++;
            else if (e instanceof SwapEvent)
                swaps++;
            else if (e instanceof OverwriteEvent)
                overwrites++;
        }

        System.out.println();
        System.out.println("+=====================================+");
        System.out.println("|         MERGE SORT STATS             |");
        System.out.println("+=====================================+");
        System.out.printf("| \u001b[33m%-14s\u001b[0m : %-18d |%n", "Comparisons", compares);
        System.out.printf("| \u001b[31m%-14s\u001b[0m : %-18d |%n", "Swaps", swaps);
        System.out.printf("| \u001b[32m%-14s\u001b[0m : %-18d |%n", "Overwrites", overwrites);
        System.out.println("+-------------------------------------+");
        System.out.printf("| %-14s : %-18d |%n", "Total Events", events.size());
        System.out.println("+=====================================+");

        System.out.println();
        System.out.printf("%-6s | %-12s | %s%n", "Step", "Event", "Details");
        System.out.println("-------+--------------+-------------------------");
        for (int i = 0; i < events.size(); i++) {
            SortingEvent e = events.get(i);
            if (e instanceof CompareEvent) {
                CompareEvent c = (CompareEvent) e;
                System.out.printf("%-6d | \u001b[33m%-12s\u001b[0m | compared index [%d] with [%d]%n", i + 1, "COMPARE",
                        c.indexA, c.indexB);
            } else if (e instanceof OverwriteEvent) {
                OverwriteEvent o = (OverwriteEvent) e;
                System.out.printf("%-6d | \u001b[32m%-12s\u001b[0m | set index [%d] = %d%n", i + 1, "OVERWRITE",
                        o.targetIndex, o.newValue);
            } else if (e instanceof SwapEvent) {
                SwapEvent s = (SwapEvent) e;
                System.out.printf("%-6d | \u001b[31m%-12s\u001b[0m | swapped index [%d] <-> [%d]%n", i + 1, "SWAP",
                        s.indexA, s.indexB);
            }
        }
    }
}
