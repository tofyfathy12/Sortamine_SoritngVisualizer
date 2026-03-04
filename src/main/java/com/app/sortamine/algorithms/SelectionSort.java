package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class SelectionSort implements SortingStrategy {
    private static SelectionSort instance;

    private int comparisons = 0;
    private int interchanges = 0;

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

    public int getComparisons() {
        return comparisons;
    }

    public int getInterchanges() {
        return interchanges;
    }

    public static void main(String[] args) {
        SelectionSort selectionSort = SelectionSort.getInstance();
        int[] arr = { 5, 3, 8, 1, 9, 2, 7, 4, 6 };
        System.out.println("Original: " + Arrays.toString(arr));
        List<SortingEvent> events = selectionSort.generateSortHistory(arr);
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

        int compares = 0, swaps = 0, minSelections = 0;
        for (SortingEvent e : events) {
            if (e instanceof CompareEvent)
                compares++;
            else if (e instanceof SwapEvent)
                swaps++;
            else if (e instanceof MinSelectionEvent)
                minSelections++;
        }

        System.out.println();
        System.out.println("+=====================================+");
        System.out.println("|       SELECTION SORT STATS           |");
        System.out.println("+=====================================+");
        System.out.printf("| \u001b[33m%-14s\u001b[0m : %-18d |%n", "Comparisons", compares);
        System.out.printf("| \u001b[31m%-14s\u001b[0m : %-18d |%n", "Swaps", swaps);
        System.out.printf("| \u001b[36m%-14s\u001b[0m : %-18d |%n", "Min Selects", minSelections);
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
            } else if (e instanceof MinSelectionEvent) {
                MinSelectionEvent m = (MinSelectionEvent) e;
                System.out.printf("%-6d | \u001b[36m%-12s\u001b[0m | new minimum at index [%d]%n", i + 1, "MIN_SELECT",
                        m.minIndex);
            }
        }
    }
}
