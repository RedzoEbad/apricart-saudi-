package com.apricart.consumer.service;

import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.enity.PendingOrder;
import com.apricart.consumer.security.enums.LanguageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PendingOrderEmailService {
    private static final Logger logger = LoggerFactory.getLogger(PendingOrderEmailService.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private PendingOrderService pendingOrderService;

    @Transactional
    @Scheduled(cron = "0 0 */6 * * 1-7")
    public void resendPendingOrderEmails() {
        try {
            List<PendingOrder> pendingOrderList = pendingOrderService.getActivePendingOrders(LanguageType.ENG);
            for (PendingOrder pendingOrder : pendingOrderList) {
                Orders order = orderService.findById(pendingOrder.getOrderId(), LanguageType.ENG);
                if (order != null) {
                    orderService.sendOrderEmail(order, LanguageType.ENG);
                }
            }
            logger.info("PendingOrderEmails completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during PendingOrderEmails.", e);
        }
    }

}
