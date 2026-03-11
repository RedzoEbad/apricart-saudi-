package com.apricart.consumer.service;



import com.apricart.consumer.security.dto.dto.PushNotificationDTO;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FCMInitializerDataService {

    void initializeFirebase();

    void sendMessageBroadcast(PushNotificationDTO request) throws InterruptedException, ExecutionException;

    void sendMessageBroadcast(Map<String, String> data, PushNotificationDTO request) throws InterruptedException, ExecutionException;

    void sendMessageToToken(PushNotificationDTO request) throws InterruptedException, ExecutionException;

    void sendMessageToToken(Map<String, String> data, PushNotificationDTO request) throws InterruptedException, ExecutionException;
}
