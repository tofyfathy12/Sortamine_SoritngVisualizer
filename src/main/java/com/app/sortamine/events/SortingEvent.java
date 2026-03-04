package com.app.sortamine.events;

public abstract class SortingEvent {
    public final SortingEventType type;

    protected SortingEvent(SortingEventType type) {
        this.type = type;
    }
}
