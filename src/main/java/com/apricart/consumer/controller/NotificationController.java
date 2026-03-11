package com.apricart.consumer.controller;

import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.NotificationDTO;
import com.apricart.consumer.security.enums.LanguageType;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/open/notification")
@Api(value = "Notification Controller", tags = {"Notification"})
public class NotificationController {
    @Autowired
    NotificationController notificationController;

    @PostMapping
    public ResponseEntity<?> sendNotification(@Valid @RequestBody NotificationDTO notificationDTO, @RequestHeader("Language") LanguageType lang) {
        notificationController.sendNotification(notificationDTO, lang);
        return Response.success();
    }
}