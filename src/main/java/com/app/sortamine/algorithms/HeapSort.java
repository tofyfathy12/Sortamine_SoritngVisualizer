package com.app.sortamine.algorithms;

import com.app.sortamine.events.CompareEvent;
import com.app.sortamine.events.SortingEvent;
import com.app.sortamine.events.SwapEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeapSort implements SortingStrategy {
    private static HeapSort instance;

    private int[] arr;
    private int heapSize;

    private int comparisons = 0;
    private int interchanges = 0;

    private List<SortingEvent> sortingEvents = new ArrayList<>();

    private HeapSort() {
    }

    public static HeapSort getInstance() {
        if (instance == null) {
            instance = new HeapSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;
        this.sortingEvents.clear();

        return sort(originalData);
    }

    @Override
    public String getAlgorithmName() {
        return "Heap Sort";
    }

    @Override
    public int getComparisons() {
        return this.comparisons;
    }

    @Override
    public int getInterchanges() {
        return this.interchanges;
    }

    public int getHeapSize() {
        return this.heapSize;
    }

    public int extractMax() {
        int maxNum = arr[0];
        arr[0] = arr[this.heapSize - 1];
        arr[this.heapSize - 1] = maxNum;
        this.heapSize -= 1;
        maxHeapify(0);
        return maxNum;
    }

    public int peek() {
        return this.arr[0];
    }

    public void buildMaxHeap() {
        int i = this.arr.length / 2 - 1;
        while (i >= 0) {
            maxHeapify(i);
            i--;
        }
    }

    private void maxHeapify(int index) {
        while (true) {
            int l = 2 * (index + 1) - 1;
            int r = 2 * (index + 1);

            int largest = index;
            if (l < this.heapSize && this.arr[l] > this.arr[index])
                largest = l;

            int temp = largest;
            if (r < this.heapSize && this.arr[r] > this.arr[largest])
                largest = r;

            if (l < this.heapSize) {
                this.sortingEvents.add(new CompareEvent(l, index));
                this.comparisons++;
            }
            if (r < this.heapSize) {
                this.sortingEvents.add(new CompareEvent(r, temp));
                this.comparisons++;
            }

            if (index != largest) {
                temp = this.arr[index];
                this.arr[index] = this.arr[largest];
                this.arr[largest] = temp;

                this.sortingEvents.add(new SwapEvent(index, largest));
                this.interchanges++;

                index = largest;
            } else {
                break;
            }
        }
    }

    public List<SortingEvent> sort(int[] arr) {
        this.arr = Arrays.copyOf(arr, arr.length);
        this.heapSize = arr.length;
        buildMaxHeap();

        for (int i = arr.length; i > 1; i--) {
            int maxNum = this.arr[0];
            this.arr[0] = this.arr[this.heapSize - 1];
            this.arr[this.heapSize - 1] = maxNum;
            this.sortingEvents.add(new SwapEvent(0, this.heapSize - 1));
            this.interchanges++;
            this.heapSize--;
            maxHeapify(0);
        }

        return this.sortingEvents;
    }

    public static void main(String[] args) {
        HeapSort heapSort = HeapSort.getInstance();
        int[] arr = { 5, 3, 8, 1, 9, 2, 7, 4, 6 };
        System.out.println("Original: " + Arrays.toString(arr));
        List<SortingEvent> events = heapSort.generateSortHistory(arr);
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
        System.out.println("|          HEAP SORT STATS             |");
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
