package com.app.sortamine.algorithms;

import com.app.sortamine.events.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockSort implements SortingStrategy{

    private long comparisons = 0;
    private long interchanges = 0;
    private final List<SortingEvent> sortingEvents = new ArrayList<>();

    static class Node {
        int value;
        int index;
        int originalIndex;

        public Node(int value, int index, int originalIndex) {
            this.value = value;
            this.index = index;
            this.originalIndex = originalIndex;
        }
    }
    private final ArrayList<Node> list = new ArrayList<>();

    private static BlockSort instance;

    private BlockSort() {
    }

    public static BlockSort getInstance() {
        if (instance == null) {
            instance = new BlockSort();
        }
        return instance;
    }

    @Override
    public List<SortingEvent> generateSortHistory(int[] originalData) {
        this.comparisons = 0;
        this.interchanges = 0;
        sortingEvents.clear();
        int[] data = Arrays.copyOf(originalData, originalData.length);

        int blockSize = Math.max(3, (int) Math.sqrt(data.length));
        float saturation = 0.9f; // High saturation for vivid colors
        float brightness = 0.9f; // High brightness for visibility

        int blocksLength = Math.ceilDiv(data.length, blockSize);
        int[][] blocks = new int[blocksLength][2]; // stores start, end (exclusive) indices for the blocks
        for (int i = 0; i < data.length; i += blockSize) {
            int index = i / blockSize;
            blocks[index][0] = i;
            blocks[index][1] = Math.min(i + blockSize, data.length);
            float hue = ((float) index / blocksLength) * 360f; // Evenly spaced hue values from 0.0 to 360.0
            sortingEvents.add(new BlockEvent(Color.hsb(hue, saturation, brightness), blocks[index][0], blocks[index][1]));
        }

        for (int j = 0; j < blocksLength; j++) {
            sortShell(data, sortingEvents, blocks[j][0], blocks[j][1]);
            heapInsert(new Node(data[blocks[j][0]], j, blocks[j][0]));
            sortingEvents.add(new ConsumeEvent(blocks[j][0]));
            blocks[j][0]++;
        }

        int[] result = new int[data.length];
        int resultIndex = 0;
        
        while (getHeapSize() > 0) {
            Node node = heapExtractMin();
            result[resultIndex++] = node.value;
            sortingEvents.add(new OverwriteEvent(resultIndex - 1, node.value));

            int bIndex = node.index;
            if (blocks[bIndex][0] < blocks[bIndex][1]) {
                heapInsert(new Node(data[blocks[bIndex][0]], bIndex, blocks[bIndex][0]));
                sortingEvents.add(new ConsumeEvent(blocks[bIndex][0]));
                blocks[bIndex][0]++;
            }
        }
        return sortingEvents;
    }

    private void sortShell(int[] copy, List<SortingEvent> sortingEvents, int start, int end) {
        int length = end - start;
        int gap = (length >> 1);
        while (gap > 0) {
            for (int i = start + gap; i < end; i++) {
                int key = copy[i];
                int j = i - gap;
                while (j >= start && copy[j] > key) {
                    this.comparisons++;
                    sortingEvents.add(new CompareEvent(j, j + gap));

                    copy[j + gap] = copy[j];

                    sortingEvents.add(new SwapEvent(j + gap, j));
                    this.interchanges++;

                    j -= gap;
                }
                if (j >= start) {
                    this.comparisons++;
                    sortingEvents.add(new CompareEvent(j, j + gap));
                }
                copy[j + gap] = key;
            }
            gap = (gap >> 1);
        }
    }

    private int getHeapSize() {
        return this.list.size();
    }

    private Node heapExtractMin() {
        Node minNum = list.getFirst();
        this.sortingEvents.add(new MinSelectionEvent(minNum.originalIndex));
        Node last = list.removeLast();
        if (!list.isEmpty()) {
            list.set(0, last);
            minHeapify(0);
        }
        return minNum;
    }

    private void heapInsert(Node num) {
        this.list.add(num);
        this.sortingEvents.add(new MinSelectionEvent(num.originalIndex));
        int i = this.list.size() - 1;
        while (i > 0) {
            int parent = (i - 1) / 2;
            this.sortingEvents.add(new CompareEvent(list.get(i).originalIndex, list.get(parent).originalIndex));
            this.comparisons++;
            if (list.get(i).value < list.get(parent).value) {
                Collections.swap(list, i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void buildMinHeap() {
        int i = this.list.size() / 2 - 1;
        while (i >= 0) {
            minHeapify(i);
            i--;
        }
    }

    private void minHeapify(int index) {
        while (true) {
            int size = getHeapSize();
            int l = 2 * index + 1;
            int r = 2 * index + 2;

            int smallest = index;
            if (l < size && list.get(l).value < list.get(smallest).value)
                smallest = l;

            int temp = smallest;
            if (r < size && list.get(r).value < list.get(smallest).value)
                smallest = r;

            if (l < size) {
                this.sortingEvents.add(new CompareEvent(list.get(l).originalIndex, list.get(index).originalIndex));
                this.comparisons++;
            }
            if (r < size) {
                this.sortingEvents.add(new CompareEvent(list.get(r).originalIndex, list.get(temp).originalIndex));
                this.comparisons++;
            }

            if (smallest == index) break;
            Collections.swap(list, index, smallest);
            index = smallest;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Block Sort";
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
