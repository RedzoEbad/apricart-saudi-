package com.apricart.consumer.service;

import com.apricart.consumer.enity.PendingOrder;
import com.apricart.consumer.security.dto.request.PendingOrderRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.google.protobuf.ServiceException;

import java.util.List;

public interface PendingOrderService {
    List<PendingOrder> getAllPendingOrders(LanguageType lang);
    PendingOrder findByOrderId(String id, LanguageType languageType);
    List<PendingOrder> getActivePendingOrders(LanguageType lang);
    void addPendingOrder(PendingOrderRequestDTO pendingOrderRequestDTO) throws ServiceException;
    void updatePendingOrder(PendingOrderRequestDTO pendingOrderRequestDTO, LanguageType languageType);
    void deletePendingOrder(String id, LanguageType languageType);
}
