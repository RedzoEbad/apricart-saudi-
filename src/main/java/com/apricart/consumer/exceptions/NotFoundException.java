package com.apricart.consumer.exceptions;

import com.apricart.consumer.security.enums.AddressType;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String entityName;
    private final Long id;
    private final AddressType type;

    public NotFoundException(String entityName, Long id) {
        super(entityName + " with id '" + id + "' not found.");
        this.entityName = entityName;
        this.id = id;
        this.type = null;
    }

    public NotFoundException(String entityName) {
        super(entityName + " not found.");
        this.entityName = entityName;
        this.id = null;
        this.type = null;

    }
    public NotFoundException(String entityName, AddressType type , Long id) {
        super(entityName + " with id '" + id +" and type '"+ type + "' not found.");
        this.entityName = entityName;
        this.id = id;
        this.type = type;
    }

}
