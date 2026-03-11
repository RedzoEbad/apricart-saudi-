package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.enity.UserPortal;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

public interface BaseService {

   Customer resolveUser(HttpServletRequest request);
   String isEmpty(String request);

    String isEmpty();

    boolean isEmptyCheck(String request);
    boolean isNotLocal(Environment env);
    boolean getAuthorization(Roles roles, String endPoint);
    boolean isSuperAdmin(UserPortal user);
}


