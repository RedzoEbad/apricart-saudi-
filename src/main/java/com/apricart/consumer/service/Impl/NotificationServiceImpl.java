package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Notification;
import com.apricart.consumer.repository.jpa.NotificationRepository;
import com.apricart.consumer.security.dto.dto.NotificationDTO;
import com.apricart.consumer.security.dto.dto.PushNotificationDTO;
import com.apricart.consumer.service.FCMInitializerDataService;
import com.apricart.consumer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    FCMInitializerDataService fcmInitializerDataService;

    @Autowired
    NotificationRepository notificationRepository;
    @Override
    public void sendNotification(NotificationDTO notificationDTO) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("type", notificationDTO.getType());
        map.put("sku", notificationDTO.getValue());
        map.put("title", notificationDTO.getTitle());
        map.put("message", notificationDTO.getMessage());

        PushNotificationDTO pushNotificationRequest = PushNotificationDTO.builder()
                .channel("topics-" + notificationDTO.getSendTo())
                .topic("topics-" + notificationDTO.getSendTo())
                .title(notificationDTO.getTitle())
                .message(notificationDTO.getMessage())
                .build();
        try {
            fcmInitializerDataService.sendMessageBroadcast(map, pushNotificationRequest);
            save(notificationDTO);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(e.getMessage(),e.getCause());
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Notification save(NotificationDTO notificationDTO) {
        Notification notification = Notification.fromDTO(notificationDTO);
        return notificationRepository.save(notification);
    }


}
