package com.apricart.consumer.exceptions;

/**
 * Thrown when creating/updating a resource that violates a business uniqueness rule
 * (e.g. duplicate category name, subcategory name within a category, product title within a subcategory).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
