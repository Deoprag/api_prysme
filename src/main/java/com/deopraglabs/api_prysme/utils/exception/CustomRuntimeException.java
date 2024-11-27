package com.deopraglabs.api_prysme.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.util.List;

public class CustomRuntimeException {
    public static class GenericException extends RuntimeException {
        public GenericException(String message) {
            super(message);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class BRValidationException extends RuntimeException {
        private final List<String> errors;
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(long id) {
            super("User with ID " + id + " not found");
        }
    }

    public static class SellerNotFoundException extends RuntimeException {
        public SellerNotFoundException(long id) {
            super("Seller with ID " + id + " not found");
        }
    }

    public static class UserNotFoundByUsernameException extends RuntimeException {
        public UserNotFoundByUsernameException(String username) {
            super("User with username " + username + " not found");
        }
    }

    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(long id) {
            super("Customer with ID " + id + " not found");
        }
    }

    public static class TeamNotFoundException extends RuntimeException {
        public TeamNotFoundException(long id) {
            super("Team with ID " + id + " not found");
        }
    }

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(long id) {
            super("Product with ID " + id + " not found");
        }
    }

    public static class ProductCategoryNotFoundException extends RuntimeException {
        public ProductCategoryNotFoundException(long id) {
            super("Product Category with ID " + id + " not found");
        }
    }

    public static class QuotationNotFoundException extends RuntimeException {
        public QuotationNotFoundException(long id) {
            super("Quotation with ID " + id + " not found");
        }
    }

    public static class ContactNotFoundException extends RuntimeException {
        public ContactNotFoundException(long id) {
            super("Contact with ID " + id + " not found");
        }
    }

    public static class SalesOrderNotFoundException extends RuntimeException {
        public SalesOrderNotFoundException(long id) {
            super("Sales Order with ID " + id + " not found");
        }
    }

    public static class NFNotFoundException extends RuntimeException {
        public NFNotFoundException(long id) {
            super("NF with ID " + id + " not found");
        }
    }

    public static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(long id) {
            super("Task with ID " + id + " not found");
        }
    }

    public static class PermissionNotFoundException extends RuntimeException {
        public PermissionNotFoundException(long id) {
            super("Permission with ID " + id + " not found");
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class InvalidJwtAuthenticationException extends AuthenticationException {
        public InvalidJwtAuthenticationException(String message) {super(message);}
    }

    public static class InvalidUsernameOrPasswordException extends RuntimeException {
        public InvalidUsernameOrPasswordException(String message) {super(message);}
    }
}
