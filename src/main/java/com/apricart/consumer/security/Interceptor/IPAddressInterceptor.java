package com.apricart.consumer.security.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IPAddressInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String ipAddress = request.getHeader("X-Forward-For");
        if(ipAddress== null){ ipAddress = request.getRemoteAddr(); }
        String requestURL = request.getRequestURI();
        String phoneNumber = request.getRemoteUser();
        String localAddr = request.getLocalAddr();
        String getLocalName = request.getLocalName();


        if (requestURL.toLowerCase().contains("/v1/options/stream")) { return true; }
        else if (requestURL.equalsIgnoreCase("/")) { return false; }
        else {
            System.out.println(
                    "ipaddress: "+ipAddress+
                            ", url: "+requestURL+
                            ", phoneNumber: "+phoneNumber+
                            ", localAddr: "+localAddr+
                            ", getLocalName: "+getLocalName
            );
            return true;
        }


    }
}
