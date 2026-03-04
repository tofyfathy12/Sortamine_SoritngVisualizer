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

    private int comparisons = 0;
    private int interchanges = 0;
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

    public int getComparisons() {
        return comparisons;
    }

    public int getInterchanges() {
        return interchanges;
    }

    public static void main(String[] args) {
        QuickSort quickSort = QuickSort.getInstance();
        int[] arr = { 5, 3, 8, 1, 9, 2, 7, 4, 6 };
        System.out.println("Original: " + Arrays.toString(arr));
        List<SortingEvent> events = quickSort.generateSortHistory(arr);
        // Replay events on a copy to show the sorted result
        int[] sorted = Arrays.copyOf(arr, arr.length);
        for (SortingEvent e : events) {
            if (e instanceof SwapEvent) {
                SwapEvent s = (SwapEvent) e;
                int temp = sorted[s.indexA];
                sorted[s.indexA] = sorted[s.indexB];
                sorted[s.indexB] = temp;
            }
        }
        System.out.println("Sorted:   " + Arrays.toString(sorted));

        int compares = 0, swaps = 0;
        for (SortingEvent e : events) {
            if (e instanceof CompareEvent)
                compares++;
            else if (e instanceof SwapEvent)
                swaps++;
        }

        System.out.println();
        System.out.println("+=====================================+");
        System.out.println("|         QUICK SORT STATS             |");
        System.out.println("+=====================================+");
        System.out.printf("| \u001b[33m%-14s\u001b[0m : %-18d |%n", "Comparisons", compares);
        System.out.printf("| \u001b[31m%-14s\u001b[0m : %-18d |%n", "Swaps", swaps);
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
            } else if (e instanceof SwapEvent) {
                SwapEvent s = (SwapEvent) e;
                System.out.printf("%-6d | \u001b[31m%-12s\u001b[0m | swapped index [%d] <-> [%d]%n", i + 1, "SWAP",
                        s.indexA, s.indexB);
            }
        }
    }
}
