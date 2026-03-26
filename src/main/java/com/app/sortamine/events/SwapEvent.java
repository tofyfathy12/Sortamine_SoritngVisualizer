package com.app.sortamine.events;

public class SwapEvent extends SortingEvent {
    public final int indexA;
    public final int indexB;

    public SwapEvent(int indexA, int indexB) {
        super(SortingEventType.SWAP);
        this.indexA = indexA;
        this.indexB = indexB;
    }

    @Override
    public void accept(SortingEventVisitor visitor) throws InterruptedException {
        visitor.visit(this);
    }
}
