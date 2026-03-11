package com.apricart.consumer.exceptions;

import com.apricart.consumer.security.enums.LanguageType;

public class WishListException extends IllegalStateException {
    public WishListException(String entityName, String productName, String customerName, LanguageType languageType) {
        super(getMessage(entityName, productName, customerName, languageType));
    }

    private static String getMessage(String entityName, String productName, String customerName, LanguageType languageType) {
        if (LanguageType.ARB.equals(languageType)) {
            return String.format("%s بالمنتج '%s' والعميل '%s' موجود بالفعل.", entityName, productName, customerName);
        } else {
            return String.format("%s with the product '%s' and customer '%s' already exists.", entityName, productName, customerName);
        }
    }
}
