package com.apricart.consumer.security.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorRegistry implements WebMvcConfigurer {

    @Autowired
    private IPAddressInterceptor ipAddressInterceptor;

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(ipAddressInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name", "").toLowerCase();
        String uploadsPath;
        if (os.contains("win")) {
            uploadsPath = "file:///" + System.getProperty("user.home").replace("\\", "/") + "/Uploads/";
        } else if (new java.io.File("/home/ubuntu/uploads/").exists()) {
            uploadsPath = "file:/home/ubuntu/uploads/";
        } else {
            uploadsPath = "file:" + System.getProperty("user.home") + "/Uploads/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsPath);
    }
}
