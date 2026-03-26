package com.app.sortamine.events;

import javafx.scene.paint.Color;

public class BlockEvent extends SortingEvent {
    public final Color color;
    public final int startIndex;
    public final int endIndex;
    public BlockEvent(Color color, int startIndex, int endIndex) {
        super(SortingEventType.BLOCK);
        this.color = color;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void accept(SortingEventVisitor visitor) throws InterruptedException {
        visitor.visit(this);
    }
}
