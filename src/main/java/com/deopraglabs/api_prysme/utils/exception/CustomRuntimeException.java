package com.deopraglabs.api_prysme.utils.exception;

public class CustomRuntimeException {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(long id) {
            super("User with ID " + id + " not found");
        }
    }

    public static class TeamNotFoundException extends RuntimeException {
        public TeamNotFoundException(Long id) {
            super("Team with ID " + id + " not found");
        }
    }

    public static class ProductCategoryNotFoundException extends RuntimeException {
        public ProductCategoryNotFoundException(Long id) {
            super("Product Category with ID " + id + " not found");
        }
    }
}
