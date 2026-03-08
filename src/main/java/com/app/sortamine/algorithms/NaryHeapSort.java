package com.app.sortamine.algorithms;

import com.app.sortamine.events.CompareEvent;
import com.app.sortamine.events.SortingEvent;
import com.app.sortamine.events.SwapEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NaryHeapSort implements SortingStrategy {

    private int[] arr;
    private int heapSize;

    private int n;

    private long comparisons = 0;
    private long interchanges = 0;

    private final List<SortingEvent> sortingEvents = new ArrayList<>();

    public NaryHeapSort(int n) {
        this.n = n;
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
        return "N-ary Heap Sort";
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public long getInterchanges() {
        return interchanges;
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

    private void buildMaxHeap() {
        int i = this.arr.length / 2 - 1;
        while (i >= 0) {
            maxHeapify(i);
            i--;
        }
    }

    private void maxHeapify(int index) {
        while (true) {

            int largest = index;

            int l = 2 * (index + 1) - 1;
            int r = 2 * (index + 1);

            int currentChild = this.n * (index + 1) - (this.n - 2) - 1;
            for (int i = 0; i < this.n; i++) {
                if (currentChild < this.heapSize) {
                    this.sortingEvents.add(new CompareEvent(currentChild, largest));
                    this.comparisons++;
                }
                if (currentChild < this.heapSize && this.arr[currentChild] > this.arr[largest]) {
                    largest = currentChild;
                }
                currentChild++;
            }

            if (index != largest) {
                int temp = this.arr[index];
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
