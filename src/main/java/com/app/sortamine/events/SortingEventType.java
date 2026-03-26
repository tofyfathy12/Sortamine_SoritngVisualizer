package com.app.sortamine.events;

public enum SortingEventType {
    SWAP,
    COMPARE,
    OVERWRITE,
    MIN_SELECTION, // to be colored when visualizing
    MAX_SELECTION, // to be colored when visualizing
    BLOCK,
    CONSUME,
}
