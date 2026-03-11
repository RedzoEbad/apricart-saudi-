package com.apricart.consumer.security.enums;

import lombok.Getter;

@Getter
public enum EnvironmentType {
    PRODUCTION("ksa.apricart.com"),
    STAGING("staging.apricart.pk");


    private final String url;

    EnvironmentType(String url) {
        this.url = url;
    }

}
