package com.apricart.consumer.security.enums;

/**
 * Price book types stored on price_list.price_book_type.
 * PERCENTAGE is kept for legacy DB rows; prefer FIXED_PERCENTAGE for new data.
 */
public enum PriceBookType {
    FIXED_PERCENTAGE,
    PERCENTAGE,
    PER_ITEM,
    FLAT
}
