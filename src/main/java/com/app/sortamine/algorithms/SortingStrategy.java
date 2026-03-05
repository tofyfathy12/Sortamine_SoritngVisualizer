package com.app.sortamine.algorithms;

import com.app.sortamine.events.SortingEvent;
import com.app.sortamine.events.SwapEvent;

import java.util.List;

public interface SortingStrategy { // TODO: color the sorted portion of the array
    List<SortingEvent> generateSortHistory(int[] originalData);

    String getAlgorithmName();

    int getComparisons();

    int getInterchanges();
}
