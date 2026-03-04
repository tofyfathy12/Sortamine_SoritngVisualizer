package com.app.sortamine.utils;

import java.util.Random;

public class SortedArrayGenerator implements ArrayGenerator {
    private static SortedArrayGenerator instance;

    private Random random = new Random();

    private SortedArrayGenerator() {
    }

    public static SortedArrayGenerator getInstance() {
        if (instance == null) {
            instance = new SortedArrayGenerator();
        }
        return instance;
    }

    @Override
    public int[] generate(int size, double height) {
        int[] array = new int[size];
        int maxStep = Math.max(2, (int) (2.0 * height / size));
        array[0] = 1 + random.nextInt(maxStep);
        for (int i = 1; i < size; i++) {
            array[i] = array[i - 1] + 1 + random.nextInt(maxStep);
        }
        return array;
    }
}
