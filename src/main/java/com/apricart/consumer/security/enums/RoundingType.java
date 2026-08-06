package com.apricart.consumer.security.enums;

/**
 * Rounding modes on price_list.rounding_type.
 * NONE / FLOOR / CEILING exist in legacy DB rows.
 */
public enum RoundingType {
    ROUND_TO_DOLLAR_MINUS_01,
    NO_ROUNDING,
    NONE,
    FLOOR,
    CEILING,
    NEAREST_WHOLE_NUMBER
}
