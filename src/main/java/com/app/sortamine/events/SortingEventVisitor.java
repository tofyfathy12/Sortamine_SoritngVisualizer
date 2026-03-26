package com.app.sortamine.events;

public interface SortingEventVisitor {
    void visit(SwapEvent event) throws InterruptedException;
    void visit(CompareEvent event) throws InterruptedException;
    void visit(OverwriteEvent event) throws InterruptedException;
    void visit(MinSelectionEvent event) throws InterruptedException;
    void visit(MaxSelectionEvent event) throws InterruptedException;
    void visit(ConsumeEvent event) throws InterruptedException;
    void visit(BlockEvent event) throws InterruptedException;
}
