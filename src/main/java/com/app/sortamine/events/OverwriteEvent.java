package com.app.sortamine.events;

public class OverwriteEvent extends SortingEvent {
    public final int targetIndex;
    public final int newValue;

    public OverwriteEvent(int targetIndex, int newValue) {
        super(SortingEventType.OVERWRITE);
        this.targetIndex = targetIndex;
        this.newValue = newValue;
    }
}
