package com.deopraglabs.api_prysme.utils;

import java.util.Random;

public class DatabaseUtils {

    public static String generateRandomValue(long id, int size) {
        final StringBuilder random = new StringBuilder("000");
        final Random randomGenerator = new Random(id);

        for (int i = 0; i < size - 3; i++) {
            random.append(randomGenerator.nextInt(10));
        }

        return random.toString();
    }

}
