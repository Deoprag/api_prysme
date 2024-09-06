package com.deopraglabs.api_prysme.utils;

import java.util.Random;

public class DatabaseUtils {

    public static String generateUniquePhoneNumber(long id) {
        final Random random = new Random(id);
        final StringBuilder phoneNumber = new StringBuilder("00");

        for (int i = 0; i < 9; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

}
