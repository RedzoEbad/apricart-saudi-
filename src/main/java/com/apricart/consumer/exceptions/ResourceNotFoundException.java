package com.apricart.consumer.exceptions;

import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LanguageType;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String entityName;
    private final Long id;
    private final AddressType type;

    public ResourceNotFoundException(String entityName, Long id, Boolean isArabic) {
        super(getMessage(entityName, id, isArabic));
        this.entityName = entityName;
        this.id = id;
        this.type = null;
    }

    public ResourceNotFoundException(String entityName, LanguageType languageType) {
        super(getMessage(entityName,  languageType));
        this.entityName = entityName;
        this.id = null;
        this.type = null;

    }
    public ResourceNotFoundException(String message, Boolean isMsg) {
        super(message);
        this.entityName = null;
        this.id = null;
        this.type = null;
    }
    public ResourceNotFoundException(String entityName, AddressType type , Long id) {
        super(entityName + " with id '" + id +" and type '"+ type + "' not found.");
        this.entityName = entityName;
        this.id = id;
        this.type = type;
    }
    private static String getMessage(String entityName, Long id, Boolean isArabic) {
        if (Boolean.TRUE.equals(isArabic)) {
            return String.format("%s بالمعرف '%d' غير موجود.", entityName, id);
        } else {
            return String.format("%s with id '%d' not found.", entityName, id);
        }
    }
    private static String getMessage(String entityName, LanguageType languageType) {
        if (LanguageType.ARB.equals(languageType)) {
            return String.format("%s غير موجود.", entityName);
        } else {
            return String.format("%s not found.", entityName);
        }
    }
}
