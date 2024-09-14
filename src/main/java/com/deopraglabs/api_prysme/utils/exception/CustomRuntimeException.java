package com.deopraglabs.api_prysme.utils.exception;

import java.util.List;

public class CustomRuntimeException {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(long id) {
            super("User with ID " + id + " not found");
        }
    }

    public static class UserBRValidationException extends RuntimeException {
        public UserBRValidationException(List<String> businessRules) {
            super(buildBRMessage(businessRules));
        }

        protected static String buildBRMessage(List<String> businessRules) {
            final StringBuilder sb = new StringBuilder();
            sb.append("User information validation failed: \n");
            businessRules.forEach(br -> sb.append(br).append("\n"));
            sb.append(".");
            return sb.toString();
        }
    }

    public static class TeamNotFoundException extends RuntimeException {
        public TeamNotFoundException(long id) {
            super("Team with ID " + id + " not found");
        }
    }

    public static class ProductCategoryNotFoundException extends RuntimeException {
        public ProductCategoryNotFoundException(long id) {
            super("Product Category with ID " + id + " not found");
        }
    }

    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(long id) {
            super("Customer with ID " + id + " not found");
        }
    }
}
