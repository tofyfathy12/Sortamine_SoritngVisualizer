package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertionSort implements SortingStrategy {

    private int comparisons = 0;
    private int interchanges = 0;

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
                sortingEvents.add(new CompareEvent(j, i));
                data[j + 1] = data[j];
//                sortingEvents.add(new OverwriteEvent(j + 1, data[j]));
                sortingEvents.add(new SwapEvent(j + 1, j));
                interchanges++;
                j--;
            }
            if (j >= 0) {
                comparisons++;
                sortingEvents.add(new CompareEvent(j, i)); // when the while check fails
            }
            data[j + 1] = key;
//            if (j + 1 != i) {
//                sortingEvents.add(new SwapEvent(j + 1, i));
//                interchanges++;
//            }
        }
        return sortingEvents;
    }

    @Override
    public String getAlgorithmName() {
        return "Insertion Sort";
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getInterchanges() {
        return interchanges;
    }

    public static void main(String[] args) {
        InsertionSort insertionSort = InsertionSort.getInstance();
        int[] arr = { 5, 3, 8, 1, 9, 2, 7, 4, 6 };
        System.out.println("Original: " + Arrays.toString(arr));
        List<SortingEvent> events = insertionSort.generateSortHistory(arr);
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
        System.out.println("|       INSERTION SORT STATS           |");
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
