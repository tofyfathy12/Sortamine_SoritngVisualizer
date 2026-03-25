package com.app.sortamine.utils;

import java.util.Random;

public class InverselySortedArrayGenerator implements ArrayGenerator {
    private static InverselySortedArrayGenerator instance;

    private final Random random = new Random();

    private InverselySortedArrayGenerator() {
    }

    public static InverselySortedArrayGenerator getInstance() {
        if (instance == null) {
            instance = new InverselySortedArrayGenerator();
        }
        return instance;
    }

    @Override
    public int[] generate(int size, double height) {
        int[] array = new int[size];
        int maxStep = Math.max(2, (int) (2.0 * height / size));
        array[size - 1] = 1 + random.nextInt(maxStep);
        for (int i = size - 2; i >= 0; i--) {
            array[i] = array[i + 1] + 1 + random.nextInt(maxStep);
        }
        return array;
    }
}
