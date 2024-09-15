package com.deopraglabs.api_prysme.utils;

import java.util.List;

public class Utils {

    public static boolean isEmpty(String str) { return str == null || str.isEmpty(); }

    public static void checkField(List<String> validations, boolean condition, String message) {
        if (condition) validations.add(message);
    }
}
