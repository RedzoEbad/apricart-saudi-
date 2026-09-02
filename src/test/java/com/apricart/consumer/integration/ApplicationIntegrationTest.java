package com.apricart.consumer.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationIntegrationTest {

    @Test
    void contextLoads() {
        // Add integration tests for API, database and security boundaries here.
    }
}
