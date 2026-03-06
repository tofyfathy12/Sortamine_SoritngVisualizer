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

    private long comparisons = 0;
    private long interchanges = 0;

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
    public long getComparisons() {
        return this.comparisons;
    }

    @Override
    public long getInterchanges() {
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
}
