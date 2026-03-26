package com.app.sortamine.events;

public class MaxSelectionEvent extends SortingEvent{
    public final int maxIndex;
    public MaxSelectionEvent(int maxIndex) {
        super(SortingEventType.MAX_SELECTION);
        this.maxIndex = maxIndex;
    }

    @Override
    public void accept(SortingEventVisitor visitor) throws InterruptedException {
        visitor.visit(this);
    }
}
