package com.deopraglabs.api_prysme.utils.exception;

public class CustomRuntimeException {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(long id) {
            super("User with ID " + id + " not found");
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
