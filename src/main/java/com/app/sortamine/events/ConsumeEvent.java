package com.app.sortamine.events;

public class ConsumeEvent extends SortingEvent{
    public final int index;
    public ConsumeEvent(int index) {
        super(SortingEventType.CONSUME);
        this.index = index;
    }

    @Override
    public void accept(SortingEventVisitor visitor) throws InterruptedException {
        visitor.visit(this);
    }
}
