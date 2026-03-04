package com.app.sortamine.events;

public class MinSelectionEvent extends SortingEvent{
    public final int minIndex;
    public MinSelectionEvent(int minIndex) {
        super(SortingEventType.MIN_SELECTION);
        this.minIndex = minIndex;
    }
}
