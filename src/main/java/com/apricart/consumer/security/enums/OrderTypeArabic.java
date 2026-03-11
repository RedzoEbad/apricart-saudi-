package com.apricart.consumer.security.enums;

import lombok.Getter;

@Getter
public enum OrderTypeArabic {
    PENDING("قيد الانتظار"),
    CANCELLED("ملغاة"),
    PROCESSING("قيد المعالجة"),
    DELIVERED("تم التسليم"),
    NOT_DELIVERED("لم يتم التسليم");

    private final String translation;

    OrderTypeArabic(String translation) {
        this.translation = translation;
    }
    public static String getTranslationForStatus(OrderType status) {
        return OrderTypeArabic.valueOf(status.name()).getTranslation();
    }

}
