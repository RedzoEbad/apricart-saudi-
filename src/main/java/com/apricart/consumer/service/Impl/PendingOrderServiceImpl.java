package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.PendingOrder;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.PendingOrderRepository;
import com.apricart.consumer.security.dto.request.PendingOrderRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.PendingOrderService;
import com.google.protobuf.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.PENDING_ORDER_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ArabicResponseMessages.PENDING_ORDER_STATUS_NOT_ACTIVE_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.PENDING_ORDER_NOT_FOUND;
import static com.apricart.consumer.security.constants.ResponseMessage.PENDING_ORDER_STATUS_NOT_ACTIVE;

@Service
public class PendingOrderServiceImpl implements PendingOrderService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PendingOrderServiceImpl.class);

    @Autowired
    private PendingOrderRepository pendingOrderRepository;

    @Override
    public List<PendingOrder> getAllPendingOrders(LanguageType lang) {
        LOGGER.info("Getting all pending orders");
        List<PendingOrder> pendingOrders = pendingOrderRepository.findAll().stream()
                .sorted(Comparator.comparingLong(PendingOrder::getId))
                .collect(Collectors.toList());

        if (!pendingOrders.isEmpty()) {
            return pendingOrders;
        } else {
            LOGGER.error("No pending order found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? PENDING_ORDER_NOT_FOUND_ARABIC : PENDING_ORDER_NOT_FOUND, true);
        }
    }

    @Override
    public PendingOrder findByOrderId(String id, LanguageType languageType) {
        LOGGER.info("Finding pending order by id: {}", id);
        return pendingOrderRepository.findPendingOrdersByOrderId(id);
    }

    @Override
    public List<PendingOrder> getActivePendingOrders(LanguageType lang) {
        LOGGER.info("Getting active pending orders");
        List<PendingOrder> pendingOrders = pendingOrderRepository.findAll().stream()
                .filter(PendingOrder::getStatus)
                .sorted(Comparator.comparingLong(PendingOrder::getId))
                .collect(Collectors.toList());

        if (!pendingOrders.isEmpty()) {
            return pendingOrders;
        } else {
            LOGGER.error("No active pending order found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? PENDING_ORDER_STATUS_NOT_ACTIVE_ARABIC : PENDING_ORDER_STATUS_NOT_ACTIVE, true);
        }
    }

    @Override
    public void addPendingOrder(PendingOrderRequestDTO pendingOrderRequestDTO) throws ServiceException {
        LOGGER.info("Adding Pending Order: {}", pendingOrderRequestDTO);
        try {
            save(PendingOrder.fromDTO(pendingOrderRequestDTO));
        } catch (Exception e) {
            LOGGER.error("Error occurred while adding banner: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public PendingOrder save(PendingOrder pendingOrder) {
        LOGGER.info("Saving Pending Order: {}", pendingOrder);
        return pendingOrderRepository.save(pendingOrder);
    }

    @Override
    public void updatePendingOrder(PendingOrderRequestDTO pendingOrderRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating Pending Order: {}", pendingOrderRequestDTO);
        PendingOrder existingPendingOrder = findByOrderId(pendingOrderRequestDTO.getOrderId(), languageType);

        existingPendingOrder.setOrderId(pendingOrderRequestDTO.getOrderId() == null ? existingPendingOrder.getOrderId() : pendingOrderRequestDTO.getOrderId());
        existingPendingOrder.setStatus(pendingOrderRequestDTO.getStatus() == null ? existingPendingOrder.getStatus() : pendingOrderRequestDTO.getStatus());
        save(existingPendingOrder);
    }

    @Override
    public void deletePendingOrder(String id, LanguageType languageType) {
        LOGGER.info("Deactivating pending order status for id: {}", id);
        PendingOrder existingPendingOrder = findByOrderId(id, languageType);
        if(existingPendingOrder.getStatus()) {
            existingPendingOrder.setStatus(false);
            save(existingPendingOrder);
        }
    }
}
