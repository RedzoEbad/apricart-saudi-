package com.apricart.consumer.service.Impl;

import com.apricart.consumer.security.dto.dto.PushNotificationDTO;
import com.apricart.consumer.security.utils.NotificationParameter;
import com.apricart.consumer.service.FCMInitializerDataService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMInitializerDataServiceImpl implements FCMInitializerDataService {

    private final String firebaseConfigPath;
    private final String firebaseConfigJson;

    public FCMInitializerDataServiceImpl(
            @Value("${app.firebase-configuration-file}") String firebaseConfigPath,
            @Value("${app.firebase-configuration-json:#{null}}") String firebaseConfigJson) {
        this.firebaseConfigPath = firebaseConfigPath;
        this.firebaseConfigJson = firebaseConfigJson;
    }

    @PostConstruct
    public void initializeFirebase() {
        try {
            GoogleCredentials credentials;
            if (firebaseConfigJson != null && !firebaseConfigJson.isEmpty()) {
                credentials = GoogleCredentials.fromStream(new java.io.ByteArrayInputStream(firebaseConfigJson.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
                System.out.println("Initializing Firebase using JSON environment variable");
            } else {
                credentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());
                System.out.println("Initializing Firebase using config file: " + firebaseConfigPath);
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase application has been initialized");
            }
        } catch (Exception e) {
            System.out.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessageBroadcast(PushNotificationDTO request) {
        sendMessageBroadcast(new HashMap<>(), request);
    }

    public void sendMessageBroadcast(Map<String, String> data, PushNotificationDTO request) {
        if (data.isEmpty()) {
            data.put("title", request.getTitle());
            data.put("body", request.getMessage());
        }
        Message message = getPreconfiguredMessageWithData(data, request);
        sendNotification(message);
    }

    public void sendMessageToToken(PushNotificationDTO request) {
        sendMessageToToken(new HashMap<>(), request);
    }

    public void sendMessageToToken(Map<String, String> data, PushNotificationDTO request) {
        request.setTopic(request.getTitle());
        if (data.isEmpty()) {
            data.put("title", request.getTitle());
            data.put("body", request.getMessage());
        }
        Message message = getPreconfiguredMessageToTokenMessageWithData(data, request);
        sendNotification(message);
    }

    private void sendNotification(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            System.out.println("\nFCM Notification Response: " + response);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error sending FCM notification: " + e.getMessage());
        }
    }

    private AndroidConfig getAndroidConfig(String channel) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis())
                .setCollapseKey(channel)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setSound(NotificationParameter.SOUND.getValue())
                        .setChannelId(channel)
                        .setColor(NotificationParameter.COLOR.getValue())
                        .setTag(channel)
                        .build())
                .build();
    }

    private ApnsConfig getApnsConfig(String channel) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setSound(NotificationParameter.SOUND.getValue())
                        .setCategory(channel)
                        .setContentAvailable(true)
                        .setThreadId(channel)
                        .build())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationDTO request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getChannel());
        ApnsConfig apnsConfig = getApnsConfig(request.getChannel());
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setNotification(new Notification(request.getTitle(), request.getMessage()));
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationDTO request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(data)
                .setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageToTokenMessageWithData(Map<String, String> data, PushNotificationDTO request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(data)
                .setToken(request.getToken())
                .build();
    }
}
