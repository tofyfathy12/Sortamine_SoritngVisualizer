package com.app.sortamine.algorithms;

import com.app.sortamine.events.SortingEvent;

import java.util.List;

public interface SortingStrategy {
    List<SortingEvent> generateSortHistory(int[] originalData);

    String getAlgorithmName();

    long getComparisons();

    long getInterchanges();
}
