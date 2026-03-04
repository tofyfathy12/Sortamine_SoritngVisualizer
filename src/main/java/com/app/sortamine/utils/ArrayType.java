package com.app.sortamine.utils;

import com.app.sortamine.algorithms.SortingStrategy;

public enum ArrayType {
    RANDOM("Random", RandomArrayGenerator.getInstance()),
    SORTED("Sorted", SortedArrayGenerator.getInstance()),
    INVERSELY_SORTED("Inversely Sorted", InverselySortedArrayGenerator.getInstance());

    private final String displayName;
    private final ArrayGenerator generator;

    ArrayType(String displayName, ArrayGenerator generator) {
        this.displayName = displayName;
        this.generator = generator;
    }

    public ArrayGenerator getGenerator() {
        return generator;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
