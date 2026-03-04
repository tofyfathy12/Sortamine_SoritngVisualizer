package com.app.sortamine.events;

public class CompareEvent extends SortingEvent {
    public final int indexA;
    public final int indexB;

    public CompareEvent(int indexA, int indexB) {
        super(SortingEventType.COMPARE);
        this.indexA = indexA;
        this.indexB = indexB;
    }
}
