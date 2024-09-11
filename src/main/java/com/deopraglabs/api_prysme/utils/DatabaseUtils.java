package com.deopraglabs.api_prysme.utils;

import java.util.Random;

public class DatabaseUtils {

    public static String generateRandomValue(long id, int size) {
        final StringBuilder random = new StringBuilder("00");

        for (int i = 0; i < size - 2; i++) {
            random.append(new Random(id).nextInt(size - 1));
        }

        return random.toString();
    }

}
