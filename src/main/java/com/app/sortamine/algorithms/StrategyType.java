package com.app.sortamine.algorithms;

public enum StrategyType {
    BUBBLE_SORT("Bubble Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return BubbleSort.getInstance();
        }
    },
    HEAP_SORT("Heap Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return HeapSort.getInstance();
        }
    },
    INSERTION_SORT("Insertion Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return InsertionSort.getInstance();
        }
    },
    MERGE_SORT("Merge Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return MergeSort.getInstance();
        }
    },
    QUICK_SORT("Quick Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return QuickSort.getInstance();
        }
    },
    SELECTION_SORT("Selection Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return SelectionSort.getInstance();
        }
    },
    N_ARY_HEAP_SORT("N-ary Heap Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return new NaryHeapSort(3);
        }

        @Override
        public SortingStrategy getStrategy(int n) {
            return new NaryHeapSort(n);
        }
    },
    SHELL_SORT("Shell Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return ShellSort.getInstance();
        }
    },
    COUNTING_SORT("Counting Sort") {
        @Override
        public SortingStrategy getStrategy() {
            return CountingSort.getInstance();
        }
    };

    private final String displayName;

    StrategyType(String displayName) {
        this.displayName = displayName;
    }

    public abstract SortingStrategy getStrategy();

    public SortingStrategy getStrategy(int n) {
        return getStrategy();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
