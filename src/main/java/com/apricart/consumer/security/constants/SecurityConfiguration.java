package com.apricart.consumer.security.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("securityConstantsConfiguration")
public class SecurityConfiguration {

    @Value("${jwt.secret:mySecretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        SecurityConstants.SECRET_KEY = secretKey;
    }
}
