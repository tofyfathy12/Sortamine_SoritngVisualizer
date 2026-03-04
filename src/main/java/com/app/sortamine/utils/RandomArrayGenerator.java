package com.app.sortamine.utils;

import java.util.Random;

public class RandomArrayGenerator implements ArrayGenerator {
    private static RandomArrayGenerator instance;

    private Random random = new Random();

    private RandomArrayGenerator() {
    }

    public static RandomArrayGenerator getInstance() {
        if (instance == null) {
            instance = new RandomArrayGenerator();
        }
        return instance;
    }

    @Override
    public int[] generate(int size, double height) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt((int) height + 1);
        }
        return array;
    }
}
