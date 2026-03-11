package com.apricart.consumer.service;

import com.apricart.consumer.enity.Notification;
import com.apricart.consumer.security.dto.dto.NotificationDTO;

public interface  NotificationService {
    void sendNotification(NotificationDTO notificationDTO) throws Exception;

    Notification save(NotificationDTO notificationDTO);
}
