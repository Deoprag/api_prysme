package com.deopraglabs.api_prysme.utils;

import com.deopraglabs.api_prysme.data.model.Product;

import java.util.List;

public class Utils {

    public static boolean isEmpty(String str) { return str == null || str.isEmpty(); }

    public static void checkField(List<String> validations, boolean condition, String message) {
        if (condition) validations.add(message);
    }

    public static double calculateTotalCartPrice(List<Product> products) {
        double sum = 0;
        for (final Product product : products) {
            sum += product.getPrice().doubleValue();
        }
        return sum;
    }
}
