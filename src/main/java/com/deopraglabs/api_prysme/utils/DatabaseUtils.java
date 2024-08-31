package com.deopraglabs.api_prysme.utils;

public class DatabaseUtils {

    public static String generateUniquePhoneNumber(Long id) {
        final String idString = String.valueOf(id);
        final String phoneNumberSuffix = idString.length() > 8 ? idString.substring(idString.length() - 8) : idString;
        return "999" + phoneNumberSuffix;
    }

}
