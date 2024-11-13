package com.deopraglabs.api_prysme.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Utils {

    private static PasswordEncoder passwordEncoder;

    @Autowired
    Utils(PasswordEncoder passwordEncoder) {
        Utils.passwordEncoder = passwordEncoder;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    public static void checkField(List<String> validations, boolean condition, String message) {
        if (condition) validations.add(message);
    }

    public static String removeSpecialCharacters(String str) {
        return str.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static Date formatStringToDate(String date) {
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
