package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.PendingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingOrderRepository extends JpaRepository<PendingOrder, Long> {
    PendingOrder findPendingOrdersByOrderId(String orderId);
}
